package com.example.kinch_home

import cn.leancloud.core.LeanCloud
import com.chaoshan.data_center.friend.Friend
import com.chaoshan.data_center.friend.GetAllDataListener
import com.chaoshan.data_center.friend.GetAllMyFirendCallBack
import com.chaoshan.data_center.friend.GetAllUer
import com.chaoshan.data_center.togetname.getPersonal_data.getplace
import org.junit.Test

/**
 *  定位测试
 */
class LocateTest{
    private var id: String? = "62935cede3c0de5dcab217be" //本人的ID

    init {
        LeanCloud.initialize(
            "WFB1URKdIJqueBEjLd0P0xoy-gzGzoHsz",
            "9uuBkty0jB2T7HXyqDWmLOVj",
            "https://wfb1urkd.lc-cn-n1-shared.com"
        )
    }


    @Test
    fun showMyLocation() {
        getplace(id) { longitude, latitude -> //获取纬度和经度
            println("经度$longitude")
            println("纬度$latitude")
        }
    }

    @Test
    fun showAllFriendsLocation() {
        GetAllUer.getAllFriend( //获取所有朋友
            id!!,
            object : GetAllMyFirendCallBack {
                override fun success(list: List<String>) {
                    if (list.isNotEmpty()) {
                        GetAllUer.getFriendDao(object : GetAllDataListener {
                            override fun success(friendList: List<Friend>) { //获得所有朋友的列表
                                for (friend in friendList) {
                                    println("朋友ID：" + friend.id) //朋友ID
                                    getplace(friend.id) { longitude, latitude ->
                                        println("经度:$longitude") //朋友定位的经度
                                        println("纬度:$latitude") //朋友定位的纬度
                                    }
                                }
                            }
                            override fun fail() {
                            }
                        }, list)
                    }
                }
            })
    }

    @Test
    fun locateFriend() {
        val friendId = "62925c660534fd5ba0ac2652" //朋友ID
        println("朋友ID：$friendId")
        getplace(friendId) { longitude, latitude ->
            println("经度:$longitude") //朋友定位的经度
            println("纬度:$latitude") //朋友定位的纬度
        }
    }
}