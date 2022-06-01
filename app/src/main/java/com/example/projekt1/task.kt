package com.example.projekt1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.projekt1.databinding.ActivityShowMyRoomsBinding
import com.example.projekt1.databinding.ActivityTaskBinding
import com.example.projekt1.databinding.ActivityTasksViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore

class task : AppCompatActivity() {
    private lateinit var binding: ActivityTaskBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var actionBar: ActionBar
    private lateinit var databaseReference: DatabaseReference

    private var mail =""//logged in user's mail from database
    private var docId2 =""//task id
    private var roomid = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        //sprawdzenie czy użytkownik jest zalogowany
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){
            mail= firebaseUser.email.toString()
            roomid = intent.getStringExtra("id").toString()
            docId2 = intent.getStringExtra("id2").toString()
            TasksList()
        }else{
            startActivity(Intent(this,Login::class.java))
            finish()
        }
        binding.guzikBack.setOnClickListener {
            //powrót do profilu
            startActivity(Intent(this, mytasks::class.java))
        }
        binding.guzikRaport.setOnClickListener {
            startActivity(Intent(this, Raport::class.java))
        }
    }
    fun TasksList() {
        val db = FirebaseFirestore.getInstance()
        val result: StringBuffer = StringBuffer()
        db.collection("Rooms").document(roomid).collection("Tasks").get().addOnCompleteListener {
            val result: StringBuffer = StringBuffer()
            if (it.isSuccessful) {
                for (doc in it.result!!) {
                    if (doc.id == docId2) {
                        val guzik = Button(this)
                        guzik.width = 50
                        guzik.height = 50
                        guzik.text = doc.data.get("tresc").toString()
                        binding.TaskName.text = doc.data.get("tresc").toString()
                        binding.comment.text = "Komentarz: " + doc.data.get("komentarz").toString()
                        binding.deadline.text =
                            "Termin wykonania: " + doc.data.get("deadline").toString()
                        binding.points.text = "Punkty do zdobycia: " + doc.data.get("maxpunkty").toString()
                        binding.creator.text =
                            "Autor zadania: " + doc.data.get("creator").toString()
                        break
                    }
                }
            }
        }
    }
}


