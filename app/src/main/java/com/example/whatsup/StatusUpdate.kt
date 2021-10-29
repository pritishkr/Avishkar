package com.example.whatsup

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import androidx.core.net.toUri
import com.example.whatsup.databinding.ActivityImageSendBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class StatusUpdate : AppCompatActivity() {
    private lateinit var binding: ActivityImageSendBinding
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private lateinit var fauth: FirebaseAuth
    private var selectedImage: Uri? = null
    private lateinit var friendName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding= ActivityImageSendBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        fauth= FirebaseAuth.getInstance()
//        database=FirebaseDatabase.getInstance().getReference()
//        database.child("Profile").child(fauth.currentUser!!.uid).get().addOnSuccessListener {
//             friendName = it.child("name").value.toString()
//        }
//        binding.statusSend.setOnClickListener {
//            launchGallery()
//        }
//        binding.finalStatusSendBtn.setOnClickListener {
//            if(selectedImage!=null){
//                storage= FirebaseStorage.getInstance().getReference().child("Status").child(fauth.currentUser!!.uid)
//                storage.putFile(selectedImage!!).addOnCompleteListener(
//                    OnCompleteListener {
//                        Toast.makeText(this,"Status Process",Toast.LENGTH_SHORT).show()
//                        if(it.isSuccessful){
//                            storage.downloadUrl.addOnSuccessListener {
//                                    var caption=binding.statusCaption.text.toString()
//                                    var status=Status(it.toString(),fauth.currentUser!!.uid,friendName,caption)
//                                    database.child("Status").child(fauth.currentUser!!.uid).setValue(status).addOnSuccessListener {
//                                        Toast.makeText(this,"Status Uploaded",Toast.LENGTH_SHORT).show()
//                                    }
//
//                            }
//                        }
//                        else{
//                            Toast.makeText(this,"Task Unsuccesful",Toast.LENGTH_SHORT).show()
//                        }
//                    })
////                storage.child("Status").child(fauth.currentUser!!.uid).putFile(selectedImage!!).addOnCompleteListener(
////                    OnCompleteListener {
////                        if(it.isSuccessful){
////                            storage.downloadUrl.addOnSuccessListener {
////                                var status=Status(it.toString(),fauth.currentUser!!.uid)
////                                database.child("Status").child(fauth.currentUser!!.uid).setValue(status).addOnSuccessListener {
////                                    Toast.makeText(this,"Status Uploaded", Toast.LENGTH_SHORT).show()
////                                }
////                            }
////                        }
////                    })
//            }
//        }
//    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(data!=null){
//            if(data.data!=null){
//                binding.statusSend.setImageURI(data.data)
//                selectedImage= data.data!!
//
//            }
//        }
//    }
//    private fun launchGallery(){
//        var intent= Intent()
//        intent.setAction(Intent.ACTION_GET_CONTENT)
//        intent.setType("image/*")
//        startActivityForResult(intent,100)
//    }
    }
}