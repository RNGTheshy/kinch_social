package com.chaoshan.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.chaoshan.ui.R
import com.chaoshan.ui.utils.DensityUtils
import com.tencent.libui.dialog.DialogWrapper
import kotlin.math.min

/**
 * @description: Two Button Dialog with: 1. title 2. description 3. negative button 4. positive button
 * @since 2021-02-08 20:27
 * @author jitaoguo@tencent.com
 */
open class TwoButtonDialog(context: Context?) : DialogWrapper<Any?>(context) {

    private var mTitleText: TextView? = null
    private var mDesText: TextView? = null
    private var mNegativeBtn: TextView? = null
    private var mPositiveBtn: TextView? = null
    private var mCloseBtn: ImageView? = null
    protected var mClickListener: ActionClickListener? = null

    override fun onCreateView(layoutInflater: LayoutInflater): View {
        return layoutInflater.inflate(R.layout.dialog_button, null)
    }

    override fun onViewCreated(rootView: View) {
        mTitleText = rootView.findViewById(R.id.title_tv)
        mDesText = rootView.findViewById(R.id.description_tv)
        mNegativeBtn = rootView.findViewById(R.id.negative_btn)
        mPositiveBtn = rootView.findViewById(R.id.positive_btn)
        mCloseBtn = rootView.findViewById(R.id.close_btn)
    }

    override fun initListener() {
        super.initListener()
        mNegativeBtn?.setOnClickListener({ v: View? ->
            onActionBtn1Click(v)
        })
        mPositiveBtn?.setOnClickListener( { v: View? ->
            onActionBtn2Click(v)
        })
        mCloseBtn?.setOnClickListener( {
            onCloseBtnClick()
        })
    }

    private fun onCloseBtnClick() {
        dismiss()
        mClickListener?.onCloseBtnClick(this)
    }

    protected fun onActionBtn1Click(v: View?) {
        mClickListener?.onNegativeBtnClick(this)
        dismiss()
    }

    protected fun onActionBtn2Click(v: View?) {
        mClickListener?.onPositiveBtnClick(this)
        dismiss()
    }

    fun setActionClickListener(clickListener: ActionClickListener) {
        mClickListener = clickListener
    }

    /**
     * 获取"确定"按钮
     */
    fun getPositiveBtn(): View? {
        return mPositiveBtn
    }

    /**
     * 获取"取消"按钮
     */
    fun getNegativeBtn(): View? {
        return mNegativeBtn
    }

    override fun onDismiss() {
        super.onDismiss()
        mNegativeBtn?.setOnClickListener(null)
        mPositiveBtn?.setOnClickListener(null)
    }

    override fun setupWindow(dialog: Dialog) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            decorView.setPadding(0, 0, 0, 0)
            val wlp: WindowManager.LayoutParams = attributes
            wlp.gravity = Gravity.CENTER
            wlp.width = getProperWidth(context)
            wlp.dimAmount = DIM_AMOUNT
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            attributes = wlp
            setWindowAnimations(R.style.dialogWindowAnim)
        }
        dialog.setCancelable(false)
    }

    //Get proper width limited by ratio and absolute max width
    private fun getProperWidth(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val point = Point()
        display.getSize(point)
        val width = (point.y * WIDTH_RATIO).toInt()
        val maxWidth = DensityUtils.dp2px(MAX_WIDTH)
        return min(width, maxWidth)
    }

    override fun onBindData(data: Any?) {
        //no - op
    }

    override fun onClick(v: View) {
        //no - op
    }

    fun setTitle(title: CharSequence) {
        mTitleText?.text = title
    }

    fun setTitle(resId: Int) {
        mTitleText?.setText(resId)
    }

    fun setTitleMaxLines(maxLines: Int) {
        mTitleText?.maxLines = maxLines
    }

    fun setTitleTextSize(size: Float) {
        mTitleText?.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    fun setTitleHeight(height: Int) {
        mTitleText?.let {
            val lp = it.layoutParams
            lp.height = height
            it.layoutParams = lp
        }
    }

    fun setDescription(resId: Int) {
        mContext?.let { ctx ->
            setDescription(ctx.resources.getString(resId))
        }
    }

    fun setDescription(desStr: CharSequence) {
        mDesText?.let { textView ->
            if (desStr.isNotEmpty()) {
                textView.visibility = View.VISIBLE
                textView.text = desStr
            } else {
                textView.visibility = View.GONE
            }
        }
    }

    fun setDescTxtGravity(gravity: Int) {
        mDesText?.gravity = gravity
    }

    fun setDescriptionVisible(visible: Boolean) {
        mDesText?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setNegativeBtnName(resId: Int) {
        mNegativeBtn?.setText(resId)
    }

    fun setNegativeBtnName(actionName: CharSequence?) {
        if (actionName.isNullOrEmpty()) {
            val lp = mNegativeBtn?.layoutParams ?: return
            lp.height = 0
            lp.width = 0
        }
        mNegativeBtn?.text = actionName
    }

    fun setPositiveBtnName(resId: Int) {
        mPositiveBtn?.setText(resId)
    }

    fun setPositiveBtnName(actionName: CharSequence?) {
        mPositiveBtn?.text = actionName
    }

    /**
     * 展示右上角的X按钮
     */
    fun showCloseBtn() {
        mCloseBtn?.visibility = View.VISIBLE
    }

    fun applyWarningDialogStyle() {
        val ctx = mContext
        val textView = mPositiveBtn
        if (ctx == null || textView == null) {
            return
        }
        textView.background = AppCompatResources.getDrawable(ctx, R.drawable.dialog_confirm_red_bg)
        textView.setTextColor(Color.WHITE)
    }

    interface ActionClickListener {

        fun onNegativeBtnClick(dialogWrapper: DialogWrapper<*>?)

        fun onPositiveBtnClick(dialogWrapper: DialogWrapper<*>?)

        fun onCloseBtnClick(dialogWrapper: DialogWrapper<*>?) {}

    }

    open class ActionClickListenerAdapter : ActionClickListener,
        DialogDismissWrapperListener,
        DialogShowWrapperListener {
        override fun onDismiss(dialogWrapper: DialogWrapper<*>?) {}

        override fun onShow(dialogWrapper: DialogWrapper<*>?) {}

        override fun onNegativeBtnClick(dialogWrapper: DialogWrapper<*>?) {}

        override fun onPositiveBtnClick(dialogWrapper: DialogWrapper<*>?) {}

    }

    companion object {
        private const val DIM_AMOUNT = 0.3f
        private const val WIDTH_RATIO = 0.79f
        private const val MAX_WIDTH = 295f
    }

}