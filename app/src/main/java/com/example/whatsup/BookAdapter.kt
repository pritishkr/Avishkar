package com.example.whatsup

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.bookmark_ques.view.*

class BookAdapter(private var context:Context,private var bookList:ArrayList<Questions>)
    :RecyclerView.Adapter<BookAdapter.BookViewHolder>(){
    class BookViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        var view=LayoutInflater.from(context).inflate(R.layout.bookmark_ques,parent,false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        var question:Questions=bookList[position]
        val ques="Q.${position+1} "+question.ques.toString()
        val opt1="1. "+question.opt1.toString()
        val opt2="2. "+question.opt2.toString()
        val opt3="3. "+question.opt3.toString()
        val opt4="4. "+question.opt4.toString()
        val ans="Answer-> "+question.ans.toString()
        holder.itemView.apply {
            bookQues.setText(ques)
            bookOpt1.setText(opt1)
            bookOpt2.setText(opt2)
            bookOpt3.setText(opt3)
            bookOpt4.setText(opt4)
            answer.setText(ans)

        }
        holder.itemView.setOnLongClickListener{
            Toast.makeText(context,question.id,Toast.LENGTH_SHORT).show()
//            it.setBackgroundResource(R.drawable.selectedques)
            holder.itemView.apply {
                quesCard.setBackgroundColor(resources.getColor(R.color.ltBlack))
            }
            var intent= Intent(context,SendQues::class.java)
            intent.putExtra("questionId",question.id.toString())
            context.startActivity(intent)
            true
        }
    }

    override fun getItemCount(): Int {
       return bookList.size
    }
}