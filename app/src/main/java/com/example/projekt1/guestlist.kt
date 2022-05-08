package com.example.projekt1

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.projekt1.databinding.ActivityGuestlistBinding
import com.example.projekt1.databinding.ActivityShowMyRoomsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.ktx.Firebase



class guestlist : AppCompatActivity() {
    private lateinit var binding: ActivityGuestlistBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var actionBar: ActionBar
    private lateinit var databaseReference: DatabaseReference

    private var mail =""
    private var docId=""



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityGuestlistBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance()
        //sprawdzenie czy użytkownik jest zalogowany
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){
            mail= firebaseUser.email.toString()
            docId = intent.getStringExtra("id").toString()
            UsersList()
        }else{
            startActivity(Intent(this,Login::class.java))
            finish()
        }

        binding.guzikBack.setOnClickListener{
            startActivity(Intent(this,Profile::class.java))
            finish()
        }

    }

    fun UsersList(){
        val db = FirebaseFirestore.getInstance()
        db.collection("Rooms").document(docId).collection("Users")
            .get()
            .addOnCompleteListener {
                val result: StringBuffer = StringBuffer()
                if (it.isSuccessful)
                    for (document in it.result!!) {
                        val email = document.data.get("email")
                        val guzik = Button(this)
                        guzik.width = 50
                        guzik.height = 50
                        guzik.text = email.toString()
                        guzik.setBackgroundColor(Color.WHITE)
                        guzik.setOnClickListener{
                            guzik.setBackgroundColor(Color.GREEN)
                            val intent = Intent(this,Creattask::class.java)
                            intent.putExtra("id",docId)
                            intent.putExtra("user",email.toString())
                            Toast.makeText(this,email.toString(), Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                                        }
                        binding.guestlistlayout.addView(guzik)
                                    }

                            }
    }

}