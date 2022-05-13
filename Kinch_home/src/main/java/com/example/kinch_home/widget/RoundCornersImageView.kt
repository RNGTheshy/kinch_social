package com.example.kinch_home.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * 切除圆角
 */
class RoundCornersImageView : AppCompatImageView {
    private var radiusX = 0f
    private var radiusY = 0f
    private val path = Path()
    private val rect = Rect(0, 0, width, height)
    private val rectF = RectF(rect)

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        init()
    }

    /**
     * @param rx x方向弧度
     * @param ry y方向弧度
     */
    fun setRadius(rx: Float, ry: Float) {
        radiusX = rx
        radiusY = ry
    }

    private fun init() {
        radiusX = 20f
        radiusY = 20f
    }

    override fun onDraw(canvas: Canvas) {
        path.addRoundRect(rectF, radiusX, radiusY, Path.Direction.CCW)
        canvas.clipPath(path) //Op.REPLACE这个范围内的都将显示，超出的部分覆盖
        super.onDraw(canvas)
    }
}