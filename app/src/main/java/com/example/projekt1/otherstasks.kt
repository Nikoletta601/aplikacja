package com.example.projekt1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.core.view.get
import com.example.projekt1.databinding.ActivityRaportBinding
import com.example.projekt1.databinding.ActivityOtherstasksBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
//import java.util.*
import kotlin.collections.HashMap

class otherstasks: AppCompatActivity(){
    private lateinit var binding: ActivityOtherstasksBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var actionBar: ActionBar
    private lateinit var databaseReference: DatabaseReference

    private var mail = ""
    private var docId = ""
    private var docId2 = ""
    private var userid = ""
    private var roomid = ""
    private var taskid = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otherstasks)
        binding = ActivityOtherstasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        //sprawdzenie czy użytkownik jest zalogowany
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            mail = firebaseUser.email.toString()
            TasksList()

        } else {
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        binding.guzikBack.setOnClickListener {
            startActivity(Intent(this, tasks_menu::class.java))
        }

    }

    fun TasksList() {
        val db = FirebaseFirestore.getInstance()
        db.collection("Users").get().addOnCompleteListener {
            for (doc in it.result!!) {
                if (doc.data.get("email").toString() == firebaseAuth.currentUser?.email.toString()) {
                    userid = doc.id
                    //  val roomid = doc.data.get("room")
                    db.collection("Users").document(userid).collection("Torate").get().addOnCompleteListener { task ->
                        for (doc2 in task.result) {
                            //zrobic roomid
                            roomid = doc2.data.get("roomid").toString()
                            taskid = doc2.data.get("taskid").toString()
                            println(roomid)

                            db.collection("Rooms").document(roomid).collection("Tasks").get().addOnCompleteListener { task2 ->
                                for (doc3 in task2.result) {
                                    if (doc3.id == taskid) {
                                        val guzik = Button(this)
                                        guzik.width = 50
                                        guzik.height = 50
                                        guzik.text = doc3.data.get("tresc").toString()

                                        var komentarzwykonawcy = doc2.data.get("komentarz").toString()
                                        var taskid = doc2.data.get("taskid").toString()
                                        var wykonawca = doc2.data.get("wykonawca").toString()
                                        var creator = doc2.data.get("creator").toString()
                                        var doctaskid = doc3.id
                                        var roomId = doc2.data.get("roomid").toString()
                                        var dataoddania = doc2.data.get("dataoddania").toString()

                                        guzik.setOnClickListener {
                                            val intent =
                                                Intent(this, com.example.projekt1.otherstasks_view::class.java)
                                            intent.putExtra("komentarzwykonawcy", komentarzwykonawcy)
                                            intent.putExtra("wykonawca", wykonawca)// wyslanie danych do pliku z intent
                                            intent.putExtra("creator", creator)
                                            intent.putExtra("taskid", taskid)
                                            intent.putExtra("roomid", roomId)
                                            intent.putExtra("doctaskid", doctaskid)
                                            intent.putExtra("dataoddania", dataoddania)

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

