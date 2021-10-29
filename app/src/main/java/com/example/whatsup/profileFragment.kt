package com.example.whatsup

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.whatsup.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class profileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private lateinit var binding: FragmentProfileBinding
    private lateinit var database:DatabaseReference
    private lateinit var auth:FirebaseAuth
    private lateinit var dialog:ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentProfileBinding.inflate(inflater,container,false)
        dialog= ProgressDialog(context)
        dialog.setMessage("Fetching data")
        dialog.setCancelable(false)
        dialog.show()
        auth= FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance().getReference()
           database.child("Profile").child(auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                dialog.cancel()
                Toast.makeText(context,"${auth.currentUser!!.email}", Toast.LENGTH_SHORT).show()
                val name=it.child("name").value.toString()
                val address =it.child("address").value.toString()
                val phone=it.child("phone").value.toString()
                val age=it.child("age").value.toString()
                val coin=it.child("coins").value.toString()
                val image=it.child("profile").value.toString()
                binding.userName.setText(name)
                binding.userEmail.setText(auth.currentUser!!.uid)
                binding.userPhone.setText(phone)
                binding.userAge.setText(age)
                binding.userAddress.setText(address)

                Glide.with(context!!)
                    .load(image)
                    .override(1000,1000)
                    .circleCrop()
                    .placeholder(R.drawable.user)
                    .into(binding.userImage)
            }.addOnFailureListener {
                   dialog.setMessage("Data Not fetched")
                   dialog.show()
                   Toast.makeText(context,"Failed", Toast.LENGTH_SHORT).show()
               }
        return binding.root
    }
}