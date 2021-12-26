package com.example.whatsup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsup.databinding.FragmentStatusBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class StatusFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private lateinit var binding:FragmentStatusBinding
    private lateinit var database:DatabaseReference
    private lateinit var fauth:FirebaseAuth
    lateinit var statusAdapter: StatusAdapter
    lateinit var statusList:ArrayList<Status>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentStatusBinding.inflate(inflater,container,false)
        database=FirebaseDatabase.getInstance().getReference().child("Status")
        fauth=FirebaseAuth.getInstance()
        statusList = arrayListOf()
        fauth = FirebaseAuth.getInstance()
        statusAdapter = StatusAdapter(requireContext(), statusList)
        binding.unseenStatus.layoutManager=LinearLayoutManager(context)
        binding.unseenStatus.adapter=statusAdapter
        database.orderByChild("date").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                statusList.clear()
                for(dataSnapshot in snapshot.children){
                    var status=dataSnapshot.getValue(Status::class.java)
                    if(status!=null){
                        if(System.currentTimeMillis()-status.date.toString().toLong()>=86400000)
                        {
                            database.child(status.friendUid.toString()).removeValue()
                        }
                        else{
                            statusList.add(status)
                        }
                    }
                }
                statusAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        binding.cardView4.setOnClickListener {
               var intent=Intent(context,StatusSelect::class.java)
            startActivity(intent)
        }


        return binding.root
    }

}