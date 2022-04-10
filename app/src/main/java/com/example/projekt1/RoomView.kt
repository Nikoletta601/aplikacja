package com.example.projekt1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.toColorInt
import androidx.core.view.marginLeft
import com.example.projekt1.databinding.ActivityLoginBinding
import com.example.projekt1.databinding.ActivityRoomViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.graphics.Color

class RoomView : AppCompatActivity() {
    private lateinit var binding:ActivityRoomViewBinding
    private lateinit var firebaseAuth: FirebaseAuth
    var searchID=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_view)
        binding = ActivityRoomViewBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val roomid=intent.getStringExtra("id") //pobranie przeslanej wczesniej wartosci
        val db = FirebaseFirestore.getInstance()
        binding.guzikBack.setOnClickListener{
            //powrót do profilu
            startActivity(Intent(this, Profile::class.java))
        }

        binding.guzikUsers.setOnClickListener{
            users_list() //wyswietlenie listy
        }

        binding.guzikNotifications.setOnClickListener{
            chat() //wyswietlenie listy
        }

        db.collection("Rooms") // wejscie do kolekcji rooms
            .get()
            .addOnCompleteListener {
                val result: StringBuffer = StringBuffer()
                if (it.isSuccessful) {
                    for (document in it.result!!) { // petla przechodzaca przez wszystkie dokumenty kolekcji
                        val id = document.data.get("ID") //pobranie ID pokoju
                        val room = document.data.get("roomname") //pobranie nazwy
                        var roomname = room.toString()
                        if (id == roomid) { // jesli id takie same jak szukane
                            binding.RoomName.text = roomname //wypisanie nazwy pokoju
                            searchID = document.id.toString()
                            chat()
                            break
                        }
                    }
                }
            }




    }

    private fun users_list(){
        clear_view()
        val db = FirebaseFirestore.getInstance()
        db.collection("Rooms").document(searchID).collection("Users") //wejscie do zakladki users w znalezionym pokoju
            .get()
            .addOnCompleteListener{
                val result: StringBuffer = StringBuffer()
                val textv3 = TextView(this) //stworzenie napisu
                textv3.textSize =30f //rozmiar
                textv3.text = "Użytkownicy:" // wypianie emaila
                textv3.setTextColor(Color.parseColor("#000000"))
                binding.roomviewlayout.addView(textv3) //dodanie stworzonego napisu do aktywnosci
                if (it.isSuccessful) {
                    var i=0
                    for (document in it.result!!) {
                        i=i+1
                        val textv = TextView(this) //stworzenie napisu
                        textv.textSize =18f //rozmiar
                        textv.text = i.toString() + ". " + document.data.get("email").toString() // wypianie emaila
                        binding.roomviewlayout.addView(textv) //dodanie stworzonego napisu do aktywnosci
                        val textv2 = TextView(this) //stworzenie napisu
                        textv2.textSize =18f //rozmiar
                        textv2.text = document.data.get("role").toString() // wypianie roli
                        textv2.setTextColor(Color.parseColor("#51C558"))
                        binding.roomviewlayout.addView(textv2)
                    }
                }
            }
    }

    private fun clear_view(){
        if (null != binding.roomviewlayout && binding.roomviewlayout.getChildCount() > 0) {
            try {
                binding.roomviewlayout.removeViews(0,binding.roomviewlayout.getChildCount())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun chat(){
        clear_view()
        val db = FirebaseFirestore.getInstance()
        db.collection("Rooms").document(searchID).collection("Chat") //wejscie do zakladki users w znalezionym pokoju
            .get()
            .addOnCompleteListener{
                val result: StringBuffer = StringBuffer()
                if (it.isSuccessful) {
                    var i=0
                    for (document in it.result!!) {
                        i=i+1
                        val textv = TextView(this) //stworzenie napisu
                        textv.textSize =15f //rozmiar
                        textv.text = document.data.get("text").toString() // wypianie emaila
                        binding.roomviewlayout.addView(textv)
                    }
                }
            }
    }
}