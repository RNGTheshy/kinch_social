package com.yubinma.fishprawncrab.person

// 庄家数据类型
data class Bookmaker(
    override val name: String = "",
    override val id: String = "",
    override val phone: String? = null,
    override val beans: Double = 0.0
) : Person(name, id, phone, beans)

