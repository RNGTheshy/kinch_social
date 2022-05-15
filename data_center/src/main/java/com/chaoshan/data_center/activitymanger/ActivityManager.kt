package com.chaoshan.data_center.activitymanger

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import java.lang.ref.WeakReference

/**
 *  Copyright (C) @2021 THL A29 Limited, a Tencent company. All rights reserved.
 *
 *     @desc   : sdk环境下模版Activity的管理器，目前是处理模版导出完成后的逻辑
 *     @author : yubinma
 *     e-mail : yubinma@tencent.com
 *     time   : 2021/11/8
 */
object ActivityManager {

    const val FORCE_OFFLINE = "FORCE_OFFLINE"
    const val LOGIN = "LOGIN"
    lateinit var receiver: ForceFinishActivity
    private var activities: MutableList<WeakReference<Activity>> = mutableListOf()

    private val lifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            activity.let {
                if (it is IRecordPage) {
                    recordForLateFinish(it)
                }
            }
        }

        override fun onActivityStarted(activity: Activity) {
        }

        override fun onActivityResumed(activity: Activity) {
            val intentFilter = IntentFilter()
            intentFilter.addAction("FORCE_OFFLINE")
            intentFilter.addAction(LOGIN)
            receiver = ForceFinishActivity()
            activity.registerReceiver(receiver, intentFilter)
        }

        override fun onActivityPaused(activity: Activity) {
            activity.unregisterReceiver(receiver)
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
        }

    }

    fun recordForLateFinish(activity: Activity) {
        activities.add(WeakReference(activity))
    }

    fun finishAllActivities() {
        activities.forEach {
            it.get()?.finish()
        }
        activities.clear()
    }

    fun finishActivity(cls: Class<Any>) {
        val listIterator = activities.listIterator()
        while (listIterator.hasNext()) {
            val activity = listIterator.next().get()
            if (activity != null && cls == activity::class.java) {
                activity.finish()
                listIterator.remove()
                break
            }
        }
    }

    fun registerActivityLifecycleCallbacks(application: Application) {
        application.registerActivityLifecycleCallbacks(lifecycleCallbacks)
    }

    fun unregisterActivityLifecycleCallbacks(application: Application) {
        application.unregisterActivityLifecycleCallbacks(lifecycleCallbacks)
    }

    interface IRecordPage

    class ForceFinishActivity : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                when (intent.action) {
                    LOGIN -> {
                        finishAllActivities()
                    }
                    FORCE_OFFLINE -> {
                        AlertDialog.Builder(context).apply {
                            setTitle("推出登陆")
                            setMessage("OUT_LOGIN")
                            setPositiveButton("OUT") { _, _ ->
                                finishAllActivities()
                            }
                            show()
                        }
                    }
                    else -> null
                }

            }

        }
    }
}



