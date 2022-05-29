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

    private var mail =""
    private var docId =""
    private var docId2 =""
    private var userid = ""
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
            docId = intent.getStringExtra("id").toString()
            docId2 = intent.getStringExtra("id2").toString()
            TasksList()
        }else{
            startActivity(Intent(this,Login::class.java))
            finish()
        }
        binding.guzikBack.setOnClickListener {
            //powrót do profilu
            startActivity(Intent(this, ShowMyRooms::class.java))
        }
        binding.guzikRaport.setOnClickListener {
            startActivity(Intent(this, Raport::class.java))
        }
    }
    fun TasksList(){
        val db = FirebaseFirestore.getInstance()
        val result: StringBuffer = StringBuffer()
        db.collection("Users").get().addOnCompleteListener {
            for( doc in it.result!!){
                userid = doc.id
                if (doc.get("email").toString() == mail.toString()){
                    db.collection("Users").document(userid).collection("Tasks")
                        .get()
                        .addOnCompleteListener { task->
                            if(task.isSuccessful)
                                for(doc2 in task.result!!){
                                    if(doc2.id == docId2){
                                        val email = doc2.data.get("email")
                                        if(email == mail){
                                            val guzik = Button(this)
                                            guzik.width = 50
                                            guzik.height = 50
                                            guzik.text = doc2.data.get("tresc").toString()
                                            binding.TaskName.text = doc2.data.get("tresc").toString()
                                            binding.comment.text = "Komentarz: " + doc2.data.get("komentarz").toString()
                                            binding.deadline.text = "Termin wykonania: "+doc2.data.get("deadline").toString()
                                            binding.points.text = "Punkty: "+doc2.data.get("punkty").toString() + "/" + doc2.data.get("maxpunkty").toString()
                                            binding.creator.text = "Autor zadania: "+doc2.data.get("creator").toString()
                                            val taskdone = doc2.data.get("wykonane")
                                            if(taskdone.toString() == "0"){
                                                binding.isdone.text = "Nie wykonane"
                                            }else{
                                                binding.isdone.text = "Wykonane"
                                                //binding.isdone.setTextColor(0x37FF00)
                                            }
                                            break
                                        }
                                    }

                                }

                        }
                    break
                }
            }
        }

    }
}


