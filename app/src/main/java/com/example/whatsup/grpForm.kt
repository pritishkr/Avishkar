package com.example.whatsup

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsup.databinding.ActivityGrpFormBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class grpForm : AppCompatActivity() {
    private lateinit var binding: ActivityGrpFormBinding
    private lateinit var database: DatabaseReference
    private lateinit var fauth: FirebaseAuth
    private lateinit var friendList:ArrayList<Profile>
    private lateinit var friendAdapter:GroupFriendAdapter
    private lateinit var friends:ArrayList<String>
    private lateinit var dialog: ProgressDialog
    private lateinit var timer: CountDownTimer
    private var selectedImage:Uri?=null
    private lateinit var storage:StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGrpFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database= FirebaseDatabase.getInstance().getReference()
        fauth= FirebaseAuth.getInstance()
        dialog= ProgressDialog(this)
        dialog.setMessage("Loading Friends")
        dialog.setCancelable(false)
        dialog.show()
        friendList= arrayListOf()
        val randomKey: String? = database.push().getKey()
        friendAdapter= randomKey?.let { GroupFriendAdapter(this,friendList, it) }!!
        binding.friendView.adapter=friendAdapter
        binding.friendView.layoutManager= LinearLayoutManager(this)
        database.child("Profile").child(fauth.currentUser!!.uid).child("friend").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                friendList.clear()
                for(frSnapshot in snapshot.children) {
                    val friend = frSnapshot.getValue().toString()
                    database.child("Profile").child(friend).get().addOnSuccessListener {
                        val newFriend= it.getValue(Profile::class.java)
                        dialog.cancel()
                        if (newFriend != null) {

                            friendList.add(newFriend)
                        }
                    }
                }
                Handler().postDelayed({
                    friendAdapter.notifyDataSetChanged()
                    Toast.makeText(this@grpForm,friendList.size.toString(),Toast.LENGTH_SHORT).show()
                }, 1000)

            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        binding.grpImage.setOnClickListener {
            launchGallery()
        }
        binding.Donegrp.setOnClickListener{

            var grpName=binding.grpName.text
            if(grpName==null){
                Toast.makeText(this,"Group Name cant be empty",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else{
                if (randomKey != null) {
                    database.child("Groups").child(randomKey).child("GroupUid").setValue(randomKey)
                    database.child("Groups").child(randomKey).child("GroupName").setValue(grpName.toString())
                    database.child("Groups").child(randomKey).child("GroupMembers").child(fauth.currentUser!!.uid).setValue(fauth.currentUser!!.uid)
                    database.child("Profile").child(fauth.currentUser!!.uid).child("Group").child(randomKey).setValue(randomKey)
                    if(selectedImage!=null){
                        storage=FirebaseStorage.getInstance().getReference().child("Profile").child(randomKey)
                        storage.putFile(selectedImage!!).addOnCompleteListener(OnCompleteListener {
                            if(it.isSuccessful){
                                storage.downloadUrl.addOnSuccessListener {
                                    database.child("Groups").child(randomKey).child("GroupImage").setValue(it.toString())
                                }
                            }
                            else{
                                Toast.makeText(this,"Problem in Uploading GroupImage",Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    else{
                        database.child("Groups").child(randomKey).child("GroupImage").setValue("No Image")
                        Toast.makeText(this,"No GroupImage",Toast.LENGTH_SHORT).show()
                    }
                    Toast.makeText(this,"Group Formed",Toast.LENGTH_SHORT).show()
                    var intent=Intent(this,GroupChat::class.java)
                    intent.putExtra("GroupUid",randomKey)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
    private fun dialogShow(){
        dialog= ProgressDialog(this)
        dialog.setMessage("Loading Ranks...")
        dialog.setCancelable(false)
        dialog.show()
        if(dialog.isShowing){
            timer = object: CountDownTimer(10000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                }
                override fun onFinish() {
//                    dialog.dismiss()
//                    Toast.makeText(context,"Network slow down ",Toast.LENGTH_SHORT).show()
//                    binding.tryAgain.setVisibility(View.VISIBLE)
//                    binding.tryAgain.setOnClickListener {
//                        dialog.show()
//                        fetchData()
//                        binding.tryAgain.setVisibility(View.INVISIBLE)
//                    }
                }
            }
            timer.start()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data!=null){
            if(data.data!=null){
                binding.grpImage.setImageURI(data.data)
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