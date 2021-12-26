package com.example.whatsup

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsup.databinding.FragmentFriendsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.*

class friendsFragment : Fragment(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private lateinit var binding:FragmentFriendsBinding
    private lateinit var fileOutputStream: FileOutputStream
    private lateinit var database: DatabaseReference
    private lateinit var fauth: FirebaseAuth
     lateinit var friendList:ArrayList<Profile>
     lateinit var friendAdapter:friendAdapter
     private lateinit var newFriend:Profile
    private lateinit var friends:ArrayList<String>
    private lateinit var dialog: ProgressDialog
    private lateinit var timer: CountDownTimer
    private var data:String?=null
    private var file:String="Friends"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentFriendsBinding.inflate(inflater,container,false)
        database= FirebaseDatabase.getInstance().getReference()
        fauth= FirebaseAuth.getInstance()
        dialog= ProgressDialog(context)
        dialog.setMessage("Loading Friends")
        dialog.setCancelable(false)
        dialog.show()
        friendList= arrayListOf()
        friendAdapter= context?.let { friendAdapter(it,friendList) }!!
        binding.friendsView.adapter=friendAdapter
        binding.friendsView.layoutManager= LinearLayoutManager(context)
//        database.child("Profile").child(fauth.currentUser!!.uid).child("friend").addValueEventListener(object :
//            ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                friendList.clear()
//                for(frSnapshot in snapshot.children) {
//                    val friend = frSnapshot.getValue().toString()
//                    database.child("Profile").child(friend).get().addOnSuccessListener {
//                        val newFriend= it.getValue(Profile::class.java)
//                        dialog.cancel()
//                        if (newFriend != null) {
//                            friendList.add(newFriend)
//                        }
//                    }
//                }
//                Handler().postDelayed({
//                    friendAdapter.notifyDataSetChanged()
//                    Toast.makeText(context,friendList.size.toString(), Toast.LENGTH_SHORT).show()
////                    writeData()
//                }, 1000)
//
//            }

//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        })
        database.child("Profile").child(fauth.currentUser!!.uid).child("friend").orderByChild("lastMsgTime").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.childrenCount.equals(null))
                {
                    dialog.cancel()
                }
                else
                {
                    friendList.clear()
                    for (frSnapshot in snapshot.children) {
                             newFriend = frSnapshot.getValue(Profile::class.java)!!
                            dialog.cancel()
                            if (newFriend != null) {
                                friendList.add(newFriend)
                            }
                    }
                    friendList.reverse()
                        friendAdapter.notifyDataSetChanged()

//                    writeData()
                }
                }



            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        binding.contactView.setOnClickListener{
            var intent= Intent(context,contactList::class.java)
            startActivity(intent)
        }

        return binding.root
    }
    private fun dialogShow(){
        dialog= ProgressDialog(context)
        dialog.setMessage("Loading Ranks...")
        dialog.setCancelable(false)
        dialog.show()
        if(dialog.isShowing){
            timer = object: CountDownTimer(10000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                }
                override fun onFinish() {
//                    dialog.dismiss()
//                    Toast.makeText(context,"Network slow down ",Toast.LENGTH_SHORT).show()
//                    binding.tryAgain.setVisibility(View.VISIBLE)
//                    binding.tryAgain.setOnClickListener {
//                        dialog.show()
//                        fetchData()
//                        binding.tryAgain.setVisibility(View.INVISIBLE)
//                    }
                }
            }
            timer.start()
        }
    }
//    private fun writeData(){
//
//        for(i in friendList)
//        {
//            data += i.name.toString()+" "
//        }
//            try {
//                fileOutputStream = openFileOutput(file, Context.MODE_APPEND)
//                fileOutputStream.write(data?.toByteArray())
//            } catch (e: FileNotFoundException){
//                e.printStackTrace()
//            }catch (e: NumberFormatException){
//                e.printStackTrace()
//            }catch (e: IOException){
//                e.printStackTrace()
//            }catch (e: Exception){
//                e.printStackTrace()
//            }
//            Toast.makeText(context,"data save",Toast.LENGTH_LONG).show()
//    }


}