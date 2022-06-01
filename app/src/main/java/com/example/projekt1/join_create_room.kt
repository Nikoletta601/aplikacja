package com.example.projekt1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.projekt1.databinding.ActivityJoinCreateRoomBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class join_create_room : AppCompatActivity() {
    private lateinit var binding: ActivityJoinCreateRoomBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var actionBar: ActionBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinCreateRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        actionBar.title = "Profil"

        firebaseAuth = FirebaseAuth.getInstance()

        binding.guzikBack.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this,ShowMyRooms::class.java))
            finish()
        }
        binding.guzikJoinRoom.setOnClickListener {
            startActivity(Intent(this,JoinRoom::class.java))
            finish()
        }
        binding.guzikCreateRoom.setOnClickListener {
            startActivity(Intent(this,Createroom::class.java))
            finish()
        }

    }

}