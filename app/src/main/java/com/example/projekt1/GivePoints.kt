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

    private var mail =""
    private var docId =""
    private var docId2 =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.guzikGivePoints.setOnClickListener {
            //sprawdzenie czy użytkownik jest zalogowany
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null){
                mail= firebaseUser.email.toString()
            }else{
                startActivity(Intent(this,Login::class.java))
                finish()
            }
            Points()

        }

        binding.guzikBack.setOnClickListener{
            //powrot do profilu
            startActivity(Intent(this, ShowMyRooms::class.java))
        }

        binding.guzikRaport.setOnClickListener{
            val intent = Intent(this,task::class.java)
            intent.putExtra("id",docId)
            intent.putExtra("id2",docId2)// wyslanie danych do pliku z intent
            startActivity(intent)
            finish()
        }
    }

    fun Points(){
        val db = FirebaseFirestore.getInstance()
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
                                    points=doc2.data.get("maxpunkty").toString()
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

            }

    }

}