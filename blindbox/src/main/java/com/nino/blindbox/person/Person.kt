package com.nino.blindbox.person

import android.widget.ImageView

open class Person(
    open var name: String = "",
    open val id: String = "",
    open val phone: String? = null,
    open val boxNum: Int = 0,
    open var photo : ImageView? = null
)