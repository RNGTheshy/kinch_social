package com.chaoshan.socialforum.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chaoshan.socialforum.GridSpaceDecoration
import com.chaoshan.socialforum.adapter.SocialForumCommentAdapter
import com.chaoshan.socialforum.databinding.SocialForumMoreActivityBinding

class SocialForumMoreActivity : AppCompatActivity() {
    private lateinit var binding: SocialForumMoreActivityBinding
    private var adapter: SocialForumCommentAdapter = SocialForumCommentAdapter()
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = SocialForumMoreActivityBinding.inflate(layoutInflater)
        initView()
    }

    private fun initView() {
        val layoutManager = GridLayoutManager(this, 1)
        binding.main.adapter = adapter
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.main.layoutManager = layoutManager
        // item之间的距离
        binding.main.addItemDecoration(
            GridSpaceDecoration(
                20,
                20
            )
        )
    }
}