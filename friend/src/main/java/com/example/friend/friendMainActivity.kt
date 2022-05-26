package com.example.friend

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chaoshan.data_center.friend.Friend
import com.chaoshan.data_center.friend.GetAllDataListener
import com.chaoshan.data_center.friend.GetAllUer
import com.example.friend.adapter.friendItemAdapter
import java.util.*

class friendMainActivity : AppCompatActivity() {
    private var friends = listOf<Friend>()
    private var mRecycleView: RecyclerView? = null
    private var mAdapter: friendItemAdapter? = null
    private var backBtn: ImageView? = null
    private var addBtn: ImageButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED; //设置为竖屏
        window.navigationBarColor = resources.getColor(R.color.white)
        setContentView(R.layout.friend_main)
        initViews()
        initData()
        initClickListener()
    }

    private fun initViews() {
        backBtn = findViewById(R.id.back_toolBar)
        mRecycleView = findViewById(R.id.friend_recyclerView)
        addBtn = findViewById(R.id.addFriend)
    }


    private fun initData() {


        mAdapter = friendItemAdapter(friends)

        mRecycleView?.layoutManager = GridLayoutManager(this, 1)
        mRecycleView?.adapter = mAdapter

        GetAllUer.getAllUerDao(object : GetAllDataListener {
            override fun success(friendList: List<Friend>) {
                friends = friendList
                mAdapter?.setData(friends)
            }

            override fun fail() {
            }

        })

    }

    private fun initClickListener() {
        backBtn?.setOnClickListener {
            finish()
        }
        addBtn?.setOnClickListener {
            val intent = Intent(this, AddFriendActivity::class.java)
            startActivity(intent)
        }

    }
}