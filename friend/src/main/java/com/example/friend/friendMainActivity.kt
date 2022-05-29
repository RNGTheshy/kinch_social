package com.example.friend

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chaoshan.data_center.friend.Friend
import com.chaoshan.data_center.friend.GetAllDataListener
import com.chaoshan.data_center.friend.GetAllUer
import com.chaoshan.data_center.togetname.Getplace
import com.chaoshan.data_center.togetname.center_getname
import com.chaoshan.data_center.togetname.getPersonal_data
import com.chaoshan.data_center.togetname.getPersonal_data.getplace
import com.example.friend.adapter.friendIconAdapter
import com.example.friend.adapter.friendItemAdapter

class friendMainActivity : AppCompatActivity() {
    private var friends = listOf<Friend>()
    private var mRecycleView: RecyclerView? = null
    private var mAdapter: friendItemAdapter? = null
    private var iconRecycleView: RecyclerView? = null
    private var iconAdapter: friendIconAdapter? = null
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
        iconRecycleView = findViewById(R.id.friend_icon_recyclerView)
        addBtn = findViewById(R.id.addFriend)
    }


    private fun initData() {

        mAdapter = friendItemAdapter(friends)
        mRecycleView?.layoutManager = LinearLayoutManager(this)
        mRecycleView?.adapter = mAdapter

        iconAdapter = friendIconAdapter(friends)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        iconRecycleView?.layoutManager = linearLayoutManager
        iconRecycleView?.adapter = iconAdapter

        GetAllUer.getAllUerDao(object : GetAllDataListener {
            override fun success(friendList: List<Friend>) {
                friends = friendList
                mAdapter?.setData(friends)
                iconAdapter?.setData(friends)
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
        iconAdapter?.setOnItemClickListener(object : friendIconAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                //跳转到主界面，定位到相应的朋友位置
                val friendId = friends[position].id
                var intent = Intent()
                intent.putExtra("id",friendId)
                getPersonal_data.getplace(
                    friendId
                ) { longitude, latitude ->
                    intent.putExtra("longitude", longitude)
                    intent.putExtra("latitude", latitude)
                }

                setResult(Activity.RESULT_OK, intent)
                finish()
            }

            override fun onItemLongClick(view: View?, position: Int) {
                TODO("Not yet implemented")
            }
        })
    }
}