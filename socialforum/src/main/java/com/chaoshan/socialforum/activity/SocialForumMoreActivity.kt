package com.chaoshan.socialforum.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chaoshan.data_center.GetApplicationContext
import com.chaoshan.data_center.SettingsPreferencesDataStore
import com.chaoshan.data_center.dynamic.comment.Comment
import com.chaoshan.data_center.dynamic.comment.CommentClient
import com.chaoshan.data_center.dynamic.comment.GetCommentDataListener
import com.chaoshan.data_center.dynamic.dynamic.Dynamic
import com.chaoshan.data_center.dynamic.like.Like
import com.chaoshan.data_center.dynamic.like.LikeClient
import com.chaoshan.socialforum.GridSpaceDecoration
import com.chaoshan.socialforum.adapter.SocialForumCommentAdapter
import com.chaoshan.socialforum.databinding.SocialForumMoreActivityBinding
import com.chaoshan.socialforum.viewmodel.SocialForumMoreActivityViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SocialForumMoreActivity : AppCompatActivity() {
    private lateinit var binding: SocialForumMoreActivityBinding
    private var adapter: SocialForumCommentAdapter = SocialForumCommentAdapter()
    private val viewModel by lazy { SocialForumMoreActivityViewModel() }

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SocialForumMoreActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initData()
        initClickListener()
    }

    @DelicateCoroutinesApi
    @SuppressLint("LongLogTag")
    private fun initClickListener() {
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
            adapter.reFresh()
        }
        binding.sent.setOnClickListener {
            GetApplicationContext.context?.let {
                GlobalScope.launch(Dispatchers.Main) {
                    withContext(Dispatchers.IO) {
                        CommentClient.pushData(
                            Comment(
                                intent.getStringExtra(DYNAMIC_ID).toString(),
                                SettingsPreferencesDataStore.getName(it, SettingsPreferencesDataStore.USER_NAME),
                                "",
                                binding.editText.text.toString()
                            )
                        )
                        binding.editText.text.clear()
                    }
                    adapter.reFresh()
                }
            }
        }
    }

    private fun initData() {
        adapter.setCurrentCYID(intent.getStringExtra(DYNAMIC_ID).toString())
        val bundle = intent.extras
        bundle?.let {
            val dynamic: Dynamic = it.getSerializable(DYNAMIC) as Dynamic
            adapter.setCurrentCY(dynamic)
        }
        CommentClient.getData(intent.getStringExtra(DYNAMIC_ID).toString(), object : GetCommentDataListener {
            override fun getData(comment: List<Comment>) {
                adapter.setData(comment)
            }

        })
    }

    @DelicateCoroutinesApi
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
    }

    companion object {
        private const val DYNAMIC_ID = "dynamic_id"
        private const val DYNAMIC = "dynamic"
        fun goTo(context: Context, dynamicId: String, dynamic: Dynamic) {
            val bundle = Bundle()
            bundle.putSerializable(DYNAMIC, dynamic)
            val intent = Intent(context, SocialForumMoreActivity::class.java)
            intent.putExtra(DYNAMIC_ID, dynamicId)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

}