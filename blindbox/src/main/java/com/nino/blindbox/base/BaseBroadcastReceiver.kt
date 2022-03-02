package com.nino.blindbox.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * BroadcastReceiver基类
 */
abstract class BaseBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        onReceive(context, intent, 0)
    }

    /**
     * onReceive回调
     *
     * @param context 参考回调文档说明
     * @param intent 参考回调文档说明
     * @param flag 标志(暂未使用)
     */
    abstract fun onReceive(context: Context?, intent: Intent?, flag: Int)
}