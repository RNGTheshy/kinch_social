package com.nino.blindbox.base

import android.content.*
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ToastUtils
import com.nino.blindbox.ui.dialog.LoadingDialog
import com.nino.blindbox.ui.widget.TopSnackBar.TSnackbar
import com.nino.blindbox.ui.widget.status.AbstractStatus
import com.nino.blindbox.ui.widget.status.StatusLayout
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.*

/**
 * fragment基类
 * @note 1. 项目中所有子类必须继承自此基类
 * -------------------------------------------------------------------------------------------------
 * @note - loadData - 懒加载数据，使用如下：
 * 1. 重写loadData方法，将加载数据的逻辑放在方法内。通过firstLoad变量，判断loadData是否是第一次调用。
 */
open class BaseFragment : Fragment(), BaseView, LifecycleProvider<FragmentEvent?> {
    /** 生命周期管理  */
    private val mLifecycleSubject = BehaviorSubject.create<FragmentEvent?>()

    /** 状态  */
    private val mStatusMap: HashMap<View?, StatusLayout?>? = HashMap()

    /** 绑定上下文  */
    var mContext: Context? = null

    /** 布局是否初始化完成  */
    private var mIsViewCreated = false

    /** 数据在进入的是否刷新  */
    private var mIsFirstLoadData = true
    var mPermissionListener: PermissionListener? = null

    /**
     * 申请运行时权限
     */
    fun requestRuntimePermission(
        permissions: Array<String?>?,
        permissionListener: PermissionListener?,
    ) {
        mPermissionListener = permissionListener
        val permissionList: MutableList<String?> = ArrayList()
        for (permission in permissions!!) {
            if (ContextCompat.checkSelfPermission(
                    mContext!!,
                    permission!!
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionList.add(permission)
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(getBaseActivity()!!, permissionList.toTypedArray(), 1)
        } else {
            permissionListener?.onGranted()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> if (grantResults.size > 0) {
                val deniedPermissions: MutableList<String?> = ArrayList()
                var i = 0
                while (i < grantResults.size) {
                    val grantResult = grantResults[i]
                    val permission = permissions[i]
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        deniedPermissions.add(permission)
                    }
                    i++
                }
                if (deniedPermissions.isEmpty()) {
                    mPermissionListener?.onGranted()
                } else {
                    mPermissionListener?.onDenied(deniedPermissions)
                }
            }
        }
    }

    override fun lifecycle(): io.reactivex.Observable<FragmentEvent?> {
        return mLifecycleSubject.hide()
    }

    override fun <T : Any?> bindUntilEvent(event: FragmentEvent): LifecycleTransformer<T> {
        return RxLifecycle.bindUntilEvent(mLifecycleSubject, event)
    }

    override fun <T> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleAndroid.bindFragment(mLifecycleSubject)
    }

    @CallSuper
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        mLifecycleSubject.onNext(FragmentEvent.ATTACH)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLifecycleSubject.onNext(FragmentEvent.CREATE)
    }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        mLifecycleSubject.onNext(FragmentEvent.CREATE_VIEW)
        // 布局创建成功
        mIsViewCreated = true
        return rootView
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        mLifecycleSubject.onNext(FragmentEvent.START)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        mLifecycleSubject.onNext(FragmentEvent.RESUME)
        if (!isHidden && userVisibleHint) {
            // 加载数据
            loadData(mIsFirstLoadData)
            mIsFirstLoadData = false
        }
    }

    @CallSuper
    override fun onPause() {
        mLifecycleSubject.onNext(FragmentEvent.PAUSE)
        super.onPause()
    }

    @CallSuper
    override fun onStop() {
        mLifecycleSubject.onNext(FragmentEvent.STOP)
        super.onStop()
    }

    @CallSuper
    override fun onDestroyView() {
        mLifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW)
        super.onDestroyView()
    }

    @CallSuper
    override fun onDestroy() {
        mLifecycleSubject.onNext(FragmentEvent.DESTROY)
        super.onDestroy()
    }

    @CallSuper
    override fun onDetach() {
        mLifecycleSubject.onNext(FragmentEvent.DETACH)
        super.onDetach()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!isHidden) {
            // 加载数据
            loadData(mIsFirstLoadData)
            mIsFirstLoadData = false
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && mIsViewCreated) {
            // 加载数据
            loadData(mIsFirstLoadData)
            mIsFirstLoadData = false
        }
    }

    /**
     * Fragment懒加载数据，主要为了解决ViewPage忽略Fragment生命周期的问题
     *
     * @param firstLoad 是否第一次加载数据
     */
    fun loadData(firstLoad: Boolean) {}
    override fun getBaseActivity(): BaseActivity? {
        if (mContext == null) {
            throw RuntimeException("This is an empty object")
        }
        return if (mContext is BaseActivity) {
            mContext as BaseActivity?
        } else {
            throw RuntimeException("please let all Activity extends BaseActivity")
        }
    }

    override fun showToast(msg: String?) {
        ToastUtils.showShort(msg)
    }

    override fun showToast(msg: String?, viewGroup: ViewGroup?) {
        if (msg != null) {
            TSnackbar.make(
                viewGroup!!,
                msg,
                TSnackbar.LENGTH_SHORT,
                TSnackbar.APPEAR_FROM_TOP_TO_DOWN
            )
                .setBackgroundColor(Color.parseColor("#F2f65a2e"))?.show()
        }
    }

    override fun showLoading() {
        LoadingDialog.getInstance()?.show(activity)
    }

    override fun hideLoading() {
        LoadingDialog.getInstance()?.close()
    }

    override fun showStatus(originView: View?, status: AbstractStatus?) {
        var statusLayout = mStatusMap?.get(originView)
        if (statusLayout == null) {
            statusLayout = StatusLayout(getBaseActivity(), originView)
            mStatusMap?.set(originView, statusLayout)
        }
        statusLayout.showStatus(status)
    }

    override fun hideStatus(originView: View?) {
        val statusLayout = mStatusMap?.get(originView)
        statusLayout?.hideStatus()
    }

    override fun getCompatColor(resId: Int): Int {
        return ContextCompat.getColor(requireActivity(), resId)
    }

    override fun getDimensionPixelOffset(resId: Int): Float {
        return resources.getDimensionPixelOffset(resId).toFloat()
    }

    override fun getCompatString(resId: Int): String? {
        return getString(resId)
    }

    override fun getCompatDrawable(resId: Int): Drawable? {
        return ContextCompat.getDrawable(requireActivity(), resId)
    }

    /**
     * 使用调度器
     *
     * @param event 生命周期
     * @return 调度转换器
     */
    fun <T> applySchedulers(event: FragmentEvent?): ObservableTransformer<T?, T?>? {
        return ObservableTransformer<T?, T?> { upstream -> // 若不绑定到View的生命周期，则直接子线程中处理 -> UI线程中回调
            if (event != null) {
                upstream.compose(this@BaseFragment.bindUntilEvent(event))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            } else upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    override fun <T> applySchedulers(): ObservableTransformer<T?, T?>? {
        return ObservableTransformer<T?, T?> { upstream ->
            upstream.compose(this@BaseFragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    override fun startActivity(clazz: Class<*>?) {
        this.startActivity(Intent(context, clazz))
    }

    override fun startActivity(intent: Intent?) {
        // TODO: 2018/5/23 添加拦截
        super.startActivity(intent)
    }

    override fun startActivity(intent: Intent?, options: Bundle?) {
        // TODO: 2018/5/23 添加拦截
        super.startActivity(intent, options)
    }

    /**
     * 有结果的跳转Activity
     *
     * @param clazz 类对象
     * @param requestCode 请求码
     */
    override fun startActivityForResult(clazz: Class<*>?, requestCode: Int) {
        this.startActivityForResult(Intent(context, clazz), requestCode)
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        // TODO: 2018/5/23 添加拦截
        super.startActivityForResult(intent, requestCode)
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        // TODO: 2018/5/23 添加拦截
        super.startActivityForResult(intent, requestCode, options)
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
            if (!BaseActivity.hookRequestLogin(activityClass, data)) {
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
            if (!BaseActivity.hookRequestLogin(activityClass, data)) {
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
            if (!BaseActivity.hookRequestLogin(activityClass, data)) {
                val intent = Intent(activity, activityClass)
                intent.flags = intentFlags
                if (data != null) {
                    intent.putExtras(data)
                }
                activity?.startActivityForResult(intent, requestCode)
            }
        }
    }
}