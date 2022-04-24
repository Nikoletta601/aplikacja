package com.example.projekt1

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.projekt1.databinding.ActivityCreateRoomBinding
import com.example.projekt1.databinding.ActivityCreateTaskBinding
import com.example.projekt1.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class Creattask : AppCompatActivity()  {

    private lateinit var binding: ActivityCreateTaskBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    private var taskname = ""
    private var taskID = ""
    private var tekstask = ""
    private var punkty = ""
    private var mail =""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding= ActivityCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)


        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Proszę czekać")
        progressDialog.setMessage("Tworzenie...")
        progressDialog.setCanceledOnTouchOutside(false)


        binding.guzikCreateTask.setOnClickListener {
            //sprawdzenie czy użytkownik jest zalogowany
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null){
                mail= firebaseUser.email.toString()
            }else{
                startActivity(Intent(this,Login::class.java))
                finish()
            }

        binding.guzikBack.setOnClickListener{
            //powrót do profilu
            startActivity(Intent(this, ShowMyRooms::class.java))
        }
    }}}


