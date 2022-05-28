package com.example.friend

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chaoshan.data_center.friend.Friend
import com.chaoshan.data_center.friend.GetAllDataListener
import com.chaoshan.data_center.friend.GetAllUer
import com.example.friend.adapter.MayBeFriend
import com.example.friend.databinding.AddFriendBinding
import com.example.friend.viewholder.GridSpaceDecoration

class AddFriendActivity : AppCompatActivity() {
    lateinit var binding: AddFriendBinding
    var adapter = MayBeFriend()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLister()
        initView()
        initData()
    }

    private fun initData() {
        GetAllUer.getAllUerDao(object : GetAllDataListener {
            override fun success(friendList: List<Friend>) {
                adapter.setMayBeData(friendList)
            }

            override fun fail() {
            }

        })

    }

    private fun initView() {
        val layoutManager = GridLayoutManager(this, 1)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.mainRecycler.adapter = adapter
        binding.mainRecycler.layoutManager = layoutManager
        binding.mainRecycler.addItemDecoration(
            GridSpaceDecoration(
                2,
                0
            )
        )
    }

    private fun initLister() {
        binding.backToolBar.setOnClickListener {
            finish()
        }
        binding.sent.setOnClickListener {
            startActivity(Intent(this, FindFriendActivity::class.java))
        }
    }

}