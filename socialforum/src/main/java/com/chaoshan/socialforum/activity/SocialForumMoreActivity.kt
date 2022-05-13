package com.chaoshan.socialforum.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chaoshan.data_center.GetApplicationContext
import com.chaoshan.data_center.SettingsPreferencesDataStore
import com.chaoshan.data_center.dynamic.like.Like
import com.chaoshan.data_center.dynamic.like.LikeClient
import com.chaoshan.socialforum.GridSpaceDecoration
import com.chaoshan.socialforum.adapter.SocialForumCommentAdapter
import com.chaoshan.socialforum.databinding.SocialForumMoreActivityBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SocialForumMoreActivity : AppCompatActivity() {
    private lateinit var binding: SocialForumMoreActivityBinding
    private var adapter: SocialForumCommentAdapter = SocialForumCommentAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        binding = SocialForumMoreActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    @DelicateCoroutinesApi
    @SuppressLint("LongLogTag")
    private fun initView() {
        val layoutManager = GridLayoutManager(this, 1)
        binding.main.adapter = adapter
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.main.layoutManager = layoutManager
        // item之间的距离
        binding.main.addItemDecoration(
            GridSpaceDecoration(
                2,
                0
            )
        )
        binding.iconLike.setOnClickListener {
            // 获取数据
            GetApplicationContext.context?.let {
                GlobalScope.launch {
                    Log.e("nameTest", SettingsPreferencesDataStore.getName(it, SettingsPreferencesDataStore.USER_NAME))
                    LikeClient.saveDate(
                        Like(
                            intent.getStringExtra(DYNAMIC_ID).toString(),
                            SettingsPreferencesDataStore.getName(it, SettingsPreferencesDataStore.USER_NAME),
                            ""
                        )
                    )
                }
            }
            Log.e("SocialForumMoreActivity_DYNAMIC_ID", intent.getStringExtra(DYNAMIC_ID).toString())
        }
    }

    companion object {
        private const val DYNAMIC_ID = "dynamic_id"
        fun goTo(context: Context, dynamicId: String) {
            val intent = Intent(context, SocialForumMoreActivity::class.java)
            intent.putExtra(DYNAMIC_ID, dynamicId)
            context.startActivity(intent)
        }
    }

}