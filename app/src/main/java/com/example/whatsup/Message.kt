package com.example.whatsup

import com.google.firebase.firestore.ServerTimestamp
import java.sql.Timestamp

class Message{
    var image:String?=null
    var msg:String?=null
    var senderId:String?=null
     @ServerTimestamp
     var date:String?=null
     var senderName:String?=null
    var seen:Boolean=false
    var msgId:String?=null

    constructor(){}
     constructor(image:String?,msg:String?,senderId:String?,senderName:String?,date:String?,seen:Boolean,msgId:String?)
     {
         this.image=image
         this.msg=msg
         this.senderId=senderId
         this.senderName=senderName
         this.date=date
         this.seen=seen
         this.msgId=msgId
     }
}