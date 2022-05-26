package com.example.friend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.friend.databinding.AddFriendBinding

class AddFriendActivity : AppCompatActivity() {
    lateinit var binding: AddFriendBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLister()
    }

    private fun initLister() {
        binding.backToolBar.setOnClickListener {
            finish()
        }
    }
}