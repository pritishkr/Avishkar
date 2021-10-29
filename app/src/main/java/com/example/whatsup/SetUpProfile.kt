package com.example.whatsup

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.whatsup.databinding.ActivitySetUpProfileBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlin.properties.Delegates

class SetUpProfile : AppCompatActivity() {
    private lateinit var binding: ActivitySetUpProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var fauth:FirebaseAuth
    private  var selectedImage:Uri?=null
    private lateinit var storage:FirebaseStorage
    private lateinit var reference:StorageReference
    private lateinit var dialog:ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySetUpProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fauth= FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance().getReference()
        storage= FirebaseStorage.getInstance()
       dialog= ProgressDialog(this)
        dialog.setCancelable(false)
        dialog.setMessage("Updating your Profile")

        binding.userImage.setOnClickListener {
        launchGallery()
        }


        binding.updateBtn.setOnClickListener {
            var name=binding.userName.text.toString().trim()
            var address=binding.address.text.toString().trim()
            var phone=binding.phone.text.toString().trim()
            var age=binding.age.text.toString().trim()

            if(name.isEmpty()){
                binding.userName.setError("Name Can't be empty")
                return@setOnClickListener
            }
            if(address.isEmpty()){
                binding.address.setError("Name Can't be empty")
                return@setOnClickListener
            }
            if(phone.isEmpty()){
                binding.phone.setError("Name Can't be empty")
                return@setOnClickListener
            }
            if(age.isEmpty()){
                binding.age.setError("Name Can't be empty")
                return@setOnClickListener
            }
            dialog.show()
            if(selectedImage!=null){

                reference=FirebaseStorage.getInstance().getReference().child("Profile").child(fauth.currentUser!!.uid)
                reference.putFile(selectedImage!!).addOnCompleteListener(OnCompleteListener {
                    if(it.isSuccessful){
                         reference.downloadUrl.addOnSuccessListener {
                             val user=Profile(name, address, phone, age,25,fauth.currentUser!!.uid.toString(),it.toString())
                             fauth.currentUser?.let { it1 -> database.child("Profile").child(it1.uid) }?.setValue(user)
                                 ?.addOnSuccessListener {
                                     dialog.dismiss()
                                     var intent=Intent(this,MainActivity::class.java)
                                     startActivity(intent)
                                     finish()
                                 }?.addOnFailureListener {
                                     dialog.dismiss()
                                     Toast.makeText(this,"Error In Updating ",Toast.LENGTH_SHORT).show()
                                 }
                         }
                    }
                })
            }
            else{
                val user=Profile(name, address, phone, age,25,fauth.currentUser!!.uid.toString(),"No Image")
                fauth.currentUser?.let { it1 -> database.child("Profile").child(it1.uid) }?.setValue(user)
                    ?.addOnSuccessListener {
                        dialog.dismiss()
                        var intent=Intent(this,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }?.addOnFailureListener {
                        dialog.dismiss()
                        Toast.makeText(this,"Error In Updating ",Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
       if(data!=null){
           if(data.data!=null){
               binding.userImage.setImageURI(data.data)
               selectedImage= data.data
           }
       }
    }
    private fun launchGallery(){
        var intent=Intent()
        intent.setAction(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent,100)
    }
}