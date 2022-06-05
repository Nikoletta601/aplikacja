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
import com.example.projekt1.databinding.ActivityCreateRoomBinding
import com.example.projekt1.databinding.ActivityGivePointsBinding
import com.example.projekt1.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

class GivePoints : AppCompatActivity() {

    private lateinit var binding: ActivityGivePointsBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    private var mail = ""
    private var docId = ""
    private var docId2 = ""
    private var userid = ""
    private var wykonawca = ""
    private var roomId = ""
    private var maxpoints= ""
    private var taskId= ""
    private var datawykonania= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_give_points)
        binding = ActivityGivePointsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        //binding.guzikGivePoints.setOnClickListener {
        //sprawdzenie czy użytkownik jest zalogowany
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            mail = firebaseUser.email.toString()
            wykonawca = intent.getStringExtra("wykonawca").toString()
            roomId = intent.getStringExtra("roomid").toString()
            maxpoints= intent.getStringExtra("maxpunkty").toString()
            taskId= intent.getStringExtra("taskid").toString()
            datawykonania=intent.getStringExtra("datawykonania").toString()

            binding.maxpunkty.text="/ "+ maxpoints
        } else {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
        //Points()

        //}

        binding.guzikBack.setOnClickListener {
            //powrot do profilu
            startActivity(Intent(this, ShowMyRooms::class.java))
        }

        binding.guzikOcen.setOnClickListener {
            Points()
            Toast.makeText(this, "Oceniono", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, tasks_menu::class.java))
        }
    }

    fun Points() {
        val db = FirebaseFirestore.getInstance()
        val result: StringBuffer = StringBuffer()
        /*db.collection("Users").get().addOnCompleteListener {
            for (doc in it.result!!) {
                if (doc.get("email").toString() == mail.toString()) {
                    userid = doc.id
                }
*/
                db.collection("Users").document(wykonawca).collection("TaskDone").get()
                    .addOnCompleteListener {
                        val task: MutableMap<String, Any> =
                            HashMap()//stworzenie nowego dokumentu
                        task["komentarzDoOceny"] = binding.commentdooceny.text.toString()
                        task["punkty"] = binding.punkty.text.toString()
                        //task["wykonawca"] = userid.toString()
                        //task["creator"] = creator
                        task["roomid"] = roomId
                        task["taskid"] = taskId
                        task["dataWykonania"] = datawykonania
                        val currentTime = Calendar.getInstance().time
                        task["dataOceny"] = currentTime

                        db.collection("Users").document(wykonawca).collection("TaskDone")
                            .add(task)

                    }
                //break
            }
        //}
    //}
}
                /*
                db.collection("Rooms").document(docId).collection("Tasks")
                    .get()
                    .addOnCompleteListener { points->
                        if(points.isSuccessful)
                            for(doc2 in points.result!!){
                                if(doc2.id == docId2){

                                    val email = doc2.data.get("email")
                                    if(email == mail){

                                        //val points= doc2.data.get("punkty")

                                        binding.guzikDone.setOnClickListener{
                                            binding.points.text = "Punkty: "+doc2.data.get("maxpunkty").toString() + "/" + doc2.data.get("maxpunkty").toString()
                                            //points=doc2.data.get("maxpunkty").toString()
                                        }

                                        val guzik = Button(this)
                                        guzik.width = 50
                                        guzik.height = 50
                                        guzik.text = doc2.data.get("tresc").toString()
                                        binding.TaskName.text = doc2.data.get("tresc").toString()
                                        binding.deadline.text = "Termin wykonania: "+doc2.data.get("deadline").toString()
                                        binding.points.text = "Punkty: "+doc2.data.get("punkty").toString() + "/" + doc2.data.get("maxpunkty").toString()

                                        break
                                    }
                                }

                            }

                    }*/



