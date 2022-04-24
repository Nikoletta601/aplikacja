package com.example.projekt1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.projekt1.databinding.ActivityShowMyRoomsBinding
import com.example.projekt1.databinding.ActivityTasksViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore

class tasks_view : AppCompatActivity() {
    private lateinit var binding: ActivityTasksViewBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var actionBar: ActionBar
    private lateinit var databaseReference: DatabaseReference

    private var mail =""
    private var docId =""
    private var docId2=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks_view)
        binding = ActivityTasksViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        //sprawdzenie czy użytkownik jest zalogowany
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){
            mail= firebaseUser.email.toString()
            docId = intent.getStringExtra("id2").toString()
            TasksList()
        }else{
            startActivity(Intent(this,Login::class.java))
            finish()
        }
    }
    fun TasksList(){
        val db = FirebaseFirestore.getInstance()
                val result: StringBuffer = StringBuffer()
                        db.collection("Rooms").document(docId).collection("Tasks")
                            .get()
                            .addOnCompleteListener { task2->
                                if(task2.isSuccessful)
                                    for(doc2 in task2.result!!){
                                        val email = doc2.data.get("email")
                                        if(email == mail){
                                            val guzik = Button(this)
                                            guzik.width = 50
                                            guzik.height = 50
                                            guzik.text = doc2.data.get("tresc").toString()
                                            val docId2 = doc2.id.toString()
                                            guzik.setOnClickListener{
                                                val intent = Intent(this,task::class.java)
                                                intent.putExtra("id",docId)
                                                intent.putExtra("id2",docId2)// wyslanie danych do pliku z intent
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


