package com.example.projekt1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBar
//import com.example.projekt1.databinding.ActivityOthersTasksViewBinding
import com.example.projekt1.databinding.ActivityOtherstasksViewBinding
//import com.example.projekt1.databinding.ActivityTaksdoneViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore

class otherstasks_view : AppCompatActivity(){
    private lateinit var binding: ActivityOtherstasksViewBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var actionBar: ActionBar
    private lateinit var databaseReference: DatabaseReference

    private var mail = ""
    private var komentarzwykonawcy = ""
    private var wykonawca = ""
    private var creator = ""
    private var taskidrate = "" //id task w kolekcji toRate //roomid
    private var doctaskid = "" //id task w kolekcji tasks w rooms //docid2


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otherstasks_view)
        binding = ActivityOtherstasksViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        //sprawdzenie czy użytkownik jest zalogowany
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){
            mail= firebaseUser.email.toString()
            komentarzwykonawcy = intent.getStringExtra("komentarzwykonawcy").toString()
            wykonawca = intent.getStringExtra("wykonawca").toString()
            creator = intent.getStringExtra("creator").toString()
            taskidrate = intent.getStringExtra("taskid").toString()
            doctaskid = intent.getStringExtra("doctaskid").toString()

            TasksList()
        }else{
            startActivity(Intent(this,Login::class.java))
            finish()
        }
        binding.guzikBack.setOnClickListener {
            //powrót do profilu
            startActivity(Intent(this, otherstasks::class.java))
        }
        binding.guzikOcen.setOnClickListener {
            //powrót do profilu
            startActivity(Intent(this, GivePoints::class.java))
        }
    }

    fun TasksList() {
        val db = FirebaseFirestore.getInstance()
        val result: StringBuffer = StringBuffer()
        db.collection("Rooms").document(taskidrate).collection("Tasks").get().addOnCompleteListener {
            val result: StringBuffer = StringBuffer()
            if (it.isSuccessful) {
                for (doc in it.result!!) {
                    if (doc.id == doctaskid){
                        val guzik = Button(this)
                        guzik.width = 50
                        guzik.height = 50
                        guzik.text = doc.data.get("tresc").toString()
                        binding.TaskName.text = doc.data.get("tresc").toString()
                        binding.Creatorcomment.text = /*"Komentarz: " +*/ doc.data.get("komentarz").toString() //komentarz tworcy zdania przy tworzeniu go
                        binding.deadline.text = /*"Deadline: " + */doc.data.get("deadline").toString()
                        binding.maxpoints.text = /*"Max punktów do zdobycia: " +*/doc.data.get("maxpunkty").toString()
                        binding.wykonawca.text = /*"Wykonawca: " +*/ wykonawca
                        binding.Wykonawcacomment.text = komentarzwykonawcy
                        break
                    }
                }
            }
        }
    }
}