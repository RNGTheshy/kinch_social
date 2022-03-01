package com.tencent.libui.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnDismissListener
import android.content.DialogInterface.OnShowListener
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StyleRes
import com.tencent.libui.dialog.DialogShowHelper.dismiss
import com.tencent.libui.dialog.DialogShowHelper.show
import java.lang.ref.WeakReference

/**
 * @description: Holds a dialog, can bind data and set memory leak proof listeners
 * @since 2021-02-08 20:27
 * @author jitaoguo@tencent.com
 */
abstract class DialogWrapper<T>(protected var mContext: Context?) : View.OnClickListener {

    protected var mDialog: Dialog? = null

    @get:StyleRes
    protected val style: Int
        protected get() = 0

    protected var mContentView: View? = null
    private var mLayoutInflater: LayoutInflater
    protected var mData: T? = null
    protected var mHasBuild = false
    protected var mOnBindDataDelay = false

    protected var mDismissListener: DialogDismissWrapperListener? = null
    protected var mShowListener: DialogShowWrapperListener? = null
    private var mDetachOnDismissListener: WeakReference<DetachOnDismissListener?>? = null
    private var mDetachOnShowListener: WeakReference<DetachOnShowListener?>? = null

    var data: T?
        get() = mData
        set(data) {
            mData = data
            if (data != null && mHasBuild) {
                onBindData(data)
            }
            mOnBindDataDelay = !mHasBuild
        }

    init {
        mLayoutInflater = LayoutInflater.from(mContext)
    }

    protected abstract fun onCreateView(layoutInflater: LayoutInflater): View
    protected abstract fun onViewCreated(rootView: View)

    /**
     * 对于复杂的dialog可以自定义data class并且绑定
     * 绑定发生在#build和#show之后
     */
    protected abstract fun onBindData(data: T)

    protected abstract fun setupWindow(dialog: Dialog)

    fun build() {
        mDialog = if (style == 0) Dialog(mContext!!) else Dialog(mContext!!, style)
        setupWindow(mDialog!!)
        mContentView = onCreateView(mLayoutInflater)
        mDialog!!.setContentView(mContentView!!)
        onViewCreated(mContentView!!)
        initListener()
        if (mData != null && mOnBindDataDelay) {
            onBindData(mData as T)
        }
        mOnBindDataDelay = false
        mHasBuild = true
    }

    protected open fun initListener() {
        mDetachOnDismissListener = WeakReference(
            DetachOnDismissListener(
                OnDismissListener { l: DialogInterface? -> onDismiss() })
        )
        mDialog?.setOnDismissListener(mDetachOnDismissListener?.get())
        mDetachOnShowListener = WeakReference(
            DetachOnShowListener(
                OnShowListener { l: DialogInterface? -> onShow() })
        )
        mDialog?.setOnShowListener(mDetachOnShowListener?.get())
    }

    fun setDismissListener(listener: DialogDismissWrapperListener) {
        mDismissListener = listener
    }

    fun setShowListener(listener: DialogShowWrapperListener) {
        mShowListener = listener
    }

    fun setOnCancelListener(listener: (DialogInterface) -> Unit) {
        mDialog?.setOnCancelListener(listener)
    }

    protected open fun onDismiss() {
        mDismissListener?.onDismiss(this)
        mContext = null
        mDismissListener = null
        mShowListener = null

        mDialog?.apply {
            setOnDismissListener(null)
            setOnShowListener(null)
        }

        mDetachOnDismissListener?.get()?.clear()
        mDetachOnDismissListener?.clear()
        mDetachOnDismissListener = null

        mDetachOnShowListener?.get()?.clear()
        mDetachOnShowListener?.clear()
        mDetachOnShowListener = null
    }

    protected open fun onShow() {
        mShowListener?.onShow(this)
    }

    fun setCancelable(cancelable: Boolean) {
        mDialog?.setCancelable(cancelable)
    }

    val isShowing: Boolean
        get() = if (mDialog != null) {
            mDialog!!.isShowing
        } else {
            false
        }

    fun show(): Boolean {
        if (mDialog == null) {
            build()
        }
        return show(mDialog)
    }

    fun dismiss(): Boolean {
        return dismiss(mDialog)
    }

    fun getDialog(): Dialog? {
        return mDialog
    }

    interface DialogDismissWrapperListener {
        fun onDismiss(dialogWrapper: DialogWrapper<*>?)
    }

    interface DialogShowWrapperListener {
        fun onShow(dialogWrapper: DialogWrapper<*>?)
    }

    open class OnDetachDialogItemListener<R>(r: R) {
        protected var mDelegate: R? = null
        fun clear() {
            mDelegate = null
        }

        fun get(): R? {
            return mDelegate
        }

        init {
            mDelegate = r
        }
    }

    class DetachOnDismissListener(referent: OnDismissListener) :
        OnDetachDialogItemListener<OnDismissListener>(referent), OnDismissListener {
        override fun onDismiss(dialog: DialogInterface) {
            mDelegate?.onDismiss(dialog)
        }
    }

    class DetachOnShowListener(referent: OnShowListener) :
        OnDetachDialogItemListener<OnShowListener>(referent), OnShowListener {
        override fun onShow(dialog: DialogInterface) {
            mDelegate?.onShow(dialog)
        }
    }

}