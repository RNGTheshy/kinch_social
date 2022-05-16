package com.chaoshan.data_center.friend

import cn.leancloud.LCObject

interface GetAllDataListener {
    fun success(friendList: List<Friend>)
    fun fail()
}