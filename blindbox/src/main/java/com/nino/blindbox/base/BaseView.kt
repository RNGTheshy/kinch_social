package com.nino.blindbox.base

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.nino.blindbox.ui.widget.status.AbstractStatus
import io.reactivex.ObservableTransformer

/**
 * 基础视图类
 */
interface BaseView {
    /**
     * 获取BaseActivity
     *
     * @return BaseActivity
     */
    open fun getBaseActivity(): BaseActivity?

    /**
     * 显示Toast
     *
     * @param msg 消息
     */
    open fun showToast(msg: String?)

    /**
     * 显示Toast
     *
     * @param msg 消息
     */
    open fun showToast(msg: String?, viewGroup: ViewGroup?)

    /**
     * 显示Loading
     */
    open fun showLoading()

    /**
     * 隐藏Loading
     */
    open fun hideLoading()

    /**
     * 显示状态
     *
     * @param originView 原始视图
     * @param status 状态
     */
    open fun showStatus(originView: View?, status: AbstractStatus?)

    /**
     * 隐藏状态
     *
     * @param originView 原始视图
     */
    open fun hideStatus(originView: View?)

    /**
     * 通过兼容取Color
     *
     * @param resId ColorRes
     * @return ColorInt
     */
    open fun getCompatColor(@ColorRes resId: Int): Int

    /**
     * 通过兼容取DimensionPixelOffset
     *
     * @param resId DimenRes
     * @return px
     */
    open fun getDimensionPixelOffset(@DimenRes resId: Int): Float

    /**
     * 通过兼容取String
     *
     * @param resId StringRes
     * @return String
     */
    open fun getCompatString(@StringRes resId: Int): String?

    /**
     * 通过兼容器取Drawable
     *
     * @param resId DrawableRes
     * @return Drawable
     */
    open fun getCompatDrawable(@DrawableRes resId: Int): Drawable?

    /**
     * 跳转Activity
     *
     * @param clazz 类对象
     */
    open fun startActivity(clazz: Class<*>?)

    /**
     * 跳转Activity获取结果
     *
     * @param clazz 类对象
     * @param requestCode 请求码
     */
    open fun startActivityForResult(clazz: Class<*>?, requestCode: Int)

    /**
     * 使用调度器
     *
     * @return 调度转换器
     * @note 默认生命周期DESTROY
     */
    open fun <T> applySchedulers(): ObservableTransformer<T?, T?>?
}