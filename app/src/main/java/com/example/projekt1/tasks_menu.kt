package com.example.projekt1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.projekt1.databinding.ActivityJoinCreateRoomBinding
import com.example.projekt1.databinding.ActivityTasksMenuBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class tasks_menu : AppCompatActivity() {
    private lateinit var binding: ActivityTasksMenuBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var actionBar: ActionBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTasksMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        actionBar.title = "Menu Zadań"

        firebaseAuth = FirebaseAuth.getInstance()

        binding.guzikBack.setOnClickListener {
            startActivity(Intent(this,Profile::class.java))
            finish()
        }
        binding.guzikZadania.setOnClickListener {
            startActivity(Intent(this,mytasks::class.java))
            finish()
        }
        binding.guzikZadaniaDone.setOnClickListener{
            startActivity(Intent(this,taskdone::class.java))
            finish()
        }

        binding.guzikZadaniaDoOceny.setOnClickListener{
            startActivity(Intent(this,otherstasks::class.java))
            finish()
        }
    }

}