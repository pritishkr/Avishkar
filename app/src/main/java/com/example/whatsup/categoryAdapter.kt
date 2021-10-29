package com.example.whatsup

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_category.view.*

class categoryAdapter (private val context:Context, private val category:ArrayList<Category>)
    :RecyclerView.Adapter<categoryAdapter.CategoryViewHolder>(){

class CategoryViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
         val view=LayoutInflater.from(context).inflate(R.layout.item_category,parent,false)
         return CategoryViewHolder(view)
     }

     override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val cate:Category=category[position]
         holder.itemView.apply {
             categoryName.text=cate.categoryName.toString()
         }

         Glide.with(context).load(cate.categoryImage).override(200,200).into(holder.itemView.categoryImage)

         holder.itemView.setOnClickListener{
             var intent= Intent(context,startActivity::class.java)
             intent.putExtra("catId",cate.categoryId.toString())
             context.startActivity(intent)
         }
     }

     override fun getItemCount(): Int {
       return category.size
     }
 }