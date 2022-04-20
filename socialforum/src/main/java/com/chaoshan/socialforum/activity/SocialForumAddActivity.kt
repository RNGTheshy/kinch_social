package com.chaoshan.socialforum.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chaoshan.data_center.dynamic.Dynamic
import com.chaoshan.data_center.dynamic.DynamicClient
import com.chaoshan.socialforum.databinding.SocialAddFragmentBinding

class SocialForumAddActivity : AppCompatActivity() {
    private lateinit var binding: SocialAddFragmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SocialAddFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAction()
    }

    private fun initAction() {
        binding.backSocial.setOnClickListener {
            finish()
        }
        binding.sent.setOnClickListener {
            sent()
        }
    }

    private fun sent() {
        val newDynamic = Dynamic(
            0,
            111,
            "111",
            binding.mainText.text.toString()
        )
        DynamicClient.saveDate(newDynamic)

    }


}