package com.chaoshan.socialforum

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Copyright (C) @2021 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * @param rowSpan 行间距
 * @param colSpan 列间距
 * @param boundSpan left, top, right, bottom的边界间距
 * @description GridLayoutManager类型[RecyclerView]的ItemDecoration
 * @author yubinma
 * @date 3/10/21 2:53 PM
 */
class GridSpaceDecoration(
    private val rowSpan: Int,
    private val colSpan: Int,
    private val boundSpan: Rect = Rect()
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val gridLayoutManager = parent.layoutManager as GridLayoutManager
        val spanCount = gridLayoutManager.spanCount
        val params = view.layoutParams as GridLayoutManager.LayoutParams
        val spanIndex = params.spanIndex
        val spanSize = params.spanSize

        val position = parent.getChildAdapterPosition(view)
        val count = parent.adapter?.itemCount ?: 0

        if (0 == spanIndex) { // 最左列
            outRect.left = boundSpan.left
            outRect.right = colSpan / 2
        } else {
            outRect.left = colSpan / 2
            if (spanIndex + spanSize == spanCount) { // 最右列
                outRect.right = boundSpan.right
            } else {
                outRect.right = colSpan / 2
            }
        }

        if (position < spanCount) { // 第一行
            outRect.top = boundSpan.top
        } else {
            outRect.top = rowSpan
            if (count != 0 && position + spanCount >= count) { // 最后一行
                outRect.bottom = boundSpan.bottom
            }
        }
    }
}