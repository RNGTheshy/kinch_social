package com.chaoshan.data_center.dynamic

import android.util.Log
import cn.leancloud.LCFile
import cn.leancloud.LCObject
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class DynamicDao {
    fun createNewObject(dynamic: Dynamic, bitmap: ByteArray?) {
        val todoCreateObject: LCObject = LCObject(dynamic.javaClass.simpleName)
        createNewObject(todoCreateObject, dynamic)
        bitmap?.let { saveImage(todoCreateObject, it) }
    }

    private fun pushObject(todoCreateObject: LCObject) {
        todoCreateObject.saveInBackground().subscribe(object : Observer<LCObject?> {
            override fun onSubscribe(d: Disposable) {}
            override fun onError(e: Throwable) {
                Log.e("DynamicCreate", "saveError")
            }

            override fun onComplete() {}
            override fun onNext(t: LCObject) {
                Log.e("DynamicCreate", "saveSuccess" + t.objectId)
            }
        })
    }

    private fun createNewObject(todoCreateObject: LCObject, dynamic: Dynamic) {
        todoCreateObject.put("user_id", dynamic.userID)
        todoCreateObject.put("dynamic_id", dynamic.dynamicId)
        todoCreateObject.put("theme", dynamic.theme)
        todoCreateObject.put("text", dynamic.text)
        todoCreateObject.put("release_time", dynamic.releaseTime)
        todoCreateObject.put("comment_num", dynamic.commentNumber)
    }

    private fun saveImage(todoCreateObject: LCObject, bitmap: ByteArray) {
        val file = LCFile("test", bitmap)
        file.saveInBackground().subscribe(object : Observer<LCFile> {
            override fun onSubscribe(disposable: Disposable) {}
            override fun onNext(file: LCFile) {
                todoCreateObject.put("picture", file.url)
                pushObject(todoCreateObject)
                Log.e("文件保存完成。", "URL：" + file.url)
            }

            override fun onError(throwable: Throwable) {
                // 保存失败，可能是文件无法被读取，或者上传过程中出现问题
            }

            override fun onComplete() {}
        })

    }
}