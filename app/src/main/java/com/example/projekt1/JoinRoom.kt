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
import com.example.projekt1.databinding.ActivityJoinRoomBinding
import com.example.projekt1.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class JoinRoom : AppCompatActivity() {

    private lateinit var binding: ActivityJoinRoomBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    private var roomID = ""
    private var mail =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityJoinRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)


        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Proszę czekać")
        progressDialog.setMessage("Tworzenie pokoju...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.guzikJoinRoom.setOnClickListener {
            val firebaseUser = firebaseAuth.currentUser
            //sprawdzenie czy uzytkownik jest zalogowany
            if (firebaseUser != null){
                mail= firebaseUser.email.toString()
            }else{
                startActivity(Intent(this,Login::class.java))
                finish()
            }
            //przypisanie wprowadzonych danych do zmiennej
            roomID = binding.roomJoinEt.text.toString().trim()
            //wywolanie funkcji
            addFirestore(roomID,mail)

            startActivity(Intent(this, Profile::class.java))
        }

        binding.guzikBack.setOnClickListener{
            //powrot do profilu
            startActivity(Intent(this, Profile::class.java))
        }
    }

    fun addFirestore(roomid: String, mail: String) {
        val db = FirebaseFirestore.getInstance()
        var succ = 0 //zmienna do sprawdzania czy pokój został znaleziony
        db.collection("Rooms") // wejscie do kolekcji Rooms
            .get()
            .addOnCompleteListener {
                val result: StringBuffer = StringBuffer()
                if (it.isSuccessful) {
                    for (document in it.result!!) {
                        //pętla przechodząca przez wszystkie pozycje w kolekcji "Rooms"
                        val id = document.data.get("ID") //pobranie ID pokoju
                        if (id == roomID) { //jeśli id zgadza się z wyszukiwanym ID
                            succ = 1 //zmiana wartosci na 1 -znaleziono pokój
                            val docId = document.id //pobranie uid pokoju
                            db.collection("Rooms").document(docId).collection("Users")// wejscie do kolekcji users w znalezionym pokoju
                                .get().addOnCompleteListener { task->
                                    if (task.isSuccessful) {
                                        for (doc in task.result!!) { //przejscie przez cala kolekcje users w pokoju
                                            val checkemail = doc.data.get("email") // pobranie maila ze sprawdzanego uzytkownika
                                            if (mail == checkemail) { // jesli mail jest taki sam ze sprawdzanym uzykownikiem
                                                Toast.makeText(this, "Należysz już do tego pokoju", Toast.LENGTH_SHORT).show()
                                                return@addOnCompleteListener // koniec funkcji
                                                break
                                            }
                                        }
                                            val user: MutableMap<String, Any> =
                                                HashMap() //stworzenie nowego dokumentu
                                            user["email"] = mail
                                            user["role"] = "normal_user" //przypisanie wartosci
                                            db.collection("Rooms").document(docId).collection("Users") // wejscie do kolekcji users w znalezionym pokoju
                                                .add(user) // dodanie uzytkownika do zakladki users w pokoju
                                                .addOnSuccessListener {
                                                    Toast.makeText(this, "Dołączono do pokoju", Toast.LENGTH_SHORT).show()
                                                }.addOnFailureListener {
                                                    Toast.makeText(this, "Nie udało się dołączyć do pokoju", Toast.LENGTH_SHORT).show()
                                                }
                                    }
                                }
                            break
                        }
                    }
                    if (succ == 0) { //jesli nie znaleziono pokoju o podanym id
                        Toast.makeText(this, "Nie znaleziono pokoju", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}