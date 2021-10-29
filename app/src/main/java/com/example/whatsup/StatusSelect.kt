package com.example.whatsup

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.whatsup.databinding.ActivityStatusSelectBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_status_select.*
import java.text.SimpleDateFormat

class StatusSelect : AppCompatActivity() {
    private var selectedImage: Uri?=null
    private lateinit var binding:ActivityStatusSelectBinding
    private lateinit var database:DatabaseReference
    private lateinit var storage:StorageReference
    private lateinit var UserName:String
    private lateinit var fauth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database=FirebaseDatabase.getInstance().getReference()
        fauth= FirebaseAuth.getInstance()
        binding= ActivityStatusSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        launchGallery()
        var uid=FirebaseAuth.getInstance().currentUser!!.uid
        database.child("Profile").child(uid).get().addOnSuccessListener {
            UserName=it.child("name").getValue().toString()
        }
        binding.finalStatusSendBtn.setOnClickListener {
            if(selectedImage!=null){
                storage=FirebaseStorage.getInstance().getReference().child("Status").child(uid)
                storage.putFile(selectedImage!!).addOnCompleteListener(OnCompleteListener {
                    if(it.isSuccessful){
                        storage.downloadUrl.addOnSuccessListener {
                            var status=Status(it.toString(),uid,UserName,statusCaption.text.toString(),System.currentTimeMillis().toString())
                            database.child("Status").child(uid).setValue(status).addOnSuccessListener {
                                Toast.makeText(this,"Status Updated",Toast.LENGTH_SHORT).show()
                                var intent=Intent(this,FirstPage::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                })
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data!=null){
            if(data.data!=null){
                binding.selectedStatus.setImageURI(data.data)
                selectedImage= data.data!!
            }
        }
    }
    private fun launchGallery(){
        var intent= Intent()
        intent.setAction(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent,100)
    }
}