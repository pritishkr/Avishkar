package com.example.whatsup

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.row_table.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat

class friendAdapter(private var context: Context,  var rankList:ArrayList<Profile>)
    :RecyclerView.Adapter<friendAdapter.friendViewHolder>() {
    class friendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    private lateinit var database:DatabaseReference
    private lateinit var fauth:FirebaseAuth
    private var date:DateFormat=SimpleDateFormat("hh:mm a")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): friendViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.row_table, parent, false)
        return friendViewHolder(view)
    }

    override fun onBindViewHolder(holder: friendViewHolder, position: Int) {
        var user: Profile = rankList[position]
        holder.itemView.apply {
            friendRankName.setText(user.name.toString())
        }
        Glide.with(context)
            .load(user.profile)
            .override(1000, 1000)
            .circleCrop()
            .placeholder(R.drawable.user)
            .into(holder.itemView.friendImage)
        database=FirebaseDatabase.getInstance().getReference()
        fauth= FirebaseAuth.getInstance()
        database.child("Profile").child(fauth.currentUser!!.uid).child("friend").child(user.uid.toString()).child("messages").limitToLast(1).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.childrenCount.equals(0)){
                    holder.itemView.lastMsg.setText("Tap to chat")
                }
                else {
                    for (datasnapshot in snapshot.children) {
                        var lastMsgTime=datasnapshot.child("date").getValue()
                        var lastMsg = datasnapshot.child("msg").getValue()
                        if (lastMsg == null) {
                            holder.itemView.lastMsg.setText("Tap to chat")
                        } else {
                            holder.itemView.lastMsg.setText(lastMsg.toString())

                            holder.itemView.lastMsgTime.setText((date.format(lastMsgTime.toString().toLong())).toString())
                        }
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        holder.itemView.setOnClickListener {
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra("uid", user.uid.toString())
            intent.putExtra("name",user.name.toString())
            intent.putExtra("Image",user.profile.toString())
            intent.putExtra("phone",user.phone.toString())
            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return rankList.size
    }
}