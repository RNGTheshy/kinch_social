package com.example.friend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chaoshan.data_center.friend.Friend
import com.chaoshan.data_center.friend.GetAllDataListener
import com.chaoshan.data_center.friend.GetAllUer
import com.example.friend.adapter.AddFriendItemAdapter
import com.example.friend.adapter.friendItemAdapter
import com.example.friend.databinding.FindFriendBinding


class FindFriendActivity : AppCompatActivity() {
    lateinit var binding: FindFriendBinding
    private var mAdapter = AddFriendItemAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FindFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLister()
        initData()
    }

    private fun initData() {
        binding.mainRecycler.layoutManager = GridLayoutManager(this, 1)
        binding.mainRecycler.adapter = mAdapter
    }

    private fun initLister() {
        binding.backToolBar.setOnClickListener {
            finish()
        }
        binding.touchView.setOnClickListener {
            GetAllUer.getUerDaoByNameOrId(object : GetAllDataListener {
                override fun success(friendList: List<Friend>) {
                    mAdapter.setData(friendList)
                }

                override fun fail() {

                }

            }, binding.editText.text.toString(), binding.editText.text.toString())
        }

    }
}