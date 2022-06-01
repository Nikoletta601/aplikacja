package com.example.projekt1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt1.databinding.ActivityMytasksBinding
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
            startActivity(Intent(this, Profile::class.java))
        }
    }


    fun TasksList(){
        val db = FirebaseFirestore.getInstance()
        db.collection("Rooms").get().addOnCompleteListener {
            for (doc in it.result!!) {
                val docId = doc.id
                db.collection("Rooms").document(docId).collection("Tasks").get()
                    .addOnCompleteListener { task ->
                        for (doc2 in task.result!!) {
                            var docsize = doc2.get("email") as ArrayList<String>
                            println(docsize)
                            for (i in docsize.indices) {
                                if (docsize[i].toString() == firebaseAuth.currentUser?.email.toString()) {
                                    val roomid = doc2.data.get("room")
                                    val guzik = Button(this)
                                    guzik.width = 50
                                    guzik.height = 50
                                    guzik.text = doc2.data.get("tresc").toString()
                                    val docId2 = doc2.id.toString()
                                    guzik.setOnClickListener {
                                        val intent =
                                            Intent(this, com.example.projekt1.task::class.java)
                                        intent.putExtra("id", docId)
                                        intent.putExtra("id2", docId2)// wyslanie danych do pliku z intent
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
}


