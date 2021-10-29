package com.example.whatsup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.whatsup.databinding.ActivityStartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.Format
import java.text.SimpleDateFormat

class startActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    private lateinit var database: DatabaseReference
    private lateinit var questionList:ArrayList<Questions>
    private lateinit var catId: String
    private lateinit var fauth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database=FirebaseDatabase.getInstance().getReference()
        fauth= FirebaseAuth.getInstance()
        catId= intent.getStringExtra("catId").toString()
        binding.startBtn.setOnClickListener {

            database.child("Register").child(catId).child(fauth.currentUser!!.uid).setValue(fauth.currentUser!!.uid).addOnSuccessListener {
                Toast.makeText(this,"Registered Succesfully",Toast.LENGTH_SHORT).show()
                binding.startBtn.setText("Registered")
                binding.startBtn.isEnabled=false
            }
        }
        binding.bookBtn.setOnClickListener {
           var intent=Intent(this,Bookmark::class.java)
            intent.putExtra("catId",catId)
            startActivity(intent)
//            var intent= Intent(this,Bookmark::class.java)
//            intent.putExtra("catId",catId)
//            startActivity(intent)
        }
    }
}