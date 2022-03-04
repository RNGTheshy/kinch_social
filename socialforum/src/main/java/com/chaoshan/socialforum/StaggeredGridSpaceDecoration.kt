package com.chaoshan.socialforum

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Copyright (C) @2021 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * @description 注意：只适用于StaggeredGridLayoutManager设置ItemDecoration
 * @author jonasfychen
 * @date 2021/6/16 8:05 下午
 */
class StaggeredGridSpaceDecoration(private val boundRect: Rect,
                                   private val rowSpan: Int,
                                   private val colSpan: Int)
    : RecyclerView.ItemDecoration()  {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val staggeredGridLayoutManager = parent.layoutManager as StaggeredGridLayoutManager
        val layoutParams = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        if (layoutParams.isFullSpan) { // 如果是全屏的不需要，这种情况出现在一些特殊的item
            return
        }
        val spanCount = staggeredGridLayoutManager.spanCount
        val spanIndex = layoutParams.spanIndex
        val childAdapterPosition = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0

        if (staggeredGridLayoutManager.orientation == StaggeredGridLayoutManager.VERTICAL) {
            countVertical(outRect, itemCount, childAdapterPosition, spanCount, spanIndex)
        } else {
            countHorizontal(outRect, itemCount, childAdapterPosition, spanCount, spanIndex)
        }
    }

    private fun countVertical(outRect: Rect,
                              itemCount: Int,
                              childAdapterPosition: Int,
                              spanCount: Int,
                              spanIndex: Int) {
        // 计算 left right
        when (spanIndex) {
            0 -> { // 左侧列
                outRect.left = boundRect.left
                outRect.right = colSpan / 2
            }
            (spanCount - 1) -> { // 右侧列
                outRect.left = colSpan / 2
                outRect.right = boundRect.right
            }
            else -> { // 中间列
                outRect.left = colSpan / 2
                outRect.right = colSpan / 2
            }
        }
        // 计算 top bottom
        when {
            childAdapterPosition < spanCount -> { // 第一行
                outRect.top = boundRect.top
                outRect.bottom = rowSpan / 2
            }
            itemCount - childAdapterPosition <= spanCount -> { // 最后一行
                outRect.top = rowSpan / 2
                outRect.bottom = boundRect.bottom
            }
            else -> { // 其他列
                outRect.top = rowSpan / 2
                outRect.bottom = rowSpan / 2
            }
        }
    }

    private fun countHorizontal(outRect: Rect,
                                itemCount: Int,
                                childAdapterPosition: Int,
                                spanCount: Int,
                                spanIndex: Int) {
        // 计算 top bottom
        when (spanIndex) {
            0 -> { // 最上行
                outRect.top = boundRect.top
                outRect.bottom = rowSpan / 2
            }
            (spanCount - 1) -> { // 最下行
                outRect.top = rowSpan / 2
                outRect.bottom = boundRect.bottom
            }
            else -> { // 中间行
                outRect.top = rowSpan / 2
                outRect.bottom = rowSpan / 2
            }
        }
        // 计算 left right
        when {
            childAdapterPosition < spanCount -> { // 最左列
                outRect.left = boundRect.left
                outRect.right = colSpan / 2
            }
            itemCount - childAdapterPosition <= spanCount -> { // 最右列
                outRect.left = colSpan / 2
                outRect.right = boundRect.right
            }
            else -> { // 其他列
                outRect.left = colSpan / 2
                outRect.right = colSpan / 2
            }
        }
    }
}