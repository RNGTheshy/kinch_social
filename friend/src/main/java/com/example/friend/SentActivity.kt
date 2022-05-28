package com.example.friend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.friend.databinding.FindFriendBinding
import com.example.friend.databinding.SentInformationBinding

class SentActivity : AppCompatActivity() {
    lateinit var binding: SentInformationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SentInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLister()
    }

    private fun initLister() {

    }
}