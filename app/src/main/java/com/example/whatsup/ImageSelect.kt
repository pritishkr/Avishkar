package com.example.whatsup

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.whatsup.databinding.ActivityImageSelectBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_image_select.*
import kotlin.random.Random

 class ImageSelect : AppCompatActivity() {
    var selectedImage: Uri?=null
     var caption:String?=null

    private lateinit var binding:ActivityImageSelectBinding
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private lateinit var UserName:String
    private lateinit var fauth: FirebaseAuth
    private lateinit var senderRoom:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityImageSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database=FirebaseDatabase.getInstance().getReference()
        fauth= FirebaseAuth.getInstance()
        senderRoom=intent.getStringExtra("senderRoom").toString()
        launchGallery()
        var rand=database.push().key.toString()
        storage=FirebaseStorage.getInstance().getReference().child(senderRoom).child(rand.toString())
        binding.finalImageSendBtn.setOnClickListener {
            caption=binding.ImageCaption.text.toString()
            var intent=Intent()
            intent.putExtra("SelectedImage",selectedImage.toString())
            intent.putExtra("ImageCaption",caption.toString())
            setResult(RESULT_OK,intent)
            Toast.makeText(this,selectedImage.toString(),Toast.LENGTH_SHORT).show()
            finish()
//            if(selectedImage!=null){
//                storage.putFile(selectedImage!!).addOnCompleteListener(OnCompleteListener {
//                    if(it.isSuccessful){
//                        storage.downloadUrl.addOnSuccessListener {
//                            val message=Message(it.toString(),binding.ImageCaption.text.toString(),fauth.currentUser!!.uid,fauth.currentUser!!.uid,System.currentTimeMillis().toString(),false,rand)
//                            database.child("Chats").child(senderRoom).child("messages").child(rand).setValue(message).addOnSuccessListener {
////                                database.child("Chats").child(recieverRoom).child(rand).setValue(message).addOnSuccessListener {
//                                val intent=Intent()
//                                intent.putExtra("SelectedImage",selectedImage)
//                                Toast.makeText(this,"Image Send",Toast.LENGTH_SHORT).show()
//                                finish()
////                                }
//                            }
//                        }
//                    }
//                })
//            }
        }
     }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data!=null){
            if(data.data!=null){
                binding.selectedImage.setImageURI(data.data)
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