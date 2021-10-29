package com.example.whatsup

import android.text.Editable
import com.google.firebase.firestore.ServerTimestamp

class withDraw(
    val uid:String?,
    @ServerTimestamp
    val date:String?,
    val phone: Long?,
    val requestBy:String?,
    val coins:Long?
    )