package com.example.whatsup

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.whatsup.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var fauth:FirebaseAuth
    private lateinit var dialog:ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fauth= FirebaseAuth.getInstance()
        dialog= ProgressDialog(this)
        dialog.setMessage("Verifying User...")
        dialog.setCancelable(false)
        binding.loginBtn.setOnClickListener {

            val email=binding.loginemail.text.toString().trim()
            val password=binding.loginpassword.text.toString().trim()
            dialog.show()
            fauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){
                task->
                if(task.isSuccessful){
                    dialog.dismiss()
                    if(fauth.currentUser?.isEmailVerified == true){
                        Toast.makeText(this,"Logged In", Toast.LENGTH_SHORT).show()
                        val intent= Intent(this,SetUpProfile::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this,"Email Not Verified",Toast.LENGTH_SHORT).show()
                    }
                }
                else
                {
                    dialog.dismiss()
                    Toast.makeText(this,"${task.exception}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}