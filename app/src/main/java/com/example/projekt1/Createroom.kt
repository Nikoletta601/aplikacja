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
            //sprawdzenie czy użytkownik jest zalogowany
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null){
                mail= firebaseUser.email.toString()
            }else{
                startActivity(Intent(this,Login::class.java))
                finish()
            }
            //przypisanie nazwy pokoju z wypełnonego okna
            roomname = binding.roomNameEt.text.toString().trim()
            //wylosowanie id pokoju przy uzyciu funkcji
            roomID = getRandomString(5)
            //wywolanie funckji
            saveFirestore(roomname,mail)

            startActivity(Intent(this, ShowMyRooms::class.java))
        }

        binding.guzikBack.setOnClickListener{
            //powrót do profilu
            startActivity(Intent(this, ShowMyRooms::class.java))
        }
    }

    fun saveFirestore(roomname: String, mail: String){
        val db = FirebaseFirestore.getInstance()
        val room: MutableMap<String, Any> = HashMap()
        //zapisanie danych ze zmiennych
        room["roomname"] = roomname
        room["creator"] = mail
        room["ID"] = roomID
        db.collection("Rooms")
            //dodanie dokumentu do kolekcji "Rooms"
            .add(room)
            .addOnSuccessListener {
                Toast.makeText(this, "Utworzono pokój, ID: " + roomID, Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(this, "Nie udało się utworzyć pokoju", Toast.LENGTH_SHORT).show()
            }
        db.collection("Rooms")
            .get()
            .addOnCompleteListener{
                val user: MutableMap<String, Any> = HashMap() //stworzenie nowego dokumentu
                user["email"] = mail
                user["role"] = "moderator" //przypisanie wartosci
                val result: StringBuffer = StringBuffer()
                if(it.isSuccessful) {
                    for (document in it.result!!) {
                        val id = document.data.get("ID") //pobranie ID pokoju
                        if (id == roomID) {
                            val docId = document.id //pobranie uid pokoju
                            db.collection("Rooms").document(docId)
                                .collection("Users") // wejscie do kolekcji users w znalezionym pokoju
                                .add(user) // dodanie uzytkownika do zakladki users w pokoju
                            var chatcount = 0
                            db.collection("Rooms").document(docId).collection("Chat") // wejscie do kolekcji chat w znalezionym pokoju
                                .get().addOnCompleteListener(){task->
                                    if(task.isSuccessful) {
                                        for (document2 in it.result!!){
                                            chatcount = chatcount+1
                                        }
                                }
                            }

                            val chat: MutableMap<String, Any> = HashMap() //stworzenie nowego dokumentu
                            chat["text"] = "Uzytkownik "+mail.toString()+" utworzył pokój."
                            db.collection("Rooms").document(docId).collection("Chat").document(chatcount.toString()).set(chat);//stworzenie nowego dokumentu z nazwą

                            break
                        }
                    }
                }
            }

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    //funkcja losująca ciąg znaków
    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }



}