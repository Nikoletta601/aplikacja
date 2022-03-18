package com.example.projekt1

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.projekt1.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding

    private lateinit var actionBar: ActionBar

    private lateinit var progressDialog:ProgressDialog

    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar= supportActionBar!!
        actionBar.title="Login"

        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Proszę czekać")
        progressDialog.setMessage("Logowanie...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth= FirebaseAuth.getInstance()
        checkUser()

        binding.guzikBrakKonta.setOnClickListener{
            startActivity(Intent(this, SignUp::class.java))
        }

        binding.guzikLogin.setOnClickListener{
            validateData()
        }

    }
    private fun validateData() {
        email=binding.emailEt.text.toString().trim()
        password=binding.passwordEt.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            binding.emailEt.setError("Nieprawidłowy e-mail!")
        }
        else if (TextUtils.isEmpty(password)){

            binding.passwordEt.error="Proszę wpisać hasło"
        }
        else{
             firebaseLogin()
        }
    }

    private fun firebaseLogin(){
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "Zalogowano jako $email", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, Profile::class.java))
                finish()
            }
            .addOnFailureListener{ e->
                progressDialog.dismiss()
                Toast.makeText(this, "Logowanie nie udało się z powodu ${e.message}", Toast.LENGTH_SHORT).show()

            }
    }
    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){
            startActivity(Intent(this, Profile::class.java))
            finish()
        }
    }
}