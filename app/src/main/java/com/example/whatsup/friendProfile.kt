package com.example.whatsup

import android.Manifest.permission.CALL_PHONE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.whatsup.databinding.ActivityFriendProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_set_up_profile.*
import kotlinx.android.synthetic.main.row_table.view.*
import java.security.Permission
import java.util.jar.Manifest

class friendProfile : AppCompatActivity() {
    private lateinit var binding: ActivityFriendProfileBinding
    private lateinit var database:DatabaseReference
    private lateinit var fauth:FirebaseAuth
    private lateinit var uid:String
    private lateinit var profile:String
    private lateinit var phone:String
    private lateinit var friend:Profile
    private var value:Int=0
    private val requestCall = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFriendProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        database=FirebaseDatabase.getInstance().getReference()
        fauth= FirebaseAuth.getInstance()
        uid=intent.getStringExtra("uid").toString()
        var name=intent.getStringExtra("name").toString()
        if (uid != null) {
            database.child("Profile").child(uid).get().addOnSuccessListener {
                 friend= it.getValue(Profile::class.java)!!
                binding.userAddress.setText(it.child("address").value.toString())
                binding.userAge.setText(it.child("age").value.toString())
                binding.userPhone.setText(it.child("phone").value.toString())
                binding.userName.setText(it.child("name").value.toString())
                binding.userCoin.setText(it.child("coins").value.toString())
                profile=it.child("profile").value.toString()
                Glide.with(this)
                    .load(profile)
                    .override(1000, 1000)
                    .circleCrop()
                    .placeholder(R.drawable.user)
                    .into(binding.friendImage)
                phone=it.child("phone").value.toString()
                database.child("Profile").child(fauth.currentUser!!.uid).child("friend").get().addOnSuccessListener {
                    val isFriend = it.child(uid).value.toString()
                    Toast.makeText(this, "Hii$isFriend",Toast.LENGTH_SHORT).show()
                    if (isFriend == uid) {
                        binding.friend.setText("UnFriend")
                        value = 1
                    }
                    else {
                        binding.friend.setText("Add to Friends")
                    }
                    if(uid == fauth.currentUser!!.uid){
                        binding.friend.setText("")
                    }
                }

            }
        }
        binding.friend.setOnClickListener {
            if (uid != null) {
                         if(value == 1){
                             Toast.makeText(this, "Processing", Toast.LENGTH_SHORT)
                                 .show()
                             database.child("Profile").child(fauth.currentUser!!.uid).child("friend").child(uid.toString()).setValue(null).addOnSuccessListener {
                                 Toast.makeText(this, "U are Not friends ", Toast.LENGTH_SHORT)
                                     .show()
                             }
                         }
                        else {
                            database.child("Profile").child(fauth.currentUser!!.uid).child("friend")
                                .child(uid).setValue(friend).addOnSuccessListener {
                                    Toast.makeText(this, "U are friends ", Toast.LENGTH_SHORT)
                                        .show()
                                }
                        }
                    }
        }
        binding.message.setOnClickListener {
            var intent=Intent(this,MessageActivity::class.java)
            intent.putExtra("profile",profile)
            intent.putExtra("uid",uid)
            intent.putExtra("name",name)
            intent.putExtra("phone",phone)
            startActivity(intent)
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.message, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.call-> {
//                makePhoneCall()
//                return true
//            }
//            R.id.video -> {
//                Toast.makeText(applicationContext, "Settings", Toast.LENGTH_LONG).show()
//                return true
//            }
//            R.id.mediaBox -> {
//                var intent=Intent(this,FriendList::class.java)
//                startActivity(intent)
//                return true
//            }
//            R.id.mute -> {
//                var intent=Intent(this,FriendList::class.java)
//                startActivity(intent)
//                return true
//            }
//            R.id.clear -> {
//                var intent=Intent(this,FriendList::class.java)
//                startActivity(intent)
//                return true
//            }
//
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
    private fun makePhoneCall() {
        if (true) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CALL_PHONE),
                    requestCall
                )
            } else {
                val dial = "tel:$phone"
                startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
            }
        } else {
            Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCall) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall()
            }
            else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }

}