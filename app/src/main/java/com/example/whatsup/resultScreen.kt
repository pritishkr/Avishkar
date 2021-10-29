package com.example.whatsup

import android.app.Instrumentation
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.result.ActivityResult
import com.example.whatsup.databinding.ActivityResultScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.properties.Delegates

class resultScreen : AppCompatActivity() {
    private lateinit var binding:ActivityResultScreenBinding
    private var point:Long=0
    private var coin:Long=0
    private lateinit var database: DatabaseReference
    private lateinit var fauth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityResultScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        point=intent.getLongExtra("correct",0)
        fauth= FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance().getReference().child("Profile").child(fauth.currentUser!!.uid)
       database.get().addOnSuccessListener {
           coin=it.child("coins").value.toString().toLong()
       }
        Handler().postDelayed({
            coin += 10 * point
            database.child("coins").setValue(coin).addOnSuccessListener {
                Toast.makeText(this,"Coins Updated",Toast.LENGTH_SHORT).show()
            }
            var newcoins=coin + point*10
            database= FirebaseDatabase.getInstance().getReference()
            database.child("Profile").child(fauth.currentUser!!.uid).child("coins").setValue(coin)

            binding.points.setText(point.toString())
            binding.coins.setText((10*point).toString())
        }, 1000)
        binding.homepage.setOnClickListener {
            var intent=Intent(this,HomeFragment::class.java)
            startActivity(intent)
            finish()
        }

    }
}