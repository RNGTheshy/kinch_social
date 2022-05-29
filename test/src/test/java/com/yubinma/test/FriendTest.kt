package com.yubinma.test

import cn.leancloud.LCLogger
import cn.leancloud.LCObject
import cn.leancloud.core.LeanCloud
import com.chaoshan.data_center.friend.*
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class FriendTest {
    init {
        LeanCloud.initialize(
            "WFB1URKdIJqueBEjLd0P0xoy-gzGzoHsz",
            "9uuBkty0jB2T7HXyqDWmLOVj",
            "https://wfb1urkd.lc-cn-n1-shared.com"
        )
    }

    @Test
    fun getAllUerDao() {
        GetAllUer.getAllUerDao(object : GetAllDataListener {
            override fun success(friendList: List<Friend>) {
                print(friendList.toString())
            }

            override fun fail() {
            }
        })
    }

    @Test
    fun getAllSentData() {
        GetAllUer.getSendFriendData("627f824e7a6d3118ac0c015f", object : GetSentFriendCallBack {
            override fun getSuccess(friend: List<SentFriend>) {
                print(friend.toString())
            }
        })



    }

}