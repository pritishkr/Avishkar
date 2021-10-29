package com.example.whatsup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsup.databinding.ActivityBookmarkBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_inputques.*

class Bookmark : AppCompatActivity() {
    private lateinit var binding:ActivityBookmarkBinding
    private lateinit var database:DatabaseReference
    private lateinit var fauth:FirebaseAuth
    private lateinit var quesList:ArrayList<Questions>
    private lateinit var bookAdapter: BookAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database=FirebaseDatabase.getInstance().getReference()
        fauth= FirebaseAuth.getInstance()
        quesList= arrayListOf()
        val catId=intent.getStringExtra("catId").toString()
        Toast.makeText(this,catId,Toast.LENGTH_SHORT).show()
        bookAdapter= BookAdapter(this,quesList)
        binding.bookView.adapter=bookAdapter
        binding.bookView.layoutManager=LinearLayoutManager(this)
        database.child("BookMark").child(fauth.currentUser!!.uid).child(fauth.currentUser!!.uid).addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                quesList.clear()
                for(dataSnapshot in snapshot.children){
                    var ques=dataSnapshot.getValue().toString()
                    Toast.makeText(this@Bookmark,ques,Toast.LENGTH_SHORT).show()
                    database.child("Questions").child(catId).child(ques).get().addOnSuccessListener {
                        var newQues:Questions?=it.getValue(Questions::class.java)
                        if (newQues != null) {
                            quesList.add(newQues)
                        }
                    }
                }
                Handler().postDelayed({
//                    Toast.makeText(this@Bookmark,quesList.size,Toast.LENGTH_SHORT).show()
                    bookAdapter.notifyDataSetChanged()
                }, 2000)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bookmark,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.requests -> {
                var intent= Intent(this,inputques::class.java)
                startActivity(intent)
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}