package  com.example.whatsup

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.whatsup.databinding.ActivityGroupChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_group_chat.*
import kotlinx.android.synthetic.main.activity_message.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GroupChat : AppCompatActivity() {
    private lateinit var binding: ActivityGroupChatBinding
    private lateinit var database: DatabaseReference
    private lateinit var fauth: FirebaseAuth
    private lateinit var senderId: String
    private lateinit var groupUid:String
    private  var receiverName:String="GroupName"
    private var senderName:String="abcd"
    private  var senderRoom: String="a"
    private  var receiverRoom:String="b"
    private lateinit var messages:ArrayList<Message>
    private lateinit var msgGrpAdapter: GroupChatAdapter
    private   val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGroupChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database= FirebaseDatabase.getInstance().getReference()
        fauth= FirebaseAuth.getInstance()
        senderId=fauth.currentUser!!.uid
        groupUid= intent.getStringExtra("GroupUid").toString()
        database.child("Groups").child(groupUid).get().addOnSuccessListener {
            var image=it.child("GroupImage").value.toString()
            receiverName = it.child("GroupName").value.toString()
            Toast.makeText(this, receiverName, Toast.LENGTH_SHORT).show()
            messages = arrayListOf()
            var linearLayoutmanager=LinearLayoutManager(this)
            linearLayoutmanager.stackFromEnd=true
            binding.msgGrpView.layoutManager =linearLayoutmanager
            msgGrpAdapter = GroupChatAdapter(this, messages, senderRoom, receiverRoom)
            binding.msgGrpView.adapter = msgGrpAdapter
            setSupportActionBar(binding.Grptoolbar)
            supportActionBar?.apply {
                title=null
                Glide.with(this@GroupChat)
                    .load(image)
                    .override(120, 120)
                    .circleCrop()
                    .placeholder(R.drawable.user)
                    .into(userGrpChatImage)
                binding.Grptitle.text=receiverName
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
                Grptoolbar.inflateMenu(R.menu.message)
            }
            binding.Grptoolbar.setOnClickListener {
                var intent=Intent(this,GrpPage::class.java)
                intent.putExtra("GroupUid",groupUid)
                startActivity(intent)
            }

            Toast.makeText(this, receiverName, Toast.LENGTH_SHORT).show()
            database.child("Groups").child(groupUid).child("GroupChat").child("messages")
                .addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        messages.clear()
                        for (dataSnapshot in snapshot.children) {
                            var message = dataSnapshot.getValue(Message::class.java)
                            if (message != null) {
                                messages.add(message)
                            }
                        }
                        msgGrpAdapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@GroupChat, "No Message sent", Toast.LENGTH_SHORT).show()
                    }

                })
            binding.msgSentGrpBtn.setOnClickListener {
                var newMsg = binding.msgGrpBox.text.toString().trim()
                database.child("Profile").child(senderId).get().addOnSuccessListener {
                    senderName=it.child("name").getValue().toString().trim()
                    if (newMsg == "" || newMsg==" ") {
                        Toast.makeText(this, "Type something", Toast.LENGTH_SHORT).show()
                        binding.msgGrpBox.setText("")
                    } else {
                        var date = Date()
                        binding.msgGrpBox.setText("")
                        var rand = database.push().key
                        var message = Message(null,newMsg.toString().trim(), senderId,senderName,
                            System.currentTimeMillis().toString(),false,rand)

                        if (rand != null) {
                            database.child("Groups").child(groupUid).child("GroupChat")
                                .child("messages").child(rand).setValue(message).addOnSuccessListener {
                                    Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show()
                                }
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
                var intent= Intent(this,friendsFragment::class.java)
                startActivity(intent)
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}