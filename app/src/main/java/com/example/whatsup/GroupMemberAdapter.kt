package com.example.whatsup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.grpfriend.view.*

class GroupMemberAdapter(private val context:Context,private val grpFriendList:ArrayList<Profile>)
    :RecyclerView.Adapter<GroupMemberAdapter.GroupMemberViewHolder>(){
    class GroupMemberViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupMemberViewHolder {
        var view=LayoutInflater.from(context).inflate(R.layout.grpfriend,parent,false)
        return GroupMemberViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupMemberViewHolder, position: Int) {
        val friend=grpFriendList[position]
        holder.itemView.apply {
            friendGroupName.setText(friend.name.toString())
        }
        Glide.with(context)
            .load(friend.profile)
            .override(120, 120)
            .circleCrop()
            .placeholder(R.drawable.user)
            .into(holder.itemView.groupImage)
    }

    override fun getItemCount(): Int {
        return grpFriendList.size
    }
}