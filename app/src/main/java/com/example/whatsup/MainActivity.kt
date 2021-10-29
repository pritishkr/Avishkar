package com.example.whatsup

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.whatsup.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private  lateinit var fauth:FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var database:DatabaseReference
    private lateinit var database1:DatabaseReference
    private lateinit var categories:ArrayList<Category>
    private lateinit var adapter:categoryAdapter
    private var lastInteractionTime:Int?=null
    var isScreen:Boolean=false
    private lateinit var friendsList:ArrayList<Profile>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        fauth= FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance().getReference().child("Online_Status")
        database.child(fauth.currentUser!!.uid).setValue("Online")
        database.child(fauth.currentUser!!.uid).onDisconnect().setValue("Online")
        val homeFragment = HomeFragment()
        val LeaderBoardFragment = LeaderBoardFragment()
        val profileFragment = profileFragment()
        val walletFragment=WalletFragment()
        setCurrentFragment(homeFragment)
       binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> setCurrentFragment(homeFragment)
                R.id.rank-> setCurrentFragment(LeaderBoardFragment)
                R.id.user -> setCurrentFragment(profileFragment)
                R.id.wallet->setCurrentFragment(walletFragment)

            }
            true
       }
    }
    fun setCurrentFragment(fragment: Fragment)=
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment,fragment)
                commit()
            }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.wallet -> {
                var intent=Intent(this,inputques::class.java)
                startActivity(intent)
                return true
            }
            R.id.settings -> {
                var intent=Intent(this,FirstPage::class.java)
                startActivity(intent)
                return true
            }
            R.id.friends -> {
                var intent=Intent(this,friendsFragment::class.java)
                startActivity(intent)
                return true
            }
            R.id.newgroup-> {
                var intent=Intent(this,grpForm::class.java)
                startActivity(intent)
                return true
            }
            R.id.group-> {
                var intent=Intent(this,GroupsFragment::class.java)
                startActivity(intent)
                return true
            }
            
            else -> super.onOptionsItemSelected(item)
        }
    }
}