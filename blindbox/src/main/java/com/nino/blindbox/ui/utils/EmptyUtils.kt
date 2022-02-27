package com.nino.blindbox.ui.utils

import android.os.Build
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.util.SparseIntArray
import android.util.SparseLongArray
import java.lang.reflect.Array

/**
 *
 *  判空相关工具类 <br></br>
 *
 * @author lwc
 * @date 2017/3/10 15:37
 * @note -
 * isEmpty   : 判断对象是否为空
 * isNotEmpty: 判断对象是否非空
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
class EmptyUtils private constructor() {
    companion object {
        /**
         * 判断对象是否为空
         *
         * @param obj 对象
         * @return `true`: 为空<br></br>`false`: 不为空
         */
        fun isEmpty(obj: Any?): Boolean {
            if (obj == null) {
                return true
            }
            if (obj is String && obj.toString().isEmpty()) {
                return true
            }
            if (obj.javaClass.isArray && Array.getLength(obj) == 0) {
                return true
            }
            if (obj is MutableCollection<*> && (obj as MutableCollection<*>?)?.isEmpty() == true) {
                return true
            }
            if (obj is MutableMap<*, *> && (obj as MutableMap<*, *>?)?.isEmpty() == true) {
                return true
            }
            if (obj is SparseArray<*> && (obj as SparseArray<*>?)?.size() == 0) {
                return true
            }
            if (obj is SparseBooleanArray && (obj as SparseBooleanArray?)?.size() == 0) {
                return true
            }
            if (obj is SparseIntArray && (obj as SparseIntArray?)?.size() == 0) {
                return true
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                if (obj is SparseLongArray && (obj as SparseLongArray?)?.size() == 0) {
                    return true
                }
            }
            return false
        }

        /**
         * 判断对象是否非空
         *
         * @param obj 对象
         * @return `true`: 非空<br></br>`false`: 空
         */
        fun isNotEmpty(obj: Any?): Boolean {
            return !isEmpty(obj)
        }
    }

    /**
     * 构造类
     */
    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }
}