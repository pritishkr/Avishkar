package com.example.whatsup

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsup.databinding.ActivitySendQuesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SendQues : AppCompatActivity() {
    private lateinit var binding:ActivitySendQuesBinding
    private lateinit var database:DatabaseReference
    private lateinit var fauth:FirebaseAuth
    private lateinit var friendList:ArrayList<Profile>
    private lateinit var friendAdapter:sendAdapter
    private lateinit var friends:ArrayList<String>
    private lateinit var dialog: ProgressDialog
    private lateinit var timer: CountDownTimer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendQuesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance().getReference()
        fauth = FirebaseAuth.getInstance()
        dialog = ProgressDialog(this)
        dialog.setMessage("Loading Friends")
        dialog.setCancelable(false)
        dialog.show()
        val questionId=intent.getStringExtra("questionId").toString()
        Toast.makeText(this,questionId,Toast.LENGTH_SHORT).show()
        friendList = arrayListOf()
        friendAdapter = sendAdapter(this, friendList,questionId)
        binding.friendView.adapter = friendAdapter
        binding.friendView.layoutManager = LinearLayoutManager(this)
        database.child("Profile").child(fauth.currentUser!!.uid).child("friend")
            .addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    friendList.clear()
                    for (frSnapshot in snapshot.children) {
                        val friend = frSnapshot.getValue().toString()
                        database.child("Profile").child(friend).get().addOnSuccessListener {
                            val newFriend = it.getValue(Profile::class.java)
                            if (newFriend != null) {
                                dialog.cancel()
                                friendList.add(newFriend)
                            }
                        }
                    }
                    Handler().postDelayed({
                        friendAdapter.notifyDataSetChanged()
                        Toast.makeText(
                            this@SendQues,
                            friendList.size.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }, 1000)
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            } )
    }
        private fun dialogShow(){
            dialog= ProgressDialog(this)
            dialog.setMessage("Loading Ranks...")
            dialog.setCancelable(false)
            dialog.show()
            if(dialog.isShowing){
                timer = object: CountDownTimer(10000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                    }
                    override fun onFinish() {
                    }
                }
                timer.start()
            }
        }
}