package com.nino.blindbox.ui.activity

import android.os.Bundle
import android.os.Message
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nino.blindbox.R
import com.nino.blindbox.base.BaseActivity
import com.nino.blindbox.base.BaseFragment
import com.nino.blindbox.ui.fragment.*

class HomeActivity : BaseActivity() {
    //标题
    private val titles = arrayOf("首页", "市场", "玩具柜", "消息","个人中心")
    private val fragmentList : MutableList<BaseFragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initViews();
    }

    /**
     * 初始化控件
     */
    private fun initViews() {
        val viewPager :ViewPager2 =findViewById(R.id.viewPager)
        val tabLayout :TabLayout =findViewById(R.id.tab_layout)
        //初始化fragment
        fragmentList.add(HomeFragment())
        fragmentList.add(MarketFragment())
        fragmentList.add(ShowFragment())
        fragmentList.add(MessageFragment())
        fragmentList.add(SelfFragment())
        //初始化viewPage
        viewPager!!.adapter = object: FragmentStateAdapter(this){
            override fun getItemCount(): Int {
                return fragmentList.size
            }

            override fun createFragment(position: Int): BaseFragment {
                return  fragmentList[position]
            }
        }
        viewPager.offscreenPageLimit = 3
        val tabLayoutMediator = TabLayoutMediator(
            tabLayout,viewPager, TabLayoutMediator.TabConfigurationStrategy { tab: TabLayout.Tab, position: Int ->
                tab.text = titles[position]
            })
        tabLayoutMediator.attach()
    }
}