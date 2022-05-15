package com.chaoshan.data_center.dynamic.like

import android.util.Log
import cn.leancloud.LCObject
import cn.leancloud.LCQuery
import com.chaoshan.data_center.dynamic.dynamic.Dynamic
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class LikeDao {
    fun createNewObject(like: Like) {
        val todoCreateObject: LCObject = LCObject(like.javaClass.simpleName)
        // 如果对应的ID有了就不再push上去了。
        updateObjectList(todoCreateObject, like)
    }

    // 拉取对象数据
    private fun updateObjectList(todoCreateObject: LCObject, like: Like) {
        val query = LCQuery<LCObject>(Like::class.java.simpleName)
        query.orderByDescending("createdAt")
        query.findInBackground().subscribe(object : Observer<List<LCObject?>?> {
            override fun onSubscribe(disposable: Disposable) {}
            override fun onError(throwable: Throwable) {}
            override fun onComplete() {}
            override fun onNext(t: List<LCObject?>) {
                t.forEach {
                    if (it?.getString("dynamic_id") == like.dynamicID
                        && it?.getString("like_people_id") == like.likePeopleId
                    ) {
                        return
                    }
                }
                createNewObject(todoCreateObject, like)
                pushObject(todoCreateObject)
            }
        })
    }

    fun getAllCountByDynamicId(string: String, listen: GetLikeCountCallBack) {
        var count = 0
        val query = LCQuery<LCObject>(Like::class.java.simpleName)
        query.orderByDescending("createdAt")
        query.findInBackground().subscribe(object : Observer<List<LCObject?>?> {
            override fun onSubscribe(disposable: Disposable) {}
            override fun onError(throwable: Throwable) {}
            override fun onComplete() {}
            override fun onNext(t: List<LCObject?>) {
                t.forEach {
                    if (it?.getString("dynamic_id") == string) {
                        count++;
                    }
                }
                listen.get(count);

            }
        })
    }

    fun getLikePersonList(dyId: String, list: GetLikePersonList) {
        val query = LCQuery<LCObject>(Like::class.java.simpleName)
        query.orderByDescending("createdAt")
        query.findInBackground().subscribe(object : Observer<List<LCObject?>?> {
            override fun onSubscribe(disposable: Disposable) {}
            override fun onError(throwable: Throwable) {}
            override fun onComplete() {}
            override fun onNext(t: List<LCObject?>) {
                val outLikeList: MutableList<Like> = mutableListOf()
                t.forEach {
                    if (it?.getString("dynamic_id") == dyId) {
                        outLikeList.add(
                            Like(
                                it.getString("dynamic_id"),
                                it.getString("like_people_id"),
                                it.getString("like_time")
                            )
                        )
                    }
                }
                list.getLikePersonList(outLikeList)

            }
        })
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