package com.chaoshan.data_center.dynamic.dynamic

object DynamicClient {
    private val mDynamicDao = DynamicDao()
    fun saveDate(dynamic: Dynamic, byteArray: ByteArray?) {
        mDynamicDao.createNewObject(dynamic, byteArray)
    }

    fun getAllDate() {
        mDynamicDao.updateObjectList();
    }
}