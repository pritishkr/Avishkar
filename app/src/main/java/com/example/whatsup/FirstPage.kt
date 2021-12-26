package com.example.whatsup

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsup.databinding.ActivityFirstPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_first_page.*
import kotlinx.android.synthetic.main.activity_search_friends.*
import kotlinx.android.synthetic.main.fragment_friends.*
import java.util.jar.Manifest

class FirstPage : AppCompatActivity() {
    private lateinit var binding: ActivityFirstPageBinding
    private lateinit var database:DatabaseReference
    private lateinit var fauth:FirebaseAuth
    private lateinit var selectedImage:Uri
    private lateinit var storage: StorageReference
    private lateinit var searchFriend:ArrayList<Profile>
    private lateinit var searchGroup:ArrayList<Group>
    private lateinit var searchStatus:ArrayList<Status>
    private lateinit var searchingList:ArrayList<Profile>
    private lateinit var searchingListGroup:ArrayList<Group>
    private lateinit var searchingListStatus:ArrayList<Status>
    private lateinit var groupsFragment: GroupsFragment
    private lateinit var statusFragment: StatusFragment
    private  var currentFragment:Int=1
    private  var image:String?=null
    private lateinit var permissions:Array<String>
    private lateinit var friendsFragment: friendsFragment
    private var requestCode:Int=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        database=FirebaseDatabase.getInstance().getReference()
        fauth= FirebaseAuth.getInstance()
permissions= arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO,android.Manifest.permission.READ_CONTACTS)
            database.child("Online_Status").child(fauth.currentUser!!.uid).setValue("Online")
            database.child("Online_Status").child(fauth.currentUser!!.uid).onDisconnect().setValue("Offline")
        if(isPermissionGranted())
        {
            friendsFragment=friendsFragment()
            groupsFragment=GroupsFragment()
            statusFragment=StatusFragment()
            setCurrentFragment(friendsFragment)
            binding.bottomNavigationView.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.friends ->
                    {
                        setCurrentFragment(friendsFragment)

                        currentFragment=1
                    }
                    R.id.groups ->
                    {
                        setCurrentFragment(groupsFragment)

                        currentFragment=2
                    }
                    R.id.status->
                    {
                        setCurrentFragment(statusFragment)

                        currentFragment=3
                    }
                    R.id.quiz->
                    {
                        var intent=Intent(this,MainActivity::class.java)
                        startActivity(intent)
                    }
                }
                true
            }
        }
        else
        {
            askPermission()
        }
        binding.camera.setOnClickListener {

        }

    }
    private fun performSearch() {
        binding.friendsSearch.queryHint="Search Here.."
        binding.friendsSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(currentFragment==1){
                    searchingList=friendsFragment.friendList
                    search(query)
                }
                else if(currentFragment==2)
                {
                    searchingListGroup=groupsFragment.grpList
                    searchGroup(query)
                }
                else{
                    searchingListStatus=statusFragment.statusList
                    searchStatus(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(currentFragment==1){
                    search(newText)
                }
                else if(currentFragment==2)
                {
                    searchGroup(newText)
                }
                else{
                    searchStatus(newText)
                }
                return true
            }
        })
    }
    private fun search(text: String?) {
        searchFriend= arrayListOf()

        text?.let {
            friendsFragment.friendList.forEach { message ->
                if (message.name?.contains(text, true) == true ) {
                    searchFriend.add(message)
                }
            }
            if (searchFriend.isEmpty()) {
                Toast.makeText(this, "No match found!", Toast.LENGTH_SHORT).show()
            }
            updateRecyclerView()
        }
    }
    private fun updateRecyclerView() {
        friendsFragment.friendsView.apply {
            friendsFragment.friendAdapter.rankList=searchFriend
            friendsFragment.friendAdapter.notifyDataSetChanged()
        }
    }

    private fun searchGroup(text: String?) {
        searchFriend= arrayListOf()

        text?.let {
            groupsFragment.grpList!!.forEach { message ->
                if (message.GroupName?.contains(text, true) == true ) {
                    searchGroup.add(message)
                }
            }
            if (searchFriend.isEmpty()) {
                Toast.makeText(this, "No match found!", Toast.LENGTH_SHORT).show()
            }
            updateRecyclerViewGroup()
        }
    }
    private fun updateRecyclerViewGroup() {
        friendsFragment.friendsView.apply {
            groupsFragment.grpAdapter.grpList=searchGroup
            groupsFragment.grpAdapter.notifyDataSetChanged()
        }
    }
    private fun searchStatus(text: String?) {
        searchFriend= arrayListOf()

        text?.let {
            statusFragment.statusList.forEach { message ->
                if (message.friendName?.contains(text, true) == true ) {
                    searchStatus.add(message)
                }
            }
            if (searchFriend.isEmpty()) {
                Toast.makeText(this, "No match found!", Toast.LENGTH_SHORT).show()
            }
            updateRecyclerViewStatus()
        }
    }
    private fun updateRecyclerViewStatus() {
        friendsFragment.friendsView.apply {
            statusFragment.statusAdapter.statusList=searchStatus
            statusFragment.statusAdapter.notifyDataSetChanged()
        }
    }
    fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.firstFragment,fragment)
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
                var intent=Intent(this,LeaderBoardFragment::class.java)
                startActivity(intent)
                return true
            }
            R.id.friends -> {
                var intent=Intent(this,FirstPage::class.java)
                startActivity(intent)
                return true
            }
            R.id.newgroup-> {
                var intent=Intent(this,grpForm::class.java)
                startActivity(intent)
                return true
            }
            R.id.group-> {
                var intent=Intent(this,FirstPage::class.java)
                startActivity(intent)
                return true
            }
            R.id.search->{
                supportActionBar?.hide()
                performSearch()
                binding.friendsSearch.visibility= View.VISIBLE
                binding.friendsSearch.isSubmitButtonEnabled=true

                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun askPermission(){
        ActivityCompat.requestPermissions(this,permissions,requestCode)
    }
    private fun isPermissionGranted():Boolean{
        for(permission in permissions){
            if(ActivityCompat.checkSelfPermission(this,permission)!=PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }


}