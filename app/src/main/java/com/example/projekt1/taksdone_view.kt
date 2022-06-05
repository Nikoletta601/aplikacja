package com.example.projekt1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.projekt1.databinding.ActivityShowMyRoomsBinding
import com.example.projekt1.databinding.ActivityTaksdoneViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore

class taskdone_view : AppCompatActivity() {
    private lateinit var binding: ActivityTaksdoneViewBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var actionBar: ActionBar
    private lateinit var databaseReference: DatabaseReference

    private var mail =""//logged in user's mail from database
    private var docId2 =""//task id
    private var roomid = ""
    private var komentarz = ""
    private var punkty = ""
    private var dataOceny = ""
    private var dataWykonania = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        binding = ActivityTaksdoneViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        //sprawdzenie czy użytkownik jest zalogowany
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){
            mail= firebaseUser.email.toString()
            roomid = intent.getStringExtra("taskroomid").toString()
            docId2 = intent.getStringExtra("docid").toString()
            komentarz = intent.getStringExtra("komentarz").toString()
            punkty = intent.getStringExtra("punkty").toString()
            dataWykonania=intent.getStringExtra("dataWykonania").toString()
            dataOceny=intent.getStringExtra("dataOceny").toString()

            binding.dataWykonania.text="Oddano: "+dataWykonania
            binding.dataOceny.text="Oceniono: "+dataOceny

            TasksList()
        }else{
            startActivity(Intent(this,Login::class.java))
            finish()
        }
        binding.guzikBack.setOnClickListener {
            //powrót do profilu
            startActivity(Intent(this, taskdone::class.java))
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
                        binding.points.text = "Punkty: " + punkty + "/" +doc.data.get("maxpunkty")
                        binding.creator.text =
                            "Autor zadania: " + doc.data.get("creator").toString()
                        binding.komentarz.text = "Komentarz: " + komentarz
                        break
                    }
                }
            }
        }
    }
}


