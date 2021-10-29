package com.example.whatsup

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.whatsup.databinding.FragmentWalletBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class WalletFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private lateinit var binding: FragmentWalletBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database:DatabaseReference
    private var coin by Delegates.notNull<Long>()
    private  lateinit var name:String
    private   val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    private lateinit var dialog:ProgressDialog
    private var minCoin:Long=10000
    private var givenCoin: Long=0
    private var remainCoin: Long=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentWalletBinding.inflate(inflater,container,false)
        auth= FirebaseAuth.getInstance()
        dialog= ProgressDialog(context)
        dialog.setMessage("Fetching data")
        dialog.setCancelable(false)
        dialog.show()

        database=FirebaseDatabase.getInstance().getReference()
        database.child("Profile").child(auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                dialog.cancel()
                 coin=it.child("coins").value.toString().toLong()
                name=it.child("name").value.toString()
                binding.avlcoins.setText(coin.toString())
            }.addOnFailureListener {
                dialog.setMessage("Coins not fetched.")
                dialog.show()
            }
        binding.sendBtn.setOnClickListener {
            givenCoin=binding.noOfCoins.text.toString().toLong()
            if(givenCoin>=minCoin && givenCoin<=coin ){
                 val uid=auth.currentUser!!.uid
                val email=binding.phoneBox.text.toString().toLong()
                val request=name
                val coins=givenCoin
                val withdraw=withDraw(uid,sdf.format(Date()),email,request,coins)

                database.child("WithdrawRequests").child(uid).setValue(withdraw).addOnSuccessListener {
                    remainCoin=coin-givenCoin
                    database.child("Profile").child(uid).child("coins").setValue(remainCoin).addOnSuccessListener {
                        Toast.makeText(context,"Requested",Toast.LENGTH_SHORT).show()
                        binding.phoneBox.setText("")
                        binding.noOfCoins.setText("")
                    }
                }

            }else{
                Toast.makeText(context,"Insufficient Coins",Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }


}