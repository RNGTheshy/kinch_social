package com.chaoshan.data_center.dynamic.like

import android.util.Log
import cn.leancloud.LCObject
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class LikeDao {
    fun createNewObject(like: Like) {
        val todoCreateObject: LCObject = LCObject(like.javaClass.simpleName)
        createNewObject(todoCreateObject, like)
        pushObject(todoCreateObject)
    }

    // push对象
    private fun pushObject(todoCreateObject: LCObject) {
        todoCreateObject.saveInBackground().subscribe(object : Observer<LCObject?> {
            override fun onSubscribe(d: Disposable) {}
            override fun onError(e: Throwable) {
                Log.e("LikeDao", "saveError")

            }

            override fun onComplete() {}
            override fun onNext(t: LCObject) {
                Log.e("LikeDao", "saveSuccess" + t.objectId)
            }
        })
    }

    private fun createNewObject(todoCreateObject: LCObject, like: Like) {
        todoCreateObject.put("dynamic_id", like.dynamicID)
        todoCreateObject.put("like_people_id", like.likePeopleId)
        todoCreateObject.put("like_time", like.likeTime)
    }


}