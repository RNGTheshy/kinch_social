package com.nino.blindbox.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nino.blindbox.R
import com.nino.blindbox.ui.fragment.showFragment.RTFragment
import com.nino.blindbox.ui.fragment.showFragment.Toy52Fragment
import com.nino.blindbox.ui.fragment.showFragment.othersFragment
import com.nino.blindbox.ui.fragment.showFragment.ppmtFragment

class ShowFragment : Fragment() {
    private val titles = arrayOf("POP MART", "52TOYS", "若态","其他")
    private val fragmentList: MutableList<Fragment> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_show, null)
        initView(view)
        return view
    }

    private fun initView(view :View) {
        val viewPager = view?.findViewById(R.id.viewPager) as ViewPager2
        val tabLayout = view?.findViewById(R.id.tab_layout) as TabLayout
        //初始化fragment
        fragmentList.add(ppmtFragment())
        fragmentList.add(Toy52Fragment())
        fragmentList.add(RTFragment())
        fragmentList.add(othersFragment())
        //初始化viewPage
        viewPager!!.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return fragmentList.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragmentList[position]
            }
        }
        viewPager.offscreenPageLimit = 3
        val tabLayoutMediator = TabLayoutMediator(
            tabLayout,
            viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab: TabLayout.Tab, position: Int ->
                tab.text = titles[position]
            })
        tabLayoutMediator.attach()
    }
}

