package com.example.whatsup

import android.app.Notification
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.whatsup.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
   private lateinit var binding: FragmentHomeBinding
    private  lateinit var fauth:FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var database1: DatabaseReference
    private lateinit var categories:ArrayList<Category>
    private lateinit var adapter:categoryAdapter
    private lateinit var dialog:ProgressDialog
    private lateinit var timer:CountDownTimer
    private lateinit var uid:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeBinding.inflate(inflater,container,false)

        dialogShow()
        fetchData()

        binding.invite.setOnClickListener {
            uid="https://www.google.com/imgres?imgurl=https%3A%2F%2Fcdn.pixabay.com%2Fphoto%2F2018%2F10%2F30%2F16%2F06%2Fwater-lily-3784022__480.jpg&imgrefurl=https%3A%2F%2Fpixabay.com%2Fimages%2Fsearch%2Fgrowth%2F&tbnid=6pLShGhEUdQAhM&vet=12ahUKEwi2s7z11qbzAhXqn0sFHWjEAQIQMygJegUIARDcAQ..i&docid=NM3-gxpf-Y0onM&w=704&h=480&q=images&ved=2ahUKEwi2s7z11qbzAhXqn0sFHWjEAQIQMygJegUIARDcAQ"
            var uri=Uri.parse(uid)
            var intent=Intent()
            intent.setAction(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT,uid.toString()+" My new application")
            intent.putExtra(Intent.EXTRA_TITLE, "Recharge2me");
            intent.setType("text/plain")
            startActivity(Intent.createChooser(intent, "Share Link"));
        }
        return binding.root
    }
    private fun fetchData(){
        fauth= FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance().getReference()
        categories= arrayListOf()
        binding.categoryView.layoutManager= GridLayoutManager(context,2)
        adapter= context?.let { categoryAdapter(it,categories) }!!
        binding.categoryView.adapter=adapter
        database1= FirebaseDatabase.getInstance().getReference()
        database1.child("Category").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dialog.cancel()
                categories.clear()
                for(catSnapshot in snapshot.children){
                    val category=catSnapshot.getValue(Category::class.java)
                    categories.add(category!!)
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun dialogShow(){
        dialog= ProgressDialog(context)
        dialog.setMessage("Loading Catogeries...")
        dialog.setCancelable(true)
        dialog.show()
        if(dialog.isShowing){
            timer = object: CountDownTimer(10000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                }
                override fun onFinish() {
//                    dialog.dismiss()
//                    Toast.makeText(context,"Network slow down ",Toast.LENGTH_SHORT).show()
//                    binding.tryAgain.setVisibility(View.VISIBLE)
//                    binding.tryAgain.setOnClickListener {
//                        dialog.show()
//                        fetchData()
//                        binding.tryAgain.setVisibility(View.INVISIBLE)

                }
            }
            timer.start()
        }
    }
}