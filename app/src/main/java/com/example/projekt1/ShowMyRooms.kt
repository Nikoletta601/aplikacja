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
import com.example.projekt1.databinding.ActivityShowMyRoomsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.ktx.Firebase



class ShowMyRooms : AppCompatActivity() {
    private lateinit var binding: ActivityShowMyRoomsBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var actionBar: ActionBar
    private lateinit var databaseReference: DatabaseReference

    private var mail =""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityShowMyRoomsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        actionBar.title = "Moje pokoje"

        firebaseAuth = FirebaseAuth.getInstance()
        //sprawdzenie czy użytkownik jest zalogowany
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){
            mail= firebaseUser.email.toString()
        }else{
            startActivity(Intent(this,Login::class.java))
            finish()
        }
        showRooms()
        binding.guzikPokojX.setOnClickListener {

        }

        binding.guzikBack2.setOnClickListener{
            //powrót do profilu
            startActivity(Intent(this, Profile::class.java))
        }


    }
    fun showRooms(){
        val db = FirebaseFirestore.getInstance()
        db.collection("Users")
            .get()
            .addOnCompleteListener{
                val result: StringBuffer = StringBuffer()
                if(it.isSuccessful){

                    for (document in it.result!!) {
                        val email = document.data.get("email")
                        if (mail == email) {
                            val docId = document.id
                            db.collection("Users").document(docId).collection("Rooms")
                                .get()
                                .addOnCompleteListener{task->
                                    var i=0
                                    for (doc in task.result!!){
                                        i=i+1
                                        val roomId = doc.data.get("ID")
                                        val roomname = doc.data.get("roomname")
                                        binding.roomIDTV.text = roomname.toString()
                                        Toast.makeText(this, i.toString() + roomname.toString(), Toast.LENGTH_SHORT).show()
                                    }
                                }
                            break
                        }
                    }
                }

            }

    }
}
