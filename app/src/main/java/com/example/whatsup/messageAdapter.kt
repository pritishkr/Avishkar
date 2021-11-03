package com.example.whatsup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.bumptech.glide.Glide
import com.example.whatsup.databinding.ItemRecieveBinding
import com.example.whatsup.databinding.ItemSentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.android.synthetic.main.item_recieve.view.*
import kotlinx.android.synthetic.main.item_sent.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat

class messageAdapter (private var context:Context,var msgList:ArrayList<Message>,private var senderRoom:String,private var receiverRoom:String)
    : Adapter<RecyclerView.ViewHolder>(){
    private var date: DateFormat = SimpleDateFormat("hh:mm a")
      var clickedMsg:ArrayList<Int>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType==1){
            var view=LayoutInflater.from(context).inflate(R.layout.item_sent,parent,false)
            return sentViewHolder(view)
        }
        else{
            var view=LayoutInflater.from(context).inflate(R.layout.item_recieve,parent,false)
            return recieveViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage=msgList[position]
        clickedMsg?.clear()
        if(holder::class.java==sentViewHolder::class.java){
            val viewHolder=holder as sentViewHolder
            if(currentMessage.image==null)
            {
                holder.itemView.ImageSent.visibility=View.GONE
            }
            else {
                holder.itemView.ImageSent.visibility=View.VISIBLE
                Glide.with(context)
                    .load(currentMessage.image)
                    .override(1000, 1000)
                    .placeholder(R.drawable.user)
                    .into(holder.itemView.ImageSent)
            }
            holder.sentMessage.text=currentMessage.msg
            holder.itemView.msgSentTime.setText(date.format(currentMessage.date.toString().toLong()).toString())
            if(currentMessage.seen.toString().toBoolean())
            {
                holder.itemView.sendMsg.setColorFilter(R.color.darkBlue)
            }
            else{
                holder.itemView.sendMsg.setColorFilter(R.color.white)
            }

            holder.itemView.setOnLongClickListener{
                clickedMsg?.add(position)
                return@setOnLongClickListener true
            }
        }
        else{
            val viewHolder=holder as recieveViewHolder
            holder.recieveMessage.text=currentMessage.msg
        }
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    override fun getItemViewType(position: Int): Int {
      var message=msgList[position]
        if(FirebaseAuth.getInstance().currentUser!!.uid.equals(message.senderId)){
            return 1
        }
        else{
            return 2
        }
    }

    class sentViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val sentMessage=itemView.msgSentText
        var binding=ItemSentBinding.bind(itemView)
    }
    class recieveViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val recieveMessage=itemView.msgRecieveBox
        var binding=ItemRecieveBinding.bind(itemView)
    }

}