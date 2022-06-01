package com.example.projekt1

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt1.databinding.ActivityCreateTaskBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

//import com.google.firebase.ktx.Firebase

class Creattask : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTaskBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var actionBar: ActionBar
    private lateinit var databaseReference: DatabaseReference

    private var mail =""
    private var docId=""
    private var useremail=""
    var userarray =ArrayList<String>()
    var emailarray =ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance()
        //sprawdzenie czy użytkownik jest zalogowany
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){
            mail= firebaseUser.email.toString()
            docId = intent.getStringExtra("id").toString()
            userarray= intent.getStringArrayListExtra("userarray") as ArrayList<String>
            emailarray= intent.getStringArrayListExtra("emailarray") as ArrayList<String>
        }else{
            startActivity(Intent(this,Login::class.java))
            finish()
        }

        binding.guzikCreateTask.setOnClickListener{
            //powrót do profilu
            startActivity(Intent(this, Creattask::class.java))
        }

        binding.who.text = "dla " + emailarray.joinToString(separator = ",")

        //kalendarz
        val c= Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minutes = c.get(Calendar.MINUTE)

        binding.taskdate.setOnClickListener{
            val dpd = DatePickerDialog(this,DatePickerDialog.OnDateSetListener{view, mYear, mMonth, mDay ->
                binding.taskdate.setText(""+ mDay + "/" + mMonth + "/" + mYear)
            },year,month,day)
            dpd.show()
        }

        binding.tasktime.setOnClickListener{
            TimePickerDialog(this,TimePickerDialog.OnTimeSetListener { view, mhour, mminutes ->
                binding.tasktime.setText("$mhour:$mminutes")
            }, hour,minutes, false ).show()
        }

        binding.guzikBack.setOnClickListener{
            startActivity(Intent(this,Profile::class.java))
            finish()
        }
        binding.guzikCreateTask.setOnClickListener {
                    var userid = userarray
                    var email = emailarray
                    val db = FirebaseFirestore.getInstance()
                    db.collection("Rooms").document(docId).collection("Tasks")
                        .get()
                        .addOnCompleteListener {
                            val task: MutableMap<String, Any> = HashMap() //stworzenie nowego dokumentu
                            task["creator"] = mail
                            task["email"] = email
                            task["maxpunkty"] = binding.taskpunkty.text.toString()
                            task["komentarz"] = binding.tasktresc.text.toString()
                            task["tresc"] = binding.taskName.text.toString()
                            task["deadline"] = binding.taskdate.text.toString() + " " + binding.tasktime.text.toString()
                            task["room"] = docId.toString()
                            task["dlaKogo"] = userarray
                            db.collection("Rooms").document(docId).collection("Tasks").add(task)
                        }

                Toast.makeText(this,"Dodano zadanie", Toast.LENGTH_SHORT).show()



            startActivity(Intent(this,Profile::class.java))
            finish()
        }



    }
}
