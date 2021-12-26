package com.example.whatsup

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
import kotlinx.android.synthetic.main.row_leaderboard.view.*

class rankAdapter(private var context: Context, private var rankList:ArrayList<Profile>)
    :RecyclerView.Adapter<rankAdapter.rankViewHolder>(){
    class rankViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    private lateinit var database: DatabaseReference
    private lateinit var fauth:FirebaseAuth
    private  var friend:Profile?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): rankViewHolder {
        var view=LayoutInflater.from(context).inflate(R.layout.row_leaderboard,parent,false)
        return  rankViewHolder(view)
    }
    override fun onBindViewHolder(holder: rankViewHolder, position: Int) {
      var user:Profile=rankList[position]
        holder.itemView.apply {
            userRankCoin.setText(user.coins.toString())
            userRankName.setText(user.name.toString())
            userRank.setText(String.format("#%d", position + 1))
        }
            Glide.with(context)
                .load(user.profile)
                .override(120,120)
                .circleCrop()
                .placeholder(R.drawable.user)
                .into(holder.itemView.Donegrp)
          database=FirebaseDatabase.getInstance().getReference()
        fauth= FirebaseAuth.getInstance()
            holder.itemView.setOnClickListener {
//                database.child("Profile").child(user.uid.toString()).get().addOnSuccessListener {
//                    friend = it.getValue(Profile::class.java)!!
//                }
//                database.child("Profile").child(fauth.currentUser!!.uid).child("friend")
//                    .child(user.uid.toString()).setValue("XYZ").addOnSuccessListener {
//                        Toast.makeText(context, "U are friends ", Toast.LENGTH_SHORT)
//                            .show()
//                    }
                var intent= Intent(context,friendProfile::class.java)
                intent.putExtra("name",user.name.toString())
                intent.putExtra("uid",user.uid.toString())
               context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
         return rankList.size
    }
}