package com.chaoshan.data_center.blindboxpersondata

import android.widget.ImageView
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blind_box_person")
data class BlindBoxPerson(
    var name: String = "",
    val phone: String? = null,
    val boxNum: Int = 0,
    var photo: ImageView? = null
){
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0L
}
