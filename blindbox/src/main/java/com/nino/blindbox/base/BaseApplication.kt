package com.nino.blindbox.base

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.multidex.MultiDex
import com.blankj.utilcode.util.Utils
import com.nino.blindbox.base.BaseActivity.ICheckLoginInterceptor

/**
 * Application基类
 */
open class BaseApplication : Application(), ActivityLifecycleCallbacks {
    /** 登录检测拦截  */
    private var mCheckLoginInterceptor: ICheckLoginInterceptor? = null

    @CallSuper
    override fun onCreate() {
        super.onCreate()
        sInstance = this
        Utils.init(this)

        // 注册生命周期的回调
        registerActivityLifecycleCallbacks(this)
    }

    /**
     * 在下一步活动之前，检测登录状态
     *
     * @param actionAfterLogin 登录后，活动的action
     * @param data 参数
     * @return false -- 未登录  true -- 已登录
     */
    fun checkLoginBeforeAction(actionAfterLogin: String?, data: Bundle?): Boolean {
        return mCheckLoginInterceptor?.checkLoginBeforeAction(actionAfterLogin, data) ?: false
    }

    /**
     * 设置登录检测拦截器
     *
     * @param interceptor 拦截器
     */
    fun setCheckLoginInterceptor(interceptor: ICheckLoginInterceptor?) {
        mCheckLoginInterceptor = interceptor
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onActivityCreated(p0: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(p0: Activity) {}
    override fun onActivityResumed(p0: Activity) {}
    override fun onActivityPaused(p0: Activity) {}
    override fun onActivityStopped(p0: Activity) {}
    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}
    override fun onActivityDestroyed(p0: Activity) {}

    companion object {
        /** 单例  */
        private var sInstance: BaseApplication? = null

        /**
         * 单例模式
         *
         * @return BaseApplication
         */
        fun getInstance(): BaseApplication? {
            return sInstance
        }
    }


}