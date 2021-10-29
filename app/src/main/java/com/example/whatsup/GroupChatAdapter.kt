package com.example.whatsup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsup.databinding.ItemRecieveBinding
import com.example.whatsup.databinding.ItemSentBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.item_recieve.view.*
import kotlinx.android.synthetic.main.item_recieve.view.msgRecieveBox
import kotlinx.android.synthetic.main.item_sent.view.*
import kotlinx.android.synthetic.main.itemreciecegrp.view.*

class GroupChatAdapter (private var context: Context, private var msgList:ArrayList<Message>, private var senderRoom:String, private var receiverRoom:String)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType==1){
            var view= LayoutInflater.from(context).inflate(R.layout.item_sent,parent,false)
            return sentGrpViewHolder(view)
        }
        else{
            var view= LayoutInflater.from(context).inflate(R.layout.itemreciecegrp,parent,false)
            return recieveGrpViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder::class.java==sentGrpViewHolder::class.java){
            val currentMessage=msgList[position]
            val viewHolder=holder as sentGrpViewHolder
            holder.sentMessage.text=currentMessage.msg
        }
        else{
            val currentMessage=msgList[position]
            val viewHolder=holder as recieveGrpViewHolder
            holder.recieveMessage.text=currentMessage.msg
            holder.itemView.msgSenderName.text=currentMessage.senderName
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

    class sentGrpViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val sentMessage=itemView.msgSentText
        var binding= ItemSentBinding.bind(itemView)
    }
    class recieveGrpViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val recieveMessage=itemView.msgRecieveBox
        var binding= ItemRecieveBinding.bind(itemView)
    }
}