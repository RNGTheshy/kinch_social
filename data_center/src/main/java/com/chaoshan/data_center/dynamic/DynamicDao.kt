package com.chaoshan.data_center.dynamic

import android.util.Log
import android.view.View
import android.widget.Toast
import cn.leancloud.LCObject
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class DynamicDao {
    fun createNewObject(dynamic: Dynamic) {
        val todoCreateObject: LCObject = LCObject(dynamic.javaClass.simpleName)
        todoCreateObject.put("user_id", dynamic.userID)
        todoCreateObject.put("dynamic_id", dynamic.dynamicId)
        todoCreateObject.put("theme", dynamic.theme)
        todoCreateObject.put("text", dynamic.text)
        todoCreateObject.put("picture", dynamic.picture)
        todoCreateObject.put("release_time", dynamic.releaseTime)
        todoCreateObject.put("comment_num", dynamic.commentNumber)
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

    fun getAllData() {

    }

}