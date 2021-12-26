package com.example.whatsup

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsup.databinding.ActivityContactListBinding
import com.example.whatsup.databinding.FragmentLeaderBoardBinding
import com.google.firebase.database.*

class contactList : AppCompatActivity() {
    private lateinit var binding: ActivityContactListBinding
    private lateinit var database: DatabaseReference
    private lateinit var users:ArrayList<Profile>
    private lateinit var rankAdapter: rankAdapter
    private lateinit var timer: CountDownTimer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityContactListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fetchData()
    }
    private fun fetchData(){
        users= arrayListOf()
        rankAdapter= rankAdapter(this ,users)
        binding.recyclerView.adapter=rankAdapter
        binding.recyclerView.layoutManager= LinearLayoutManager(this)
        database= FirebaseDatabase.getInstance().getReference()
        database.child("Profile").orderByChild("coins").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                users.clear()
                for(datasnapshot in snapshot.children){
                    val user=datasnapshot.getValue(Profile::class.java)
                    users.add(user!!)
                }
                users.reverse()
                rankAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(this,"No rank", Toast.LENGTH_SHORT).show()
            }

        })
    }
}