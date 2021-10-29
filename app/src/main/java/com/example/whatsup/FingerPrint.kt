package com.example.whatsup

import android.annotation.TargetApi
import android.app.KeyguardManager
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.security.keystore.KeyProperties
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.whatsup.databinding.ActivityFingerPrintBinding
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator


class FingerPrint : AppCompatActivity() {
    private lateinit var keystore:KeyStore
    private lateinit var binding:ActivityFingerPrintBinding
    private lateinit var cipher:Cipher
    private var Key_Name:String="androidHive"
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFingerPrintBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        val fingerprintManager = getSystemService(FINGERPRINT_SERVICE) as FingerprintManager

        if(!fingerprintManager.isHardwareDetected){
            Toast.makeText(this,"Your Hardware doesn`t have FingerPrint Sensor",Toast.LENGTH_SHORT).show()
        }
        else{
            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.USE_FINGERPRINT)!=PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"FingerPrint Permission not allowed",Toast.LENGTH_SHORT).show()
            }
            else
            {
                if(!fingerprintManager.hasEnrolledFingerprints()){
                    Toast.makeText(this,"Regiseter at least One FingerPrint",Toast.LENGTH_SHORT).show()
                }
                else{
                    if(!keyguardManager.isKeyguardSecure){
                        Toast.makeText(this,"KeyGuard Setting not enable",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        generateKey();
                    }
                }

            }
        }

    }
    @TargetApi(Build.VERSION_CODES.M)
    private fun generateKey() {
        try {
            keystore= KeyStore.getInstance("AndroidKeyStore")
        }
        catch (e:Exception){
            e.printStackTrace()
        }
        var keyGenerator:KeyGenerator
//        try {
//            keyGenerator =
//                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
//        }catch (){


    }
}