package com.example.projekt1

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.projekt1.databinding.ActivityCreateRoomBinding
import com.example.projekt1.databinding.ActivityJoinRoomBinding
import com.example.projekt1.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class JoinRoom : AppCompatActivity() {

    private lateinit var binding: ActivityJoinRoomBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    private var roomID = ""
    private var mail =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityJoinRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)


        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Proszę czekać")
        progressDialog.setMessage("Tworzenie pokoju...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.guzikJoinRoom.setOnClickListener {
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null){
                mail= firebaseUser.email.toString()
            }
            roomID = binding.roomJoinEt.text.toString().trim()
            addFirestore(roomID,mail)

            startActivity(Intent(this, Profile::class.java))
        }

        binding.guzikBack.setOnClickListener{
            startActivity(Intent(this, Profile::class.java))
        }
    }

    fun addFirestore(roomid: String, mail: String){
        val db = FirebaseFirestore.getInstance()
        var succ = 0
        db.collection("Rooms")
            .get()
            .addOnCompleteListener{
                val result: StringBuffer = StringBuffer()

                if(it.isSuccessful){
                    for(document in it.result!!){
                        val id = document.data.get("ID")
                        if (id == roomID){
                            val user: MutableMap<String, Any> = HashMap()
                            user["email"] = mail
                            user["role"] = "normal_user"
                            succ = 1
                            val docId = document.id
                                    db.collection("Rooms").document(docId).collection("Users")
                                   .add(user)
                                        .addOnSuccessListener {
                                    Toast.makeText(this, "Dołączono do pokoju", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener{
                                    Toast.makeText(this, "Nie udało się dołączyć do pokoju", Toast.LENGTH_SHORT).show()
                                }
                            break
                        }
                    }
                    if(succ == 0){
                        Toast.makeText(this, "Nie znaleziono pokoju", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}