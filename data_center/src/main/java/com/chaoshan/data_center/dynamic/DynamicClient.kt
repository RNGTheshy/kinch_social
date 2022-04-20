package com.chaoshan.data_center.dynamic

object DynamicClient {
    private val mDynamicDao = DynamicDao()

    fun saveDate(dynamic: Dynamic) {
        mDynamicDao.createNewObject(dynamic)
    }
}