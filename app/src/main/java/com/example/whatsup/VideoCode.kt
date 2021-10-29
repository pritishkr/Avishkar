package com.example.whatsup

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.whatsup.databinding.ActivityVideoCodeBinding
import java.net.MalformedURLException
import java.net.URL

class VideoCode : AppCompatActivity() {
    private lateinit var binding:ActivityVideoCodeBinding
    private lateinit var serverUrl:URL
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityVideoCodeBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_video_code)

    }
}