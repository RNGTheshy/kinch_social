package com.chaoshan.socialforum.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chaoshan.socialforum.R
import com.chaoshan.socialforum.StaggeredGridSpaceDecoration
import com.chaoshan.socialforum.adapter.SocialForumItemAdapter
import com.chaoshan.socialforum.databinding.SocialForumAllFragmentBinding
import com.chaoshan.socialforum.databinding.SocialForumMainFragmentBinding

class SocialForumAllFragment : Fragment() {
    companion object {
        fun newInstance(): SocialForumMainFragment {
            return SocialForumMainFragment()
        }

        private const val TAG = "SocialForumMainFragment"
        const val SPAN_COUNT = 2 // 每行的个数
        private const val DELAY_SCROLL_TO_ZERO_US = 10L
        private const val OPERATION_DELAY_TIME_US = 1000
        private const val DELAY_SCROLL_DURATION = 300L
        private const val DELAY_SHOW_MORE_DATA_DURATION = 400L
    }

    private val socialForumItemAdapter = SocialForumItemAdapter()

    private lateinit var binding: SocialForumAllFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SocialForumAllFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {

        binding.mainFragment.adapter = socialForumItemAdapter
        binding.mainFragment.apply {
            val staggeredGridLayoutManager = StaggeredGridLayoutManager(
                SPAN_COUNT,
                StaggeredGridLayoutManager.VERTICAL
            )
            layoutManager = staggeredGridLayoutManager
            staggeredGridLayoutManager.gapStrategy =
                StaggeredGridLayoutManager.GAP_HANDLING_NONE //防止item 交换位置
            val boundDistance =
                resources.getDimensionPixelSize(R.dimen.template_card_bound_distance)
            val topBoundDistance =
                resources.getDimensionPixelSize(R.dimen.template_card_top_bound_distance)
            val bottomDistance = 0
            addItemDecoration(
                StaggeredGridSpaceDecoration(
                    Rect(boundDistance, topBoundDistance, boundDistance, bottomDistance),
                    resources.getDimensionPixelSize(R.dimen.template_card_row_distance),
                    resources.getDimensionPixelSize(R.dimen.template_card_col_distance)
                )
            )
            itemAnimator = null
        }
    }

}