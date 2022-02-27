package com.nino.blindbox.base

import android.annotation.SuppressLint
import android.content.*
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ToastUtils
import com.nino.blindbox.annotation.ActivityOption
import com.nino.blindbox.ui.dialog.LoadingDialog
import com.nino.blindbox.ui.widget.TopSnackBar.TSnackbar
import com.nino.blindbox.ui.widget.status.AbstractStatus
import com.nino.blindbox.ui.widget.status.StatusLayout
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import com.trello.rxlifecycle2.android.ActivityEvent as ActivityEvent1

abstract class BaseActivity : AppCompatActivity(), BaseView, LifecycleProvider<ActivityEvent1?> {
    /** RxJava生命周期管理  */
    private val mLifecycleSubject = BehaviorSubject.create<ActivityEvent1?>()
    lateinit var receiver: ForceOfflineReceiver
    /** 状态  */
    private val mStatusMap: HashMap<View?, StatusLayout?>? = HashMap()
    override fun lifecycle(): Observable<ActivityEvent1?> {
        return mLifecycleSubject.hide()
    }

    inner class ForceOfflineReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            TODO("Not yet implemented")
        }
    }
    override fun <T : Any?> bindUntilEvent(event: com.trello.rxlifecycle2.android.ActivityEvent): LifecycleTransformer<T> {
        return event.let { RxLifecycle.bindUntilEvent(mLifecycleSubject, it) }
    }

    override fun <T> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleAndroid.bindActivity(mLifecycleSubject)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLifecycleSubject.onNext(ActivityEvent1.CREATE)
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        mLifecycleSubject.onNext(ActivityEvent1.START)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        mLifecycleSubject.onNext(ActivityEvent1.RESUME)
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.tool.rss.offline")
        receiver = ForceOfflineReceiver()
        registerReceiver(receiver, intentFilter)
    }

    @CallSuper
    override fun onPause() {
        mLifecycleSubject.onNext(ActivityEvent1.PAUSE)
        super.onPause()
        unregisterReceiver(receiver)
    }

    @CallSuper
    override fun onStop() {
        mLifecycleSubject.onNext(ActivityEvent1.STOP)
        super.onStop()
    }

    @CallSuper
    override fun onDestroy() {
        mLifecycleSubject.onNext(ActivityEvent1.DESTROY)
        window.decorView.removeCallbacks(null)
        super.onDestroy()
    }

    override fun getBaseActivity(): BaseActivity? {
        return this
    }

    override fun showToast(msg: String?) {
        ToastUtils.showShort(msg)
    }

    override fun showToast(msg: String?, viewGroup: ViewGroup?) {
        TSnackbar.make(
            viewGroup!!,
            msg!!,
            TSnackbar.LENGTH_SHORT,
            TSnackbar.APPEAR_FROM_TOP_TO_DOWN
        )
            .setBackgroundColor(Color.parseColor("#F2f65a2e"))?.show()
    }

    override fun showLoading() {
        LoadingDialog.Companion.getInstance()?.show(this)
    }

    override fun hideLoading() {
        LoadingDialog.Companion.getInstance()?.close()
    }

    override fun showStatus(originView: View?, status: AbstractStatus?) {
        var statusLayout = mStatusMap?.get(originView)
        if (statusLayout == null) {
            statusLayout = StatusLayout(this, originView)
            mStatusMap?.set(originView, statusLayout)
        }
        statusLayout.showStatus(status)
    }

    override fun hideStatus(originView: View?) {
        val statusLayout = mStatusMap?.get(originView)
        statusLayout?.hideStatus()
    }

    override fun getCompatColor(resId: Int): Int {
        return ContextCompat.getColor(this, resId)
    }

    override fun getDimensionPixelOffset(resId: Int): Float {
        return resources.getDimensionPixelOffset(resId).toFloat()
    }

    override fun getCompatString(resId: Int): String? {
        return getString(resId)
    }

    override fun getCompatDrawable(resId: Int): Drawable? {
        return ContextCompat.getDrawable(this, resId)
    }

    override fun startActivity(clazz: Class<*>?) {
        this.startActivity(Intent(this, clazz))
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
    }

    override fun startActivity(intent: Intent?, options: Bundle?) {
        super.startActivity(intent, options)
    }

    override fun startActivityForResult(clazz: Class<*>?, requestCode: Int) {
        this.startActivityForResult(Intent(this, clazz), requestCode)
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
    }

    @SuppressLint("RestrictedApi")
    override fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        super.startActivityForResult(intent, requestCode, options)
    }

    /**
     * 使用调度器
     *
     * @param event 生命周期
     * @return 调度转换器
     */
    fun <T> applySchedulers(event: ActivityEvent1?): ObservableTransformer<T?, T?>? {
        return ObservableTransformer<T?, T?> { upstream -> // 若不绑定到View的生命周期，则直接子线程中处理 -> UI线程中回调
            if (event != null) {
                upstream.compose(this@BaseActivity.bindUntilEvent(event))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            } else upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    override fun <T> applySchedulers(): ObservableTransformer<T?, T?>? {
        return ObservableTransformer<T?, T?> { upstream ->
            upstream.compose(this@BaseActivity.bindUntilEvent(ActivityEvent1.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    /**
     * 显示Fragment
     *
     * @param containerId 容器id
     * @param fragment fragment
     */
    protected fun showFragment(containerId: Int, fragment: Fragment?) {
        if (fragment == null) {
            return
        }
        val tag = fragment.javaClass.simpleName
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        val cache = fm.findFragmentByTag(fragment.tag)
        if (cache != null) {
            ft.show(cache)
        } else {
            ft.add(containerId, fragment, tag)
        }
        ft.commitAllowingStateLoss()
    }

    /**
     * 替换Fragment
     *
     * @param containerId 容器id
     * @param fragment fragment
     */
    protected fun replaceFragment(containerId: Int, fragment: Fragment?) {
        if (fragment == null) {
            return
        }
        val tag = fragment.javaClass.simpleName
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(containerId, fragment, tag)
        ft.commitAllowingStateLoss()
    }

    /**
     * 隐藏fragment
     *
     * @param fragment fragment
     */
    protected fun hideFragment(fragment: Fragment?) {
        if (fragment == null) {
            return
        }
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        val cache = fm.findFragmentByTag(fragment.tag)
        if (cache != null) {
            ft.hide(cache)
        }
        ft.commitAllowingStateLoss()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val ret = super.dispatchTouchEvent(ev)
        /*
         * 若点击Activity的任何区域(除了输入框之外，应隐藏键盘)
         */if (ev?.action == MotionEvent.ACTION_UP) {
            val v = currentFocus
            if (v != null && shouldHideInput(v, ev)) {
                hideInput(v)
            }
        }
        return ret
    }

    /**
     * 是否应该隐藏输入
     *
     * @param v 焦点控件
     * @param event 动作事件
     * @return true -- 是  false -- 否
     */
    protected fun shouldHideInput(v: View?, event: MotionEvent?): Boolean {
        var should = true

        // 仅点击到输入框时，键盘不隐藏
        if (v != null && v is EditText) {
            val loc = IntArray(2)
            v.getLocationOnScreen(loc)

            // 焦点控件位置
            val left = loc[0]
            val top = loc[1]
            val right = left + v.getWidth()
            val bottom = top + v.getHeight()
            val touchX = event?.rawX
            val touchY = event?.rawY

            // 是否点击到输入框
            if ((touchX!! >= left && touchX!! <= right) && (touchY!! >= top && touchY!! <= bottom)) {
                should = false
            }
        }
        return should
    }

    /**
     * 隐藏键盘
     *
     * @param v 控件
     */
    private fun hideInput(v: View?) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * 检测登录的拦截器接口
     */
    interface ICheckLoginInterceptor {
        /**
         * 在活动之前检测登录情况
         *
         * @param actionAfterLogin 登录后的action
         * @param data 参数
         * @return true -- 已经登录 false -- 没有登录
         * @note 1. 实现者若检测到没有登录，则需要自己的处理未登录的逻辑
         * 2. 实现者若自己处理了未登录的逻辑，可执行actionAfterLogin实现隐式启动活动
         * 3. 若需要通过actionAfterLogin隐式启动活动，则需将该活动名作为action注册到
         * intent filter之中
         */
        open fun checkLoginBeforeAction(actionAfterLogin: String?, data: Bundle?): Boolean
    }

    companion object {
        /**
         * 启动Activity(扩展)
         *
         * @param context 上下文
         * @param activityClass Activity的class
         * @param intentFlags intent标识
         */
        fun startActivityEx(context: Context?, activityClass: Class<*>?, intentFlags: Int) {
            startActivityEx(context, activityClass, null, intentFlags)
        }

        /**
         * 启动Activity(扩展)
         *
         * @param context 上下文
         * @param activityClass Activity的class
         * @param data 参数
         * @param intentFlags intent标识
         */
        fun startActivityEx(
            context: Context?,
            activityClass: Class<*>?,
            data: Bundle?,
            intentFlags: Int,
        ) {
            if (!hookRequestLogin(activityClass, data)) {
                val intent = Intent(context, activityClass)
                intent.flags = intentFlags
                if (data != null) {
                    intent.putExtras(data)
                }
                context?.startActivity(intent)
            }
        }

        /**
         * 带转场的启动Activity(扩展)
         *
         * @param context 上下文
         * @param activityClass Activity的class
         * @param data Intent携带的数据
         * @param transitionActivityOptions 转场参数
         * @param intentFlags intent标识
         */
        fun startActivityTranslationEx(
            context: Context?,
            activityClass: Class<*>?,
            data: Bundle?,
            transitionActivityOptions: ActivityOptionsCompat?,
            intentFlags: Int,
        ) {
            if (!hookRequestLogin(activityClass, data)) {
                val intent = Intent(context, activityClass)
                intent.flags = intentFlags
                if (data != null) {
                    intent.putExtras(data)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    context?.startActivity(intent, transitionActivityOptions?.toBundle())
                } else {
                    context?.startActivity(intent)
                }
            }
        }

        /**
         * 启动Activity(扩展)
         *
         * @param activity activity
         * @param activityClass Activity的class
         * @param requestCode 请求码
         * @param intentFlags intent标识
         */
        fun startActivityForResultEx(
            activity: AppCompatActivity?, activityClass: Class<*>?,
            requestCode: Int, intentFlags: Int,
        ) {
            startActivityForResultEx(activity, activityClass, requestCode, null, intentFlags)
        }

        /**
         * 启动Activity(扩展)
         *
         * @param activity activity
         * @param activityClass Activity的class
         * @param data 参数
         * @param requestCode 请求码
         * @param intentFlags intent标识
         */
        fun startActivityForResultEx(
            activity: AppCompatActivity?, activityClass: Class<*>?, requestCode: Int,
            data: Bundle?, intentFlags: Int,
        ) {
            if (!hookRequestLogin(activityClass, data)) {
                val intent = Intent(activity, activityClass)
                intent.flags = intentFlags
                if (data != null) {
                    intent.putExtras(data)
                }
                activity?.startActivityForResult(intent, requestCode)
            }
        }

        /**
         * 拦截请求登录的页面
         *
         * @param activityClass 页面的类
         * @param data 参数
         * @return true -- 拦截成功  false -- 没有拦截
         */
        fun hookRequestLogin(activityClass: Class<*>?, data: Bundle?): Boolean {
            // 查看该activity是否需要登录
            val option = activityClass?.getAnnotation(ActivityOption::class.java)
            return if (option != null && option.reqLogin) {
                !BaseApplication.Companion.getInstance()
                    ?.checkLoginBeforeAction(activityClass.name, data)!!
            } else false
        }
    }

}