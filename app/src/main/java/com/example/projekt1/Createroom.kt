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
import com.example.projekt1.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class Createroom : AppCompatActivity() {

    private lateinit var binding: ActivityCreateRoomBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    private var roomname = ""
    private var roomID = ""
    private var mail =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCreateRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)


        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Proszę czekać")
        progressDialog.setMessage("Tworzenie pokoju...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.guzikCreateRoom.setOnClickListener {
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null){
                mail= firebaseUser.email.toString()
            }

            roomname = binding.roomNameEt.text.toString().trim()
            roomID = getRandomString(5)
            saveFirestore(roomname,mail)

            startActivity(Intent(this, Profile::class.java))
        }

        binding.guzikBack.setOnClickListener{
            startActivity(Intent(this, Profile::class.java))
        }
    }

    fun saveFirestore(roomname: String, mail: String){
        val db = FirebaseFirestore.getInstance()
        val room: MutableMap<String, Any> = HashMap()
        room["roomname"] = roomname
        room["creator"] = mail
        room["ID"] = roomID
        db.collection("Rooms")
            .add(room)
            .addOnSuccessListener {
                Toast.makeText(this, "Utworzono pokój", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this, "Nie udało się utworzyć pokoju", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

}