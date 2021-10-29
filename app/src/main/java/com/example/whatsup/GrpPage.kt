package com.example.whatsup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsup.databinding.ActivityGrpFormBinding
import com.example.whatsup.databinding.ActivityGrpPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class GrpPage : AppCompatActivity() {
    private lateinit var binding: ActivityGrpPageBinding
    private lateinit var database:DatabaseReference
    private lateinit var fauth:FirebaseAuth
    private lateinit var memberAdapter: GroupMemberAdapter
    private lateinit var members:ArrayList<Profile>
    private lateinit var GroupUid:String
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityGrpPageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        database=FirebaseDatabase.getInstance().getReference()
        fauth= FirebaseAuth.getInstance()
        members= arrayListOf()
        memberAdapter= GroupMemberAdapter(this,members)
        binding.GroupMemberView.layoutManager=LinearLayoutManager(this)
        binding.GroupMemberView.adapter=memberAdapter
        GroupUid=intent.getStringExtra("GroupUid").toString()
        database.child("Groups").child(GroupUid).child("GroupMembers").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                members.clear()
               for(dataSnapshot in snapshot.children){
                   var friend=dataSnapshot.value.toString()
                   database.child("Profile").child(friend).get().addOnSuccessListener {
                       var Member=it.getValue(Profile::class.java)
                       if (Member != null) {
                           members.add(Member)
                       }
                   }
               }
                Handler().postDelayed({
                    memberAdapter.notifyDataSetChanged()
                }, 1000)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}