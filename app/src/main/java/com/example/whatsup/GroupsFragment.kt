package com.example.whatsup

import android.app.ProgressDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsup.databinding.FragmentGroupsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class GroupsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private lateinit var binding:FragmentGroupsBinding
    private lateinit var database: DatabaseReference
    private lateinit var database2: DatabaseReference
    private lateinit var fauth: FirebaseAuth
     lateinit var grpList:ArrayList<Group>
     lateinit var grpAdapter:GroupAdapter
    private lateinit var friends:ArrayList<String>
    private lateinit var dialog: ProgressDialog
    private lateinit var timer: CountDownTimer
    private var empty:Long=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentGroupsBinding.inflate(inflater,container,false)
        database= FirebaseDatabase.getInstance().getReference()
        fauth= FirebaseAuth.getInstance()
        dialog= ProgressDialog(context)
        dialog.setMessage("Loading Friends")
        dialog.setCancelable(false)
        dialog.show()
        grpList= arrayListOf()
        grpAdapter= context?.let { GroupAdapter(it,grpList) }!!
        binding.grpsView.adapter=grpAdapter
        binding.grpsView.layoutManager= LinearLayoutManager(context)
        database.child("Profile").child(fauth.currentUser!!.uid).child("Group").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.childrenCount==empty){
                    dialog.cancel()
                    Toast.makeText(context,"No Groups",Toast.LENGTH_SHORT).show()
                }
                else{
                    grpList.clear()
                    for(frSnapshot in snapshot.children) {
                        val grp = frSnapshot.getValue().toString()
                        database.child("Groups").child(grp).get().addOnSuccessListener {
                            val newGroup= it.getValue(Group::class.java)
                            dialog.cancel()
                            if (newGroup!= null) {
                                grpList.add(newGroup)
                            }
                        }
                    }
                    Handler().postDelayed({
                        grpAdapter.notifyDataSetChanged()
                        Toast.makeText(context,grpList.size.toString(), Toast.LENGTH_SHORT).show()
                    }, 1000)
                }


            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
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


}