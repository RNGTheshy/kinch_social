package com.chaoshan.data_center.dynamic.dynamic

import com.chaoshan.data_center.friend.DeleteCallback

object DynamicClient {
    private val mDynamicDao = DynamicDao()
    fun saveDate(dynamic: Dynamic, byteArray: ByteArray?) {
        mDynamicDao.createNewObject(dynamic, byteArray)
    }

    fun getAllDate() {
        mDynamicDao.updateObjectList();
    }

    fun deleteComment(
        string: String,
        deleteCallback: DeleteCallback,
    ) {
        mDynamicDao.deleteComment(
            string, deleteCallback
        )
    }
}