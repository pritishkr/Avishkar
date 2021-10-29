package com.example.whatsup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.rowtable2.view.*

class sendAdapter (private val context:Context,private val frndList:ArrayList<Profile>, private val questionId:String)
    :RecyclerView.Adapter<sendAdapter.sendViewHolder>()
{
    private lateinit var database:DatabaseReference
    private lateinit var fauth:FirebaseAuth
    class sendViewHolder (itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): sendViewHolder {
        var view=LayoutInflater.from(context).inflate(R.layout.rowtable2,parent,false)
        return sendViewHolder(view)
    }
    override fun onBindViewHolder(holder: sendViewHolder, position: Int) {
         var friend:Profile=frndList[position]
        holder.itemView.apply {
            friendRankName.setText(friend.name)
        }
        holder.itemView.done.setOnClickListener{
            fauth= FirebaseAuth.getInstance()
          database=FirebaseDatabase.getInstance().getReference()
          database.child("BookMark").child(friend.uid.toString()).child(fauth.currentUser!!.uid).child(questionId).setValue(questionId).addOnSuccessListener {
              Toast.makeText(context,"Question sent",Toast.LENGTH_SHORT).show()
              holder.itemView.done.isEnabled=false
              holder.itemView.done.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
          }
        }
    }
    override fun getItemCount(): Int {
        return frndList.size
    }
}