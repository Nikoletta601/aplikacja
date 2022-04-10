package com.example.projekt1

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Button
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
            RoomList()
        }else{
            startActivity(Intent(this,Login::class.java))
            finish()
        }

        binding.guzikCreateRoom.setOnClickListener{
            startActivity(Intent(this,Createroom::class.java))
            finish() //wyswietlenie listy
        }
        binding.guzikJoinRoom.setOnClickListener{
            startActivity(Intent(this,JoinRoom::class.java))
            finish()
        }
        binding.guzikBack.setOnClickListener{
            startActivity(Intent(this,Profile::class.java))
            finish()
        }

    }

    fun RoomList(){
        val db = FirebaseFirestore.getInstance()
        db.collection("Rooms") // wejscie do kolekcji Rooms
            .get()
            .addOnCompleteListener {
                val result: StringBuffer = StringBuffer()
                if (it.isSuccessful)
                    for (document in it.result!!) {
                        val docId = document.id
                        db.collection("Rooms").document(docId).collection("Users")
                            .get()
                            .addOnCompleteListener { task->
                                if(task.isSuccessful)
                                    for(doc2 in task.result!!){
                                        val email = doc2.data.get("email")
                                        if(email == mail){
                                            val guzik = Button(this)
                                            guzik.width = 50
                                            guzik.height = 50
                                            guzik.text = document.data.get("roomname").toString()
                                            guzik.setOnClickListener{
                                                val intent = Intent(this,RoomView::class.java)
                                                intent.putExtra("id",document.data.get("ID").toString()) // wyslanie danych do pliku z intent
                                                startActivity(intent)
                                                finish()
                                            }
                                            binding.roomviewlayout.addView(guzik)
                                            break
                                        }
                                    }

                            }
                    }
            }
    }
}
