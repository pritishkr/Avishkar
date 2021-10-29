package com.example.whatsup

import com.google.firebase.firestore.ServerTimestamp

data class Status (
     val statusUid:String?=null,
     val friendUid:String?=null,
     val friendName:String?=null,
     val caption:String?=null,
     @ServerTimestamp
     var date:String?=null
)