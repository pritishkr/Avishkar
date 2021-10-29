package com.example.whatsup

import android.app.ProgressDialog
import android.graphics.Path
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsup.databinding.FragmentLeaderBoardBinding
import com.google.firebase.database.*
import com.google.firebase.firestore.Query

class LeaderBoardFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private lateinit var binding: FragmentLeaderBoardBinding
    private lateinit var database:DatabaseReference
    private lateinit var users:ArrayList<Profile>
    private lateinit var dialog:ProgressDialog
    private lateinit var rankAdapter: rankAdapter
    private lateinit var timer:CountDownTimer
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentLeaderBoardBinding.inflate(inflater,container,false)
        dialogShow()
        fetchData()

        return binding.root
    }
    private fun fetchData(){
        users= arrayListOf()
        rankAdapter= rankAdapter(context!! ,users)
        binding.recyclerView.adapter=rankAdapter
        binding.recyclerView.layoutManager=LinearLayoutManager(context)
        database= FirebaseDatabase.getInstance().getReference()
        database.child("Profile").orderByChild("coins").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                users.clear()
                dialog.cancel()
                for(datasnapshot in snapshot.children){
                    val user=datasnapshot.getValue(Profile::class.java)
                    users.add(user!!)
                }
                users.reverse()
                rankAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"No rank",Toast.LENGTH_SHORT).show()
            }

        })
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