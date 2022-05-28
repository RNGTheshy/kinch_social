package com.chaoshan.data_center.friend

import android.util.Log
import cn.leancloud.LCObject
import cn.leancloud.LCQuery
import cn.leancloud.Messages
import com.chaoshan.data_center.SettingsPreferencesDataStore
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.GlobalScope
import kotlin.concurrent.thread

object GetAllUer {
    fun getAllUerDao(callBack: GetAllDataListener) {
        val query = LCQuery<LCObject>("_User")
        query.whereNotEqualTo("objectId", SettingsPreferencesDataStore.getCurrentUserObjetID());
        query.findInBackground().subscribe(object : Observer<List<LCObject?>?> {
            override fun onSubscribe(disposable: Disposable) {}
            override fun onError(throwable: Throwable) {}
            override fun onComplete() {}
            override fun onNext(t: List<LCObject?>) {
                val f: MutableList<Friend> = mutableListOf()
                t.forEach {
                    f.add(Friend("null", null, "null", it?.getString("objectId").toString()))
                }
                callBack.success(f)
            }
        })


    }

    fun addFriendWithMessage(mId: String, fId: String, messages: String) {
        val todoCreateObject: LCObject = LCObject(SentFriend::class.java.simpleName)
        todoCreateObject.put("mId", mId)
        todoCreateObject.put("fId", fId)
        todoCreateObject.put("message", messages)
        todoCreateObject.saveInBackground().subscribe(object : Observer<LCObject?> {
            override fun onSubscribe(d: Disposable) {}
            override fun onError(e: Throwable) {
                Log.e("addFriendWithMessage", "saveError")

            }

            override fun onComplete() {}
            override fun onNext(t: LCObject) {
                Log.e("addFriendWithMessage", "saveSuccess" + t.objectId)
            }
        })
    }

    fun addFriend(mId: String, fId: String) {
        val query = LCQuery<LCObject>(FriendList::class.java.simpleName)
        query.whereEqualTo("mId", mId);
        query.findInBackground().subscribe(object : Observer<List<LCObject?>?> {
            override fun onSubscribe(disposable: Disposable) {}
            override fun onError(throwable: Throwable) {}
            override fun onComplete() {}
            override fun onNext(t: List<LCObject?>) {
                if (t.isEmpty()) {
                    val todoCreateObject =
                        LCObject.createWithoutData(FriendList::class.java.simpleName, mId)
                    todoCreateObject.put("mId", mId)
                    val friendList = emptyList<String>().toMutableList()
                    friendList.add(fId)
                    todoCreateObject.put("fId", friendList.toList())
                } else {
                    t.forEach {
                        val todo = LCObject.createWithoutData(
                            FriendList::class.java.simpleName,
                            it?.objectId
                        )
                        val friendList = todo.getList("fId")
                        friendList.add(fId)
                        todo.put("fId", fId)
                        todo.saveInBackground().subscribe(object : Observer<LCObject?> {
                            override fun onSubscribe(disposable: Disposable) {}
                            override fun onError(throwable: Throwable) {
                                println("保存失败！")
                            }

                            override fun onComplete() {}
                            override fun onNext(t: LCObject) {

                            }
                        })
                    }

                }
            }
        })

    }

    fun getAllFriend(mId: String, getAllFriendCallBack: GetAllFriendCallBack) {
        val query = LCQuery<LCObject>(FriendList::class.java.simpleName)
        query.whereNotEqualTo("mId", mId);
        query.findInBackground().subscribe(object : Observer<List<LCObject?>?> {
            override fun onSubscribe(disposable: Disposable) {}
            override fun onError(throwable: Throwable) {}
            override fun onComplete() {}
            override fun onNext(t: List<LCObject?>) {
                t[0]?.let {
                    getAllFriendCallBack.getSuccess(
                        FriendList(
                            mId,
                            it.getList("fId") as List<String>
                        )
                    )
                }

            }
        })
    }

}