package com.example.whatsup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.whatsup.databinding.ActivityInputquesBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.random.Random

class inputques : AppCompatActivity() {
    private lateinit var binding: ActivityInputquesBinding
    private lateinit var database:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityInputquesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button2.setOnClickListener {
            database=FirebaseDatabase.getInstance().getReference()
            val randomKey: String? = database.push().getKey()
            var ques=Questions(binding.ques.text.toString(),binding.opt1.text.toString(),binding.opt2.text.toString(),binding.opt3.text.toString(),binding.opt4.text.toString(),binding.index.text.toString().toInt(),binding.answer.text.toString().toInt(),randomKey.toString())

            if (randomKey != null) {
                database.child("Questions").child("general").child(randomKey).setValue(ques).addOnSuccessListener {
                    Toast.makeText(this,"Question Uploaded",Toast.LENGTH_SHORT).show()
                    binding.answer.setText("")
                    binding.ques.setText("")
                    binding.opt1.setText("")
                    binding.opt2.setText("")
                    binding.opt3.setText("")
                    binding.opt4.setText("")
                    binding.index.setText("")
                }
            }
        }

    }
}