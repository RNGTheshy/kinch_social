package com.example.friend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.friend.databinding.FindFriendBinding


class FindFriendActivity : AppCompatActivity() {
    lateinit var binding: FindFriendBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FindFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLister()
    }

    private fun initLister() {
        binding.backToolBar.setOnClickListener {
            finish()
        }
    }
}