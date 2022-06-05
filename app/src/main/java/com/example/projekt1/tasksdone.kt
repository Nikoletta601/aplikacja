package com.example.projekt1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt1.databinding.ActivityTasksdoneBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore


class taskdone: AppCompatActivity() {
    private lateinit var binding: ActivityTasksdoneBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var actionBar: ActionBar
    private lateinit var databaseReference: DatabaseReference

    private var mail= ""
    private var userid= ""
    private var roomid= ""
    private var taskid= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mytasks)
        binding = ActivityTasksdoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        //sprawdzenie czy użytkownik jest zalogowany
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){
            mail= firebaseUser.email.toString()
            TasksList()
        }else{
            startActivity(Intent(this,Login::class.java))
            finish()
        }
        binding.guzikBack.setOnClickListener{
            //powrót do profilu
            startActivity(Intent(this, tasks_menu::class.java))
        }
    }


    fun TasksList(){
        val db = FirebaseFirestore.getInstance()
        db.collection("Users").get().addOnCompleteListener {
            for (doc in it.result!!) {
                if(doc.data.get("email").toString() == firebaseAuth.currentUser?.email.toString()) {
                    userid = doc.id

                    db.collection("Users").document(userid).collection("TaskDone").get().addOnCompleteListener { task->

                        for(doc2 in task.result) {
                            roomid = doc2.data.get("roomid").toString()
                            taskid = doc2.data.get("taskid").toString()
                            println(roomid)
                            db.collection("Rooms").document(roomid).collection("Tasks").get().addOnCompleteListener { task2->
                                for(doc3 in task2.result) {
                                        if(doc3.id == taskid) {
                                            val guzik = Button(this)
                                            guzik.width = 50
                                            guzik.height = 50
                                            guzik.text = doc3.data.get("tresc").toString()

                                            var komentarz = doc2.data.get("komentarzDoOceny").toString()
                                            var punkty = doc2.data.get("punkty").toString()
                                            var taskroomid = doc2.data.get("roomid").toString()
                                            var docid = doc3.id
                                            guzik.setOnClickListener {
                                                val intent =
                                                    Intent(this, com.example.projekt1.taskdone_view::class.java)
                                                intent.putExtra("docid", docid)  //taskid
                                                intent.putExtra("komentarz", komentarz)// wyslanie danych do pliku z intent
                                                intent.putExtra("punkty", punkty)
                                                intent.putExtra("taskroomid", taskroomid)

                                                startActivity(intent)
                                                finish()
                                            }
                                            binding.roomviewlayout.addView(guzik)
                                        }
                                }

                            }

                        }
                    }
                }
            }
        }
    }
}


