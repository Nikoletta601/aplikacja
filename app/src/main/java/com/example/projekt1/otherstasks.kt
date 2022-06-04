package com.example.projekt1
/*
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
import com.example.projekt1.databinding.ActivityothertasksBinding
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

class otherstasks : AppCompatActivity(){
    private lateinit var binding: ActivityothertasksBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var actionBar: ActionBar
    private lateinit var databaseReference: DatabaseReference

    private var mail = ""
    private var docId = ""
    private var docId2 = ""

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
            docId2 = intent.getStringExtra("id2").toString()

            val db = FirebaseFirestore.getInstance()
            db.collection("Users").get().addOnCompleteListener {
                for (doc in it.result!!) {
                    if (doc.data.get("email") == firebaseAuth.currentUser?.email.toString())
                        db.collection("Users").document(doc.id).collection("TasksDone").get()
                            .addOnCompleteListener { task ->
                                for (doc2 in task.result!!) {
                                    tasksdonearray.add(doc2.id)

                                }
                                TasksList()
                            }

                }
            }





        } else {
            startActivity(Intent(this, Login::class.java))
            //Raport()
            finish()
        }

        binding.guzikBack.setOnClickListener {
            //powrót do zadania
            startActivity(Intent(this, tasks_menu::class.java))
        }

        binding.guzikWykonane.setOnClickListener {
            Raport()
            Toast.makeText(this, "Zdano raport", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, mytasks::class.java))

        }
    }

        fun TasksList(){
            val db = FirebaseFirestore.getInstance()
            db.collection("Rooms").document(docId).collection("Tasks").get().addOnCompleteListener {
                for( doc in it.result!!){
                    val roomid = doc.data.get("room")
                    if(roomid == docId){
                        val guzik = Button(this)
                        guzik.width = 50
                        guzik.height = 50
                        guzik.text = doc.data.get("tresc").toString()
                        val docId2 = doc.id
                        guzik.setOnClickListener{
                            val intent = Intent(this,task::class.java)
                            intent.putExtra("id",docId)
                            intent.putExtra("id2",docId2)// wyslanie danych do pliku z intent
                            intent.putExtra("back", "1")
                            intent.putStringArrayListExtra("tasksdonearray", tasksdonearray)
                            startActivity(intent)
                            finish()
                        }
                        binding.roomviewlayout.addView(guzik)
                    }
                }

            }
        }

}

 */