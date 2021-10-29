package com.example.whatsup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.whatsup.databinding.ActivitySearchFriendsBinding

class searchFriends : AppCompatActivity() {
    private lateinit var binding:ActivitySearchFriendsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySearchFriendsBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_search_friends)
    }
}