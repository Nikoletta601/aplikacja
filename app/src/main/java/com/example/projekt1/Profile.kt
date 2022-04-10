package com.example.projekt1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.projekt1.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Profile : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var actionBar: ActionBar
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        actionBar.title = "Profil"

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        binding.guzikWyloguj.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this,Login::class.java))
            finish()
        }
        binding.guzikShowMyRooms.setOnClickListener {
            startActivity(Intent(this,ShowMyRooms::class.java))
            finish()
        }
        binding.guzikCreateRoom.setOnClickListener {
            startActivity(Intent(this,Createroom::class.java))
            finish()
        }
        binding.guzikJoinRoom.setOnClickListener {
            startActivity(Intent(this,JoinRoom::class.java))
            finish()
        }
        binding.guzikroom.setOnClickListener {
            val intent = Intent(this,RoomView::class.java)
            intent.putExtra("id","FTMm4") // wyslanie danych do pliku z intent
            startActivity(intent)
            finish()
        }

    }
    private fun checkUser(){
        val uid= firebaseAuth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){//wypisanie maila jesli jest sie zalogowanym
                val email= firebaseUser.email
                binding.emailTV.text = email
        }
        else{ //wylogowanie jesli nie jest sie zalogowanym
            startActivity(Intent(this,Login::class.java))
            finish()
        }
    }

}