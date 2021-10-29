package com.example.whatsup

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.whatsup.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {
    private lateinit var  binding:ActivitySignUpBinding
    private lateinit var fauth: FirebaseAuth
     private lateinit var dialog:ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding= ActivitySignUpBinding.inflate(layoutInflater)

        setContentView(binding.root)
        fauth= FirebaseAuth.getInstance()
        val currentUser=fauth.currentUser
        if(currentUser!=null){
            val intent=Intent(this,FirstPage::class.java)
            startActivity(intent)
            finish()
        }
        dialog= ProgressDialog(this)
        dialog.setMessage("Sign in process..")
        dialog.setCancelable(false)
         binding.textView2.setOnClickListener {
             val intent=Intent(this,Login::class.java)
             startActivity(intent)
         }
        binding.signupBtn.setOnClickListener{
             val email=binding.useremail.text.toString()
            val name=binding.username.text.toString()
            val password=binding.password.text.toString()
            val conf_password=binding.confPassword.text.toString()
            Toast.makeText(this,"Button",Toast.LENGTH_SHORT).show()

            if(email.isEmpty()){
                binding.useremail.setError("Email Can't be empty")
                return@setOnClickListener
            }
            if(name.isEmpty()){
                binding.username.setError("Name can't be empty..")
                return@setOnClickListener
            }
            if(password.length<6){
                binding.password.setError("Password length should greater than 6")
                return@setOnClickListener
            }
            if(conf_password.length<6){
                binding.confPassword.setError("Confirm Password length should greater than 6")
                return@setOnClickListener
            }
            if(!password.equals(conf_password)){
                binding.confPassword.setError("Password should be same")
                return@setOnClickListener
            }
            dialog.show()
            fauth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        dialog.dismiss()
                        val user=fauth.currentUser
                        user!!.sendEmailVerification().addOnCompleteListener {
                            task->
                            if(task.isSuccessful){
                                Toast.makeText(this,"Email sent..Please check your email..",Toast.LENGTH_LONG).show()
                                binding.username.setText("")
                                binding.useremail.setText("")
                                binding.password.setText("")
                                binding.confPassword.setText("")
                            }
                            else{
                                Toast.makeText(this, task.exception?.message +"Problem Occured",Toast.LENGTH_LONG).show()
                            }
                        }
//                        val intent=Intent(this,EmailVerfication::class.java)
//                        startActivity(intent)

                    } else {
                        dialog.dismiss()
                        Toast.makeText(this,"Error Occured",Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
}