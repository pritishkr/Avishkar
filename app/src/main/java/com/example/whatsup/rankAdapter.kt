package com.example.whatsup

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.row_leaderboard.view.*

class rankAdapter(private var context: Context, private var rankList:ArrayList<Profile>)
    :RecyclerView.Adapter<rankAdapter.rankViewHolder>(){
    class rankViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)

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

            holder.itemView.setOnClickListener {
                var intent= Intent(context,friendProfile::class.java)
                intent.putExtra("uid",user.uid.toString())
               context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
         return rankList.size
    }
}