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
import com.example.projekt1.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var actionBar: ActionBar
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private var email = ""
    private var password =""
    private var username =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)



        actionBar= supportActionBar!!
        actionBar.title = "Załóż konto"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Proszę czekać")
        progressDialog.setMessage("Tworzenie konta...")
        progressDialog.setCanceledOnTouchOutside(false)

        //firebase
        firebaseAuth= FirebaseAuth.getInstance()

        binding.guzikReg.setOnClickListener {
            validateData()
        }


    }



    private fun validateData() {
        email = binding.emailEt.text.toString().trim()
        password= binding.passwordEt.text.toString().trim()
        username= binding.nameEt.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //zly email
            binding.emailEt.error = "Nieprawidłowy format e-mail"
        }
        else if(TextUtils.isEmpty(password)){
            //haslo nie wpisane
            binding.passwordEt.error ="Należy wprowadzić hasło"
        }
        else if(password.length < 8){
            binding.passwordEt.error = "Hasło musi zawierać conajmniej 8 znaków!"
        }
        else if(username.length < 3 || username.length > 30 ){
            binding.nameEt.error = "Nazwa musi zawierać od 3 do 30 znaków!"
        }
        else{
            firebaseSignUp()
            saveFirestore(username,email)
        }
    }
    private fun firebaseSignUp(){
        progressDialog.show()
        //tworzenie konta
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
            progressDialog.dismiss()
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this,"Konto założone, email: $email", Toast.LENGTH_SHORT).show()
                //otwórz profil
                startActivity(Intent(this,Profile::class.java))
                finish()

           }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(this,"Nie udało się założyć konta, powód: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun saveFirestore(usrname: String, mail: String){
        val db = FirebaseFirestore.getInstance()
        val user: MutableMap<String, Any> = HashMap()
        user["username"] = username
        user["email"] = email
        db.collection("Users") //wejscie do kolekcji users, stworzenie jej jesli nie ma
            .add(user) //dodanie uzytkownika
            .addOnSuccessListener {
                Toast.makeText(this, "Utworzono uzytkownika", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this, "Nie udało się utworzyć uzytkownika", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}