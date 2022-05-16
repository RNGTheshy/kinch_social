package com.chaoshan.data_center.friend

import cn.leancloud.LCObject
import cn.leancloud.LCQuery
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

object GetAllUer {
    fun getAllUerDao(callBack: GetAllDataListener) {
        val query = LCQuery<LCObject>("_User")
        query.whereNotEqualTo("objectId", "");
        query.findInBackground().subscribe(object : Observer<List<LCObject?>?> {
            override fun onSubscribe(disposable: Disposable) {}
            override fun onError(throwable: Throwable) {}
            override fun onComplete() {}
            override fun onNext(t: List<LCObject?>) {
                val f: MutableList<Friend> = mutableListOf()
                t.forEach {
                    f.add(Friend("null", null, "null", null, it?.getString("objectId").toString()))
                }
                callBack.success(f)
            }
        })

    }
}