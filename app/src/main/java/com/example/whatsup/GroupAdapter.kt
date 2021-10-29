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
import kotlinx.android.synthetic.main.group.view.*
import kotlinx.android.synthetic.main.row_table.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat

class GroupAdapter (private val context:Context, var grpList:ArrayList<Group>)
    :RecyclerView.Adapter<GroupAdapter.GroupViewHolder>(){
    class GroupViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    private  var database:DatabaseReference=FirebaseDatabase.getInstance().getReference()
    private  var fauth:FirebaseAuth= FirebaseAuth.getInstance()
    private var date: DateFormat = SimpleDateFormat("hh:mm a")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.group,parent,false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
       var grp:Group=grpList[position]
        holder.itemView.apply {
            grpName.text=grp.GroupName.toString()
        }
        Glide.with(context)
            .load(grp.GroupImage)
            .override(120, 120)
            .circleCrop()
            .placeholder(R.drawable.user)
            .into(holder.itemView.grpImage)
        database.child("Groups").child(grp.GroupUid.toString()).child("GroupChat").child("messages").limitToLast(1).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.childrenCount.equals(0)){
                    holder.itemView.grpLastmsg.setText("tap to chat")
                }
                else{
                    for(dataSnapshot in snapshot.children)
                    {
                        val lastMsg=dataSnapshot.getValue(Message::class.java)

                        if (lastMsg == null) {
                            holder.itemView.grpLastmsg.setText("Tap to chat")
                        }
                        else{

                            if(lastMsg.senderId.equals(fauth.currentUser!!.uid)){
                                holder.itemView.lastMsgSender.setText("")
                            }
                            else{
                                holder.itemView.lastMsgSender.setText(lastMsg.senderName+": ")
                            }
                            holder.itemView.grpLastMsgTime.setText((date.format(lastMsg.date.toString().toLong())).toString())
                            holder.itemView.grpLastmsg.setText(lastMsg.msg.toString())
                        }
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        holder.itemView.setOnClickListener{
            var intent=Intent(context,GroupChat::class.java)
            intent.putExtra("GroupUid",grp.GroupUid)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return  grpList.size
    }
}