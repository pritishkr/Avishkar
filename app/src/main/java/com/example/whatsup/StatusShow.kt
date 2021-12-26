package com.example.whatsup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.whatsup.databinding.ActivityStatusShowBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.android.synthetic.main.activity_status_show.*
import java.text.DateFormat
import java.text.SimpleDateFormat

class StatusShow : AppCompatActivity() {
    private lateinit var binding:ActivityStatusShowBinding
    private lateinit var database:DatabaseReference
    private lateinit var statusUid:String
    private lateinit var friendUid:String
    private lateinit var friendName:String
    private lateinit var statusTime:String
    private lateinit var caption:String
    private lateinit var fauth:FirebaseAuth
    private var date: DateFormat = SimpleDateFormat("hh:mm a")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityStatusShowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database=FirebaseDatabase.getInstance().getReference()
        fauth= FirebaseAuth.getInstance()
        statusUid=intent.getStringExtra("StatusUid").toString()
        friendUid=intent.getStringExtra("FriendUid").toString()
        statusTime=intent.getStringExtra("StatusTime").toString()
        friendName=intent.getStringExtra("FriendName").toString()
        caption=intent.getStringExtra("StatusCaption").toString()
        setSupportActionBar(binding.StatusToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title=null
            binding.StatusFriendName.setText(friendName)
            binding.StatusShowTime.setText(date.format(statusTime.toString().toLong()).toString())

        }
        binding.statusCaptionShow.setText(caption)
        Glide.with(this@StatusShow)
            .load(statusUid)
            .override(1000, 1000)
            .placeholder(R.drawable.user)
            .into(statusShow)
    }
}