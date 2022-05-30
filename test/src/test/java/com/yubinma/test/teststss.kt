package com.yubinma.test

import cn.leancloud.core.LeanCloud
import com.chaoshan.data_center.friend.*
import org.junit.Test

class teststss {
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

    @Test
    fun getUerDaoByNameOrId() {
        GetAllUer.getUerDaoByNameOrId(object : GetAllDataListener {
            override fun success(friendList: List<Friend>) {
                print(friendList.toString())
            }

            override fun fail() {

            }
        }, "13048806641", " ")

    }

}