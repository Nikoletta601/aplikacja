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
import android.widget.Button
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class RoomView : AppCompatActivity() {
    private lateinit var binding: ActivityRoomViewBinding
    private lateinit var firebaseAuth: FirebaseAuth
    var searchID = ""
    private var klucz = ""
    private var mail =""
    private var chatcount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_view)
        binding = ActivityRoomViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser
        //sprawdzenie czy uzytkownik jest zalogowany
        if (firebaseUser != null){
            mail= firebaseUser.email.toString()
        }else{
            startActivity(Intent(this,Login::class.java))
            finish()
        }

        val roomid = intent.getStringExtra("id") //pobranie przeslanej wczesniej wartosci
        val db = FirebaseFirestore.getInstance()
        binding.guzikBack.setOnClickListener {
            //powrót do profilu
            startActivity(Intent(this, ShowMyRooms::class.java))
        }

        binding.guzikUsers.setOnClickListener {
            users_list() //wyswietlenie listy
        }

        binding.guzikNotifications.setOnClickListener {
            chat() //wyswietlenie czatu
        }
        binding.guzikSettings.setOnClickListener {
            settings() //wyswietlenie ustawien
        }
        binding.guzikTasks.setOnClickListener {
            tasks() //wyswietlenie zadan
        }

        db.collection("Rooms") // wejscie do kolekcji rooms
            .get()
            .addOnCompleteListener {
                val result: StringBuffer = StringBuffer()
                if (it.isSuccessful) {
                    for (document in it.result!!) { // petla przechodzaca przez wszystkie dokumenty kolekcji
                        val id = document.data.get("ID") //pobranie ID pokoju
                        klucz = id.toString()
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

    private fun users_list() {
        clear_view()
        val db = FirebaseFirestore.getInstance()
        db.collection("Rooms").document(searchID)
            .collection("Users") //wejscie do zakladki users w znalezionym pokoju
            .get()
            .addOnCompleteListener {
                val result: StringBuffer = StringBuffer()
                val textv3 = TextView(this) //stworzenie napisu
                textv3.textSize = 30f //rozmiar
                textv3.text = "Użytkownicy:" // wypianie emaila
                textv3.setTextColor(Color.parseColor("#000000"))
                binding.roomviewlayout.addView(textv3) //dodanie stworzonego napisu do aktywnosci
                if (it.isSuccessful) {
                    var i = 0
                    for (document in it.result!!) {
                        i = i + 1
                        val textv = TextView(this) //stworzenie napisu
                        textv.textSize = 18f //rozmiar
                        textv.text = i.toString() + ". " + document.data.get("email")
                            .toString() // wypianie emaila
                        binding.roomviewlayout.addView(textv) //dodanie stworzonego napisu do aktywnosci
                        val textv2 = TextView(this) //stworzenie napisu
                        textv2.textSize = 18f //rozmiar
                        textv2.text = document.data.get("role").toString() // wypianie roli
                        textv2.setTextColor(Color.parseColor("#51C558"))
                        binding.roomviewlayout.addView(textv2)
                    }
                }
            }
    }

    private fun clear_view() {
        if (null != binding.roomviewlayout && binding.roomviewlayout.getChildCount() > 0) {
            try {
                binding.roomviewlayout.removeViews(0, binding.roomviewlayout.getChildCount())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun chat() {
        clear_view()
        val db = FirebaseFirestore.getInstance()
        db.collection("Rooms").document(searchID)
            .collection("Chat") //wejscie do zakladki users w znalezionym pokoju
            .get()
            .addOnCompleteListener {
                val result: StringBuffer = StringBuffer()
                if (it.isSuccessful) {
                    for (document in it.result!!) {
                        val textv2 = TextView(this) //stworzenie napisu
                        textv2.textSize = 15f //rozmiar
                        textv2.text = document.data.get("data").toString() // wypianie daty
                        textv2.setTextColor(Color.parseColor("#008F47"))
                        binding.roomviewlayout.addView(textv2)
                        val textv = TextView(this) //stworzenie napisu
                        textv.textSize = 16f //rozmiar
                        textv.text =
                            document.data.get("text").toString() + "\n"// wypianie wiadomosci
                        binding.roomviewlayout.addView(textv)
                    }
                }
            }
    }
    private fun settings() {
        clear_view()
        val textv = TextView(this) //stworzenie napisu
        textv.textSize = 25f //rozmiar
        textv.text = "Klucz pokoju: " +  klucz + "\n"
        textv.setTextColor(Color.parseColor("#000000"))
        binding.roomviewlayout.addView(textv)
        val guzik = Button(this)
        guzik.width = 50
        guzik.height = 50
        guzik.text = "Opuść pokój"
        guzik.setOnClickListener{
            leaveRoom()
        }
        binding.roomviewlayout.addView(guzik)
    }

    private fun leaveRoom(){
        var todelete=""
        val db = FirebaseFirestore.getInstance()
        db.collection("Rooms").document(searchID).collection("Users")
            .get()
            .addOnCompleteListener {
                for (document in it.result!!) {
                    if(mail == document.data.get("email")){
                        todelete = document.id.toString()
                        break
                    }
                }
                db.collection("Rooms").document(searchID).collection("Users").document(todelete).delete()

            }
        db.collection("Rooms").document(searchID).collection("Chat") // wejscie do kolekcji chat w znalezionym pokoju
            .get().addOnCompleteListener(){task->
                if(task.isSuccessful) {
                    for (document2 in task.result!!){
                        chatcount = chatcount+1
                    }
                    val chat: MutableMap<String, Any> = HashMap() //stworzenie nowego dokumentu
                    chat["text"] = "Uzytkownik "+mail.toString()+" opuścił pokój."
                    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                    val currentDate = sdf.format(Date())
                    chat["data"]= currentDate
                    db.collection("Rooms").document(searchID).collection("Chat").document(chatcount.toString()).set(chat);//stworzenie nowego dokumentu z nazwą
                }
            }
        startActivity(Intent(this,Profile::class.java))
        finish()
    }

    private fun tasks(){
        clear_view()
    }

}