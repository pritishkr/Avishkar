package com.example.whatsup

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.status.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat

class StatusAdapter (private val context:Context, var statusList:ArrayList<Status>)
    :RecyclerView.Adapter<StatusAdapter.StatusViewHolder>(){
    class StatusViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    private  var fauth:FirebaseAuth= FirebaseAuth.getInstance()
    private var database:DatabaseReference=FirebaseDatabase.getInstance().getReference()
    private var date: DateFormat = SimpleDateFormat("hh:mm a")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
       var view=LayoutInflater.from(context).inflate(R.layout.status,parent,false)
        return StatusViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        var currentStatus:Status=statusList[position]
            holder.itemView.apply {
                    friendName.setText(currentStatus.friendName)
                    friendStatusTime.setText(date.format(currentStatus.date.toString().toLong()).toString())
            }
            Glide.with(context)
                .load(currentStatus.statusUid)
                .override(1000,1000)
                .circleCrop()
                .placeholder(R.drawable.user)
                .into(holder.itemView.friendStatusImage)
        holder.itemView.setOnClickListener{
            var intent= Intent(context,StatusShow::class.java)
            intent.putExtra("StatusUid",currentStatus.statusUid)
            intent.putExtra("FriendUid",currentStatus.friendUid)
            intent.putExtra("FriendName",currentStatus.friendName)
            intent.putExtra("StatusTime",currentStatus.date)
            intent.putExtra("StatusCaption",currentStatus.caption)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return statusList.size
    }
}