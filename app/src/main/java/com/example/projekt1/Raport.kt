package com.example.projekt1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt1.databinding.ActivityRaportBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.MutableMap
import kotlin.collections.set

class Raport : AppCompatActivity() {
    private lateinit var binding: ActivityRaportBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var actionBar: ActionBar
    private lateinit var databaseReference: DatabaseReference

    private var mail = ""
    private var docId = ""
    private var docId2 = ""
    private var userId = ""
    private var creator = ""
    private var creatorid = ""
    private var taskId = ""
    private var roomid = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_raport)
        binding = ActivityRaportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        //sprawdzenie czy użytkownik jest zalogowany
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            mail = firebaseUser.email.toString()
            docId = intent.getStringExtra("id").toString()
            taskId = intent.getStringExtra("taskId").toString()
            docId2 = intent.getStringExtra("id2").toString()
            creator = intent.getStringExtra("creator").toString()
            roomid = intent.getStringExtra("roomid").toString()

            val db = FirebaseFirestore.getInstance()
            val result: StringBuffer = StringBuffer()
            db.collection("Users").get().addOnCompleteListener {
                for (doc in it.result!!) {
                    //if (doc.data.get("email").toString() == firebaseAuth.currentUser?.email.toString()){
                    if (doc.get("email").toString() == mail.toString()) {
                        userId = doc.id
                    }
                }
            }
                //Raport()
            } else {
                startActivity(Intent(this, Login::class.java))
                finish()
            }

            binding.guzikBack.setOnClickListener {
                //powrót do zadania
                startActivity(Intent(this, task::class.java))
            }

            binding.guzikWykonane.setOnClickListener {
                Raport()
                Toast.makeText(this, "Zdano raport", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, mytasks::class.java))

            }

        }

        fun Raport() {
            val db = FirebaseFirestore.getInstance()
            val result: StringBuffer = StringBuffer()

            db.collection("Users").get().addOnCompleteListener {
                for (doc in it.result!!) {
                    /* //if (doc.data.get("email").toString() == firebaseAuth.currentUser?.email.toString()){
                        if (doc.get("email") .toString() == mail.toString()){
                            userId = doc.id
                        }
*/
                    if (doc.get("email").toString() == creator) {
                        creatorid = doc.id
                        db.collection("Users").document(creatorid).collection("Torate")
                            .get()
                            .addOnCompleteListener {
                                val task: MutableMap<String, Any> =
                                    HashMap()//stworzenie nowego dokumentu
                                task["komentarz"] = binding.commentwykonawcy.text.toString()
                                task["taskid"] = taskId.toString()
                                task["wykonawca"] = userId.toString()
                                task["creator"] = creator
                                task["roomid"] = roomid
                                val currentTime = Calendar.getInstance().time
                                task["dataoddania"] = currentTime

                                db.collection("Users").document(creatorid).collection("Torate")
                                    .add(task)
                            }

                        break
                    }
                }
            }


    }
}
