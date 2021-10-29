package com.example.whatsup

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.whatsup.databinding.ActivityMessageBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.android.synthetic.main.group.view.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.jakewharton.rxbinding.widget.RxTextView
import rx.functions.Action1
import java.util.concurrent.TimeUnit
class MessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessageBinding
    private lateinit var database:DatabaseReference
    private lateinit var storage:StorageReference
    private lateinit var database2:DatabaseReference
    private lateinit var fauth:FirebaseAuth
    private lateinit var senderId: String
    private lateinit var receiverId:String
    private lateinit var receiverName:String
    private var isTyping:Boolean=false
    private var requestCode:Int=1
    private lateinit var permissions:Array<String>
    private lateinit var handler:Handler
    private lateinit var senderRoom: String
    private lateinit var receiverRoom:String
    private lateinit var messages:ArrayList<Message>
    private lateinit var searchmessages:ArrayList<Message>
    private lateinit var msgAdapter: messageAdapter
    private   val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    private  var selectedImage:Uri?=null
    private  var image:String?=null
    private lateinit var phone:String
    private var value:Int=0
    private val requestCall = 1
    private  var caption:String?=null
    private final var delay:Long=1500
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.searchMessage.invalidate()
        database2=FirebaseDatabase.getInstance().getReference()
        fauth= FirebaseAuth.getInstance()
        senderId=fauth.currentUser!!.uid
        image=intent.getStringExtra("profile").toString()
        receiverId= intent.getStringExtra("uid").toString()
        receiverName=intent.getStringExtra("name").toString()
        phone=intent.getStringExtra("phone").toString()
        database2.child("Online_Status").get().addOnSuccessListener {
            var isOnline=it.child(receiverId).value
            if(isOnline==null){
                binding.onlineStatus.setText("Offline")
            }
            else{
                binding.onlineStatus.setText(isOnline.toString())
            }
        }
        senderRoom=senderId+receiverId
        receiverRoom=receiverId+senderId
        messages= arrayListOf()
        var linearLayoutmanager=LinearLayoutManager(this)
        linearLayoutmanager.stackFromEnd=true
        binding.recyclerView2.layoutManager=linearLayoutmanager
        msgAdapter= messageAdapter(this,messages,senderRoom,receiverRoom)
        binding.recyclerView2.adapter=msgAdapter
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title=null
            Glide.with(this@MessageActivity)
                .load(image)
                .override(120, 120)
                .circleCrop()
                .placeholder(R.drawable.user)
                .into(userChatImage)
            binding.title.text=receiverName

        }
        permissions= arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO)
        binding.toolbar.setOnClickListener{
            var intent=Intent(this,friendProfile::class.java)
            intent.putExtra("uid",receiverId)
            intent.putExtra("name",receiverName)
            intent.putExtra("profile",image)
            startActivity(intent)
        }
        Toast.makeText(this,receiverName,Toast.LENGTH_SHORT).show()
        database=FirebaseDatabase.getInstance().getReference().child("Chats").child(senderRoom).child("messages")
        database.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                messages.clear()
                for(dataSnapshot in snapshot.children){
                    var message=dataSnapshot.getValue(Message::class.java)
                    if (message != null) {
                        messages.add(message)
                        if(message.senderId.equals(receiverId))
                        database.child(message.msgId.toString()).child("seen").setValue(true)
                    }
                }
                msgAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MessageActivity,"No Message sent",Toast.LENGTH_SHORT).show()
            }
        })
        binding.fileSendBtn.setOnClickListener {
            var intent=Intent(this,ImageSelect::class.java)
            intent.putExtra("senderRoom",senderRoom)
            startActivityForResult(intent,1)
        }
        binding.msgBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                database2.child("Chats").child(senderRoom).child(fauth.currentUser!!.uid).setValue("typing..")
            }
            var timer=Timer()

            override fun afterTextChanged(s: Editable?) {
                if(!isTyping){
                    isTyping=true
                }
                timer.cancel()
                timer= Timer()
                timer = Timer()
                timer.schedule(
                    object : TimerTask() {
                        override fun run() {
                            isTyping = false
                            database2.child("Chats").child(senderRoom).child(fauth.currentUser!!.uid).setValue(null)
                        }
                    },
                    delay
                )
            }
        })
        database2.child("Chats").child(receiverRoom).get().addOnSuccessListener {
            var typing=it.child(receiverId).value
        }
        binding.msgSentBtn.setOnClickListener {
            var newMsg=binding.msgBox.text
            if(newMsg==null){
                Toast.makeText(this,"Type something",Toast.LENGTH_SHORT).show()
            }
            else{
                var date=Date()
                binding.msgBox.setText("")
                var rand=database.push().key
                var message=Message(null,newMsg.toString().trim(),senderId,receiverName,System.currentTimeMillis().toString(),false,rand)
                if (rand != null) {
                    database2.child("Chats").child(senderRoom).child("messages").child(rand).setValue(message).addOnSuccessListener {
                        database2.child("Chats").child(receiverRoom).child("messages").child(rand).setValue(message).addOnSuccessListener{
                            Toast.makeText(this,"Message Sent",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }


    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.message, menu)


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.call-> {
                makePhoneCall()
                Toast.makeText(applicationContext, "Call", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.video -> {
                if(isPermissionGranted()){
                    var intent=Intent(this@MessageActivity,VideoCall::class.java)
                    intent.putExtra("FriendUid",receiverId)
                    intent.putExtra("FriendImage",image)
                    intent.putExtra("FriendName",receiverName)
                    startActivity(intent)
                }
                else{
                    askPermission()
                }


                return true
            }
            R.id.mediaBox -> {
                var intent= Intent(this,friendsFragment::class.java)
                startActivity(intent)
                return true
            }
            R.id.mute -> {
                var intent= Intent(this,friendsFragment::class.java)
                startActivity(intent)
                return true
            }
            R.id.clear -> {
                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setMessage("Do you want to Clear all chats?")
                    .setCancelable(false)
                    .setPositiveButton("Proceed", DialogInterface.OnClickListener {
                            dialog, id ->
                        database2.child("Chats").child(senderRoom).child("messages").setValue(null).addOnSuccessListener {
                            Toast.makeText(this,"Chats Clear",Toast.LENGTH_SHORT).show()
                        }
                    })
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                            dialog, id -> dialog.cancel()
                        Toast.makeText(this," No Chats Clear",Toast.LENGTH_SHORT).show()
                    })

                val alert = dialogBuilder.create()
                alert.show()
                return true
            }
            R.id.search->{
                supportActionBar?.hide()
                binding.searchMessage.visibility=View.VISIBLE
                binding.searchMessage.isSubmitButtonEnabled=true
                performSearch()
                return true
            }
            R.id.backGround->{
                return true
            }
            R.id.reply->{
                Toast.makeText(this,"Reply",Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.favourite->{
                Toast.makeText(this,"Add to Favourite",Toast.LENGTH_SHORT).show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun performSearch() {
        binding.searchMessage.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                search(newText)
                return true
            }
        })
    }
    private fun search(text: String?) {
         searchmessages = arrayListOf()

        text?.let {
            messages.forEach { message ->
                if (message.msg?.contains(text, true) == true ) {
                    searchmessages.add(message)
                }
            }
            if (searchmessages.isEmpty()) {
                Toast.makeText(this, "No match found!", Toast.LENGTH_SHORT).show()
            }
            updateRecyclerView()
        }
    }
    private fun makePhoneCall() {
        if (true) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CALL_PHONE),
                    requestCall
                )
            } else {
                val dial = "tel:$phone"
                startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
            }
        } else {
            Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCall) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall()
            }
            else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateRecyclerView() {
        binding.recyclerView2.apply {
            msgAdapter.msgList=searchmessages
            msgAdapter.notifyDataSetChanged()
        }
    }

    private fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) : Void? {
        super.onActivityResult(requestCode, resultCode, data)
        Toast.makeText(this,"Activity",Toast.LENGTH_SHORT).show()
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                 selectedImage= intent.getStringExtra("SelectedImage")?.toUri()!!
                caption=intent.getStringExtra("Caption").toString()
                Toast.makeText(this,"sending Image",Toast.LENGTH_SHORT).show()
                sendImage()
            }
        }
        return null
    }

    private fun sendImage() {
        var rand=database.push().key.toString()
        storage= FirebaseStorage.getInstance().getReference().child(senderRoom).child(rand.toString())
        if(selectedImage!=null){
            Toast.makeText(this,"Not null",Toast.LENGTH_SHORT).show()
                storage.putFile(selectedImage!!).addOnCompleteListener(OnCompleteListener {
                    if(it.isSuccessful){
                        storage.downloadUrl.addOnSuccessListener {
                            val message=Message(it.toString(),caption,fauth.currentUser!!.uid,fauth.currentUser!!.uid,System.currentTimeMillis().toString(),false,rand)
                            database.child("Chats").child(senderRoom).child("messages").child(rand).setValue(message).addOnSuccessListener {
                              database.child("Chats").child(receiverRoom).child(rand).setValue(message).addOnSuccessListener {
                                val intent=Intent()
                                intent.putExtra("SelectedImage",selectedImage)
                                Toast.makeText(this,"Image Send",Toast.LENGTH_SHORT).show()
                                finish()
                              }
                            }
                        }
                    }
                })
            }
    }
    fun Activity.hideSoftKeyboard(editText: EditText){
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(editText.windowToken, 0)
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