package com.nino.blindbox.ui.dialog

import android.app.Activity
import android.content.DialogInterface
import android.graphics.*
import android.view.*
import android.widget.TextView
import com.nino.blindbox.R
import com.nino.blindbox.ui.dialog.PopupDialog.IBackListener
import com.nino.blindbox.ui.utils.EmptyUtils

/**
 * 正在加载对话框
 *
 * @author mos
 * @date 2017.02.24
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
class LoadingDialog
/**
 * 私有化构造函数
 */
private constructor() {
    /** 对话框对象  */
    private var sDialog: PopupDialog? = null
    /**
     * 显示
     *
     * @param activity activity
     * @param option 参数
     */
    /**
     * 显示
     *
     * @param activity activity
     */
    @JvmOverloads
    fun show(activity: Activity?, option: Option? = null) {
        // 参数检查
        if (activity == null) {
            return
        }
        if (sDialog != null && sDialog!!.isShowing) {
            return
        }
        if (sDialog == null) {
            val res = activity.applicationContext.resources
            sDialog = PopupDialog(activity, R.layout.loading_dialog)
            sDialog!!.setAlpha(1.0f)
            sDialog!!.setWindowRect(
                Rect(
                    0, 0,
                    80,
                    80
                )
            )
            sDialog!!.setGravity(Gravity.CENTER)
            sDialog!!.setCanceledOnTouchOutside(false)
        }
        val msgText = sDialog!!.findViewById<TextView?>(R.id.tv_loading_dialog_msg)
        if (!EmptyUtils.Companion.isEmpty(option)) {
            sDialog!!.setCancelable(option!!.mCancelable)
            sDialog!!.setOnCancelListener(option.mListener)
            sDialog!!.setBackListener(option.mBackListener)
            if (option.mMsg != null && option.mMsg!!.length > 0) {
                msgText.text = option.mMsg
                msgText.visibility = View.VISIBLE
            } else {
                msgText.visibility = View.GONE
            }
        } else {
            // 默认不能按返回取消，若需要按返回取消，则有义务实现取消的逻辑
            sDialog!!.setCancelable(false)
            sDialog!!.setOnCancelListener(null)
            msgText.visibility = View.GONE
        }
        try {
            sDialog!!.show()
        } catch (ignored: Exception) {
        }
    }

    /**
     * 关闭正在加载对话框
     */
    fun close() {
        try {
            if (sDialog != null) {
                sDialog!!.cancel()
                sDialog = null
            }
        } catch (ignored: Exception) {
        }
    }

    /**
     * 选项参数
     */
    class Option {
        /** 取消监听  */
        var mListener: DialogInterface.OnCancelListener? = null

        /** 返回监听  */
        var mBackListener: IBackListener? = null

        /** 是否可以取消  */
        var mCancelable = true

        /** 消息  */
        var mMsg: String? = ""

        /**
         * 设置监听
         *
         * @param listener 监听
         * @return 选项参数
         */
        fun setListener(listener: DialogInterface.OnCancelListener?): Option? {
            mListener = listener
            return this
        }

        /**
         * 设置是否可取消
         *
         * @param cancelable 是否可取消
         * @return 选项参数
         */
        fun setCancelable(cancelable: Boolean): Option? {
            mCancelable = cancelable
            return this
        }

        /**
         * 设置消息
         *
         * @param message 消息
         * @return 选项参数
         */
        fun setMessage(message: String?): Option? {
            mMsg = message
            return this
        }

        /**
         * 设置返回监听
         *
         * @param listener 监听器
         * @return 选项参数
         */
        fun setBackListener(listener: IBackListener?): Option? {
            mBackListener = listener
            return this
        }
    }

    companion object {
        /** 单例  */
        private val sLoadingDialog: LoadingDialog? = LoadingDialog()

        /**
         * 获取实例
         *
         * @return 实例
         */
        fun getInstance(): LoadingDialog? {
            return sLoadingDialog
        }
    }
}