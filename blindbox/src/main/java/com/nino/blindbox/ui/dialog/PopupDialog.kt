package com.nino.blindbox.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.*
import android.widget.TextView
import androidx.annotation.ColorInt
import com.nino.blindbox.R

/**
 * 弹出对话框
 *
 * @author mos
 * @date 2017.02.24
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified mos
 * @date 2017.06.09
 * @note 1. 修正设置位置不生效的bug
 * 2. 增加获取对话框位置的函数
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
open class PopupDialog : Dialog {
    // 顶层View
    protected var mRootView: View? = null

    // 上下文
    protected var mContext: Context?

    // 窗口对象
    protected var mWindow: Window? = null

    // 返回监听
    protected var mBackListener: IBackListener? = null

    /**
     * 构造函数
     *
     * @param context 上下文(Activity的Context)
     * @param layoutId 布局id
     */
    constructor(context: Context?, layoutId: Int) : super(
        context!!,
    ) {
        mContext = context
        initView(layoutId)
    }

    /**
     * 构造函数
     *
     * @param context 上下文(Activity的Context)
     * @param layoutId 布局id
     * @param style 样式
     */
    constructor(context: Context?, layoutId: Int, style: Int) : super(context!!, style) {
        mContext = context
        initView(layoutId)
    }

    /**
     * 设置View点击监听
     *
     * @param resId view的资源id
     * @param clickListener 点击监听
     */
    fun setViewClickListener(resId: Int, clickListener: View.OnClickListener?) {
        val v = mRootView?.findViewById<View?>(resId)
        v?.setOnClickListener(clickListener)
    }

    /**
     * 给指定控件设置文本情况
     *
     * @param resId 布局id
     * @param text 文本
     */
    fun setText(resId: Int, text: String?) {
        val v = mRootView?.findViewById<View?>(resId)
        if (v != null && v is TextView) {
            v.text = text
        }
    }

    /**
     * 设置字体颜色
     *
     * @param resId 布局id
     * @param color 颜色
     */
    fun setTextColor(resId: Int, @ColorInt color: Int) {
        val v = mRootView?.findViewById<View?>(resId)
        if (v != null && v is TextView) {
            v.setTextColor(color)
        }
    }

    /**
     * 设置字体大小
     *
     * @param resId 布局id
     * @param size 大小
     */
    fun setTextSize(resId: Int, size: Float) {
        val v = mRootView?.findViewById<View?>(resId)
        if (v != null && v is TextView) {
            v.textSize = size
        }
    }

    /**
     * 给指定控件设置隐藏情况
     *
     * @param resId 布局id
     * @param visibility 显示情况
     */
    fun setVisibility(resId: Int, visibility: Int) {
        val v = mRootView?.findViewById<View?>(resId)
        if (v != null) {
            v.visibility = visibility
        }
    }

    /**
     * 设置窗口动画
     *
     * @param resId 动画资源id
     */
    fun setWindowAnimations(resId: Int) {
        window?.setWindowAnimations(resId)
    }

    /**
     * 初始化View
     *
     * @param layoutId 布局id
     */
    private fun initView(layoutId: Int) {
        // 获取窗口
        mWindow = window

        // 设置布局id
        mRootView =
            LayoutInflater.from(mContext).inflate(layoutId, mWindow?.decorView as ViewGroup)

        // 默认点击外面可取消
        setCancelable(true)
        setCanceledOnTouchOutside(false)

        // 设置点击返回的监听
        setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                if (mBackListener != null) {
                    mBackListener!!.onBack()
                }
            }
            false
        }
    }

    /**
     * 返回监听
     *
     * @param listener 监听器
     */
    fun setBackListener(listener: IBackListener?) {
        mBackListener = listener
    }

    /**
     * 设置Gravity
     *
     * @param gravity 属性值
     */
    fun setGravity(gravity: Int) {
        mWindow?.setGravity(gravity)
    }

    /**
     * 设置透明度
     *
     * @param alpha 透明度(0.0f完全透明，1.0f不透明)
     */
    fun setAlpha(alpha: Float) {
        val lp = mWindow?.attributes
        if (lp != null) {
            lp.alpha = alpha
        }
        mWindow?.attributes = lp
    }

    /**
     * 设置窗口宽度
     *
     * @param width 宽度
     */
    fun setWindowWidth(width: Int) {
        val lp = mWindow?.attributes

        // 设置宽度
        if (lp != null) {
            lp.width = width
        }
        mWindow?.attributes = lp
    }

    /**
     * 设置窗口高度
     *
     * @param height 高度
     */
    fun setWindowHeight(height: Int) {
        val lp = mWindow?.attributes

        // 设置高度
        if (lp != null) {
            lp.height = height
        }
        mWindow?.attributes = lp
    }

    /**
     * 设置窗口占用屏幕的比例
     *
     * @param x x坐标
     * @param y y坐标
     * @param widthRatio 宽度比例(0.0f ~ 1.0f)
     * @param heightRatio 高度比例(0.0f ~ 1.0f)
     */
    fun setWindowRatio(x: Int, y: Int, widthRatio: Float, heightRatio: Float) {
        // 获取屏幕宽高
        val manager = mContext?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        manager.defaultDisplay.getMetrics(metrics)

        // 设置坐标及大小
        val lp = mWindow?.attributes
        if (lp != null) {
            lp.x = x
        }
        if (lp != null) {
            lp.y = y
        }
        if (lp != null) {
            lp.width = (metrics.widthPixels * widthRatio + 0.5f) as Int
        }
        if (lp != null) {
            lp.height = (metrics.heightPixels * heightRatio + 0.5f) as Int
        }
        mWindow?.attributes = lp
        setGravity(Gravity.LEFT or Gravity.TOP)
    }

    /**
     * 设置位置
     *
     * @param x x坐标
     * @param y y坐标
     */
    fun setWindowPosition(x: Int, y: Int) {
        // 设置坐标
        val lp = mWindow?.attributes
        lp?.x = x
        lp?.y = y
        mWindow?.attributes = lp
        setGravity(Gravity.LEFT or Gravity.TOP)
    }

    /**
     * 获取窗口矩形
     *
     * @return 窗口矩形
     */
    fun getWindowRect(): Rect {
        val lp = mWindow?.attributes
        return Rect(lp!!.x, lp.y, lp.x + lp.width, lp.y + lp.height)
    }

    /**
     * 设置窗口矩形区
     *
     * @param rect 矩形结构
     */
    fun setWindowRect(rect: Rect?) {
        val lp = mWindow?.attributes

        // 设置坐标及大小
        lp?.x = rect?.left
        lp?.y = rect?.top
        lp?.width = rect?.width()
        lp?.height = rect?.height()
        mWindow?.attributes = lp
        setGravity(Gravity.LEFT or Gravity.TOP)
    }

    /**
     * 设置变暗的不透明度
     *
     * @param amount 不透明度(0.0 ~ 完全透明，1.0 ~ 全黑)
     */
    fun setDimAmount(amount: Float) {
        mWindow?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        val layoutParams = mWindow?.attributes
        layoutParams?.dimAmount = amount
        mWindow?.attributes = layoutParams
    }

    /**
     * 返回监听
     */
    interface IBackListener {
        /**
         * 取消键被按下
         */
        open fun onBack()
    }
}