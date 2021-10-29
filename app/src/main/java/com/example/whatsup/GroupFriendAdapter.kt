package com.example.whatsup
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_grp_form.view.*
import kotlinx.android.synthetic.main.grpfriend.view.*
import kotlinx.android.synthetic.main.grpfriend.view.done
import kotlinx.android.synthetic.main.row_table.view.*
import kotlinx.android.synthetic.main.row_table.view.friendImage
import kotlinx.android.synthetic.main.rowtable2.view.*

class GroupFriendAdapter (private var context: Context, private var friendList:ArrayList<Profile>,private var randomKey:String)
    : RecyclerView.Adapter<GroupFriendAdapter.GroupingViewHolder>() {
    class GroupingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var database:DatabaseReference
    private lateinit var fauth:FirebaseAuth

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupingViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.grpfriend, parent, false)
        return GroupingViewHolder(view)
    }
    override fun onBindViewHolder(holder: GroupingViewHolder, position: Int) {
        var user: Profile = friendList[position]
        holder.itemView.apply {
            friendGroupName.setText(user.name.toString())
        }
        Glide.with(context)
            .load(user.profile)
            .override(120, 120)
            .circleCrop()
            .placeholder(R.drawable.user)
            .into(holder.itemView.groupImage)

        holder.itemView.done.setOnClickListener {
            database=FirebaseDatabase.getInstance().getReference()
                    database.child("Groups").child(randomKey).child("GroupMembers").child(user.uid.toString()).setValue(user.uid.toString())

            .addOnSuccessListener {
                database.child("Profile").child(user.uid.toString()).child("Group").child(randomKey).setValue(randomKey).addOnSuccessListener {
                    Toast.makeText(context,"Group member ${user.name} added", Toast.LENGTH_SHORT).show()
                    holder.itemView.done.isEnabled=false
                    holder.itemView.done.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return friendList.size
    }
}
