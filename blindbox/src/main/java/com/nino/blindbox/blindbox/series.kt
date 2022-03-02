package com.nino.blindbox.blindbox

import android.widget.ImageView

data class series (
    val name : String ="",
    var alreadyOwnedBoxNum : Int = 0 ,
    var allCollected : Boolean = false,
    val photo : ImageView? = null
)