package com.example.projekt1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.projekt1.databinding.ActivityMytasksBinding
import com.example.projekt1.databinding.ActivityShowMyRoomsBinding
import com.example.projekt1.databinding.ActivityTasksViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore

class mytasks: AppCompatActivity() {
    private lateinit var binding: ActivityMytasksBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var actionBar: ActionBar
    private lateinit var databaseReference: DatabaseReference

    private var mail =""
    private var docId =""
    private var docId2=""
    private var userid=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mytasks)
        binding = ActivityMytasksBinding.inflate(layoutInflater)
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
            startActivity(Intent(this, ShowMyRooms::class.java))
        }
    }
    fun TasksList(){
        val db = FirebaseFirestore.getInstance()
        db.collection("Users").get().addOnCompleteListener {
            for( doc in it.result!!){
                userid = doc.id
                if (doc.get("email").toString() == firebaseAuth.currentUser?.email.toString()){
                    val result: StringBuffer = StringBuffer()
                    db.collection("Users").document(userid).collection("Tasks")
                        .get()
                        .addOnCompleteListener { task2->
                            if(task2.isSuccessful)
                                for(doc2 in task2.result!!){
                                    val roomid = doc2.data.get("room")
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
                                }

                        }
                    break
                }
            }
        }

    }
}


