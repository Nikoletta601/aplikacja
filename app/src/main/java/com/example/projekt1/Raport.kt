package com.example.projekt1

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.core.view.get
import com.example.projekt1.databinding.ActivityRaportBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

class Raport : AppCompatActivity() {
    private lateinit var binding: ActivityRaportBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var actionBar: ActionBar
    private lateinit var databaseReference: DatabaseReference

    private var mail = ""
    private var docId = ""
    private var docId2 = ""
    private var userid = ""
    private var creator = ""
    private var taskId = ""

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
            //Raport()
        } else {
            startActivity(Intent(this, Login::class.java))
            //Raport()
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
/*
        db.collection("Users").document(userid).collection("Tasks")
            .get()
            .addOnCompleteListener() { task -> //TODO prawdopodobnie addOnCompleteListener nic nie zwraca
                if (task.isSuccessful)
                    for (doc2 in task.result!!) {
                        if (doc2.id == docId2) {
                            val email = doc2.data.get("email")
                            if (email == mail) {
                                //val creatortask = doc2.get("creator")
                                creator = doc2.get("creator").toString()
                            }
                            break
                        }

                    }
*/
                db.collection("Users").get().addOnCompleteListener {
                    for (doc in it.result!!) {
                        userid = doc.id

                        if (doc.get("email")
                                .toString() == mail.toString()
                        ) {
                            db.collection("Users").document(userid).collection("Torate")
                                .get()
                                .addOnCompleteListener {
                                    val task: MutableMap<String, Any> =
                                        HashMap()//stworzenie nowego dokumentu
                                    task["komentarz"] = binding.commentwykonawcy.text.toString()
                                    task["taskid"] = taskId.toString()
                                    task["wykonawca"] = userid.toString()
                                    //task["creator"] = creator

                                    db.collection("Users").document(userid).collection("Torate")
                                        .add(task)
                                }

                            break
                        }
                    }
                }
            }
    }
/*
 fun Zmiendane(){
     val db = FirebaseFirestore.getInstance()
     val result: StringBuffer = StringBuffer()
     db.collection("Rooms").get().addOnCompleteListener {
         for (doc in it.result!!) {
             userid = doc.id

             if (doc.get("email")
                     .toString() == mail.toString()
             ) {
                 db.collection("Users").document(userid).collection("Tasks")
         .get()
         .addOnCompleteListener() { task -> //TODO prawdopodobnie addOnCompleteListener nic nie zwraca
             if (task.isSuccessful)
                 for (doc2 in task.result!!) {
                     if (doc2.id == docId2) {
                         val email = doc2.data.get("email")
                         if (email == mail) {
                             val creator = binding.creator.text.toString()
                             val tekst = binding.commentwykonawcy.text.toString()
                             val komentarz = hashMapOf(
                                 "komentarzW" to binding.commentwykonawcy.text.toString()
                             )
                             db.collection("Users").document(userid).collection("Tasks")
                                 .document(docId2).set(komentarz)
                         }
                         break
                     }

                 }
         }
 }
/*
                    db.collection("Users").document(userid).collection("Tasks")
                        .get()
                        .addOnCompleteListener() { task -> //TODO prawdopodobnie addOnCompleteListener nic nie zwraca
                            if (task.isSuccessful)
                                for (doc2 in task.result!!) {
                                    if (doc2.id == docId2) {
                                        val email = doc2.data.get("email")
                                        if (email == mail) {
                                        val creator = binding.creator.text.toString()
                                            val tekst = binding.commentwykonawcy.text.toString()
                                            val komentarz = hashMapOf(
                                                "komentarzW" to binding.commentwykonawcy.text.toString()
                                            )
                                            db.collection("Users").document(userid).collection("Tasks").document(docId2).set(komentarz)
                                        }
                                        break
                                    }

                                }
                        }*/


//                                            db.collection("Users").document(userid).collection("Tasks").document(docId2).get("commentWykonawcy").setValue()


//var commentW=binding.commentWykonawcy.text.toString()
//                                            db.collection("Users").document(userid).collection("Tasks")
//                                                .get()
//                                                .addOnCompleteListener{
//                                                    val task = task()
//                                                    val taskValues = task.toMap()
//                                                    val task: Map<String, String>
//
//
//                                                }
//                                                    task["komentarzW"]=binding.commentWykonawcy.text.toString()

//doc2
//.add(task["komentarzW"]=binding.commentWykonawcy.text.toString())

//var newPostKey = db.collection("Users").document(userid).collection("Tasks").ref()
//binding.commentwykonawcy.text = "Komentarz wykonawcy: " + doc2.data.get("komentarzW").toString()


/*
    fun Update(e){
        val db=FirebaseFirestore.getInstance()
        val task=db.collection("Users").document(userid).collection("Tasks").document(docId2)
        task.update({komentarzW: e.target.value})

    }
*/


/*
    fun Update(e){
        val db=FirebaseFirestore.getInstance()
        val task=db.collection("Users").document(userid).collection("Tasks").document(docId2)
        task.update({komentarzW: e.target.value})

    }
*/