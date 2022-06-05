package com.example.projekt1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.projekt1.databinding.ActivityShowMyRoomsBinding
import com.example.projekt1.databinding.ActivityTaskBinding
import com.example.projekt1.databinding.ActivityTasksViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore

class task : AppCompatActivity() {
    private lateinit var binding: ActivityTaskBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var actionBar: ActionBar
    private lateinit var databaseReference: DatabaseReference

    private var mail =""//logged in user's mail from database
    private var docId2 =""//task id
    private var roomid = ""
    private var backvalue = "0"
    private var creator = ""

    var tasksdonearray = java.util.ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        //sprawdzenie czy użytkownik jest zalogowany
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){
            mail= firebaseUser.email.toString()
            roomid = intent.getStringExtra("id").toString()
            docId2 = intent.getStringExtra("id2").toString()
            backvalue = intent.getStringExtra("back").toString()
            tasksdonearray= intent.getStringArrayListExtra("tasksdonearray") as java.util.ArrayList<String>
            TasksList()
        }else{
            startActivity(Intent(this,Login::class.java))
            finish()
        }
        binding.guzikBack.setOnClickListener {
            //powrót do profilu
            if(backvalue == "0"){
                startActivity(Intent(this, mytasks::class.java))
            }else if(backvalue == "1"){
                val intent = Intent(this,tasks_view::class.java)
                intent.putExtra("id2",roomid)// wyslanie danych do pliku z intent
                startActivity(intent)

            }

        }
        binding.guzikRaport.setOnClickListener {
            //startActivity(Intent(this, Raport::class.java))

                val intent = Intent(this,Raport::class.java)
                intent.putExtra("creator",creator)
            intent.putExtra("roomid",roomid)
                intent.putExtra("taskId",docId2)// wyslanie danych do pliku z intent
                //intent.putExtra("back", "1")
                //intent.putStringArrayListExtra("tasksdonearray", tasksdonearray)
                startActivity(intent)
                //finish()

        }
    }
    fun TasksList() {
        val db = FirebaseFirestore.getInstance()
        val result: StringBuffer = StringBuffer()
        db.collection("Rooms").document(roomid).collection("Tasks").get().addOnCompleteListener {
            val result: StringBuffer = StringBuffer()
            if (it.isSuccessful) {
                for (doc in it.result!!) {
                    if (doc.id == docId2) {
                        println(doc.id)
                        creator=doc.data.get("creator").toString()
                        val guzik = Button(this)
                        guzik.width = 50
                        guzik.height = 50
                        guzik.text = doc.data.get("tresc").toString()
                        binding.TaskName.text = doc.data.get("tresc").toString()
                        binding.comment.text = "Komentarz: " + doc.data.get("komentarz").toString()
                        binding.deadline.text =
                            "Termin wykonania: " + doc.data.get("deadline").toString()
                        binding.points.text = "Punkty do zdobycia: " + doc.data.get("maxpunkty").toString()
                        binding.creator.text =
                            "Autor zadania: " + doc.data.get("creator").toString()
                        if(backvalue == "1"){
                            var docsize = doc.get("email") as ArrayList<String>
                            for (i in docsize.indices) {
                                binding.dlakogo.text = binding.dlakogo.text.toString() +"\n" + docsize[i]
                            }
                            for (i in tasksdonearray.indices){
                                if(tasksdonearray[i] == doc.id){
                                    binding.wykonane.text = "Wykonane"
                                    break
                                }
                            }
                        }else{
                            binding.dlakogo.text = " "
                            binding.wykonane.text = " "
                        }
                        break
                    }
                }
            }
        }
    }
}


