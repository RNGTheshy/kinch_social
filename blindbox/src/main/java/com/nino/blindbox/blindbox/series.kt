package com.nino.blindbox.blindbox

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

data class series (
    val name : String ="",
    var alreadyOwnedBoxNum : Int = 0 ,
    var allCollected : Boolean = false,
    val photoId : Int = 0,
    val boxes: LinkedList<box>
)