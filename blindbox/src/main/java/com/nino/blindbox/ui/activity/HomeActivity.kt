package com.nino.blindbox.ui.activity

import android.graphics.Outline
import android.os.Bundle
import android.view.View
import android.view.ViewOutlineProvider


import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.chaoshan.socialforum.SocialForumMainFragment
import com.chaoshan.socialforum.databinding.SocialForumMainFragmentBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nino.blindbox.R
import com.nino.blindbox.ui.fragment.*

class HomeActivity : AppCompatActivity() {
    //标题
    private val titles = arrayOf("首页", "市场", "展柜", "消息", "我的")
    private val picId = arrayOf(R.mipmap.home, R.mipmap.market, R.mipmap.show, R.mipmap.message, R.mipmap.self)
    private val fragmentList: MutableList<Fragment> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initViews();
    }

    /**
     * 初始化控件
     */
    private fun initViews() {
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        //初始化fragment
        fragmentList.add(SocialForumMainFragment.newInstance())
        fragmentList.add(MarketFragment())
        fragmentList.add(ShowFragment())
        fragmentList.add(SocialForumMainFragment.newInstance())
        fragmentList.add(SocialForumMainFragment.newInstance())
        //初始化viewPage
        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return fragmentList.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragmentList[position]
            }
        }
        //取消切换动画效果
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let { viewPager.setCurrentItem(it, false) }
            }

        })
        //取消滑动切换
        viewPager.isUserInputEnabled = false
        viewPager.offscreenPageLimit = 3
        val tabLayoutMediator = TabLayoutMediator(
            tabLayout, viewPager, TabLayoutMediator.TabConfigurationStrategy { tab: TabLayout.Tab, position: Int ->
                tab.text = titles[position]
                val icon = getResources().getDrawable(picId[position])
                tab.icon = icon
            })
        tabLayoutMediator.attach()
    }
}