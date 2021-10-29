package com.example.whatsup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.bumptech.glide.Glide
import com.example.whatsup.databinding.ActivityVideoCallBinding

class VideoCall : AppCompatActivity() {
    private lateinit var binding:ActivityVideoCallBinding
    private lateinit var handler:Handler
    private lateinit var friendUid:String
    private lateinit var friendImage:String
    private lateinit var friendName:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityVideoCallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        friendImage=intent.getStringExtra("FriendImage").toString()
        friendName=intent.getStringExtra("FriendName").toString()
        friendUid=intent.getStringExtra("FriendUid").toString()
        Glide.with(this)
            .load(friendImage)
            .override(1000, 1000)
            .circleCrop()
            .placeholder(R.drawable.user)
            .into(binding.FriendVideoImage)
    }
}