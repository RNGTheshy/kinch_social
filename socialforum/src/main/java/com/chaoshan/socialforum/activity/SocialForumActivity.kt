package com.chaoshan.socialforum.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chaoshan.socialforum.databinding.SocialForumActivityBinding
import com.chaoshan.socialforum.viewmodel.SocialForumActivityViewModel

class SocialForumActivity : AppCompatActivity() {
    private val viewModel = SocialForumActivityViewModel()
    lateinit var binding: SocialForumActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SocialForumActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAction()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getDynamicData()
    }

    private fun initAction() {
        binding.addSocialForum.setOnClickListener {
            val intent = Intent(this, SocialForumAddActivity::class.java)
            startActivity(intent)
        }
        binding.back.setOnClickListener {
            finish()
        }
        viewModel.getDynamicData()
    }

}