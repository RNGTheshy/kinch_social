package com.chaoshan.data_center.friend

import android.util.Log
import androidx.annotation.NonNull
import cn.leancloud.LCObject
import cn.leancloud.LCQuery
import com.chaoshan.data_center.SettingsPreferencesDataStore
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlin.concurrent.thread
import cn.leancloud.types.LCNull
import org.junit.Test
import java.util.*


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

    fun getFriendDao(callBack: GetAllDataListener, list: List<String>) {
        val qList = mutableListOf<LCQuery<LCObject>>()
        list.forEach {
            val query = LCQuery<LCObject>("_User")
            query.whereEqualTo("objectId", it);
            qList.add(query)
        }
        val query: LCQuery<LCObject> = LCQuery.or(qList.toList())
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

    fun getUerDaoByNameOrId(callBack: GetAllDataListener, phoneNumber: String, useremail: String) {
        val query = LCQuery<LCObject>("userdata")
        query.whereEqualTo("mobilePhoneNumber", phoneNumber)
        query.findInBackground().subscribe(object : Observer<List<LCObject?>?> {
            override fun onSubscribe(disposable: Disposable) {}
            override fun onError(throwable: Throwable) {}
            override fun onComplete() {}
            override fun onNext(t: List<LCObject?>) {
                val f: MutableList<Friend> = mutableListOf()
                t.forEach {
                    f.add(Friend("null", null, "null", it?.getString("userid").toString()))
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

    fun addFriend(mId: String, fId: String, deleteCallback: DeleteCallback) {
        val todoCreateObject: LCObject = LCObject(FriendList::class.java.simpleName)
        todoCreateObject.put("mId", mId)
        todoCreateObject.put("fId", fId)
        val todoCreateObject2: LCObject = LCObject(FriendList::class.java.simpleName)
        todoCreateObject2.put("mId", fId)
        todoCreateObject2.put("fId", mId)
        pushObject(todoCreateObject2)
        pushObject(todoCreateObject, deleteCallback)

    }

    // push对象
    private fun pushObject(todoCreateObject: LCObject) {
        todoCreateObject.saveInBackground().subscribe(object : Observer<LCObject?> {
            override fun onSubscribe(d: Disposable) {}
            override fun onError(e: Throwable) {
                Log.e("addFriend", "saveError")

            }

            override fun onComplete() {}
            override fun onNext(t: LCObject) {
                Log.e("addFriend", "saveSuccess" + t.objectId)
            }
        })
    }

    fun getAllFriend(mId: String, getAllFriendCallBack: GetAllMyFirendCallBack) {
        val query = LCQuery<LCObject>(FriendList::class.java.simpleName)
        query.whereEqualTo("mId", mId)
        query.findInBackground().subscribe(object : Observer<List<LCObject?>?> {
            override fun onSubscribe(disposable: Disposable) {
                print(disposable.toString())
            }

            override fun onError(throwable: Throwable) {}
            override fun onComplete() {}
            override fun onNext(t: List<LCObject?>) {
                val outString: MutableList<String> = mutableListOf()
                t.forEach {
                    if (it != null) {
                        outString.add(it.getString("fId"))
                    }
                }
                getAllFriendCallBack.success(outString)
            }
        })
    }


    fun getSendFriendData(mId: String, getSentFriendCallBack: GetSentFriendCallBack) {
        val query = LCQuery<LCObject>(SentFriend::class.java.simpleName)
        query.whereEqualTo("fId", mId);
        query.orderByDescending("createdAt")
        query.findInBackground().subscribe(object : Observer<List<LCObject?>?> {
            override fun onSubscribe(disposable: Disposable) {}
            override fun onError(throwable: Throwable) {}
            override fun onComplete() {}
            override fun onNext(t: List<LCObject?>) {
                val list: MutableList<SentFriend> = mutableListOf()
                t.forEach {
                    it?.let {
                        list.add(
                            SentFriend(
                                it.getString("mId").toString(),
                                it.getString("fId").toString(),
                                it.getString("message")
                            )
                        )
                    }
                }
                getSentFriendCallBack.getSuccess(
                    list.toList()
                )

            }
        })
    }

    // push对象
    private fun pushObject(todoCreateObject: LCObject, deleteCallback: DeleteCallback) {
        todoCreateObject.saveInBackground().subscribe(object : Observer<LCObject?> {
            override fun onSubscribe(d: Disposable) {}
            override fun onError(e: Throwable) {
            }

            override fun onComplete() {}
            override fun onNext(t: LCObject) {

                //delete
                var query = LCQuery<LCObject>(SentFriend::class.java.simpleName)
                query.whereEqualTo("fId", todoCreateObject.getString("fId"))
                query.whereEqualTo("mId", todoCreateObject.getString("mId"))
                query.findInBackground().subscribe(object : Observer<List<LCObject?>?> {
                    override fun onSubscribe(disposable: Disposable) {}
                    override fun onError(throwable: Throwable) {}
                    override fun onComplete() {}
                    override fun onNext(t: List<LCObject?>) {
                        t.forEach {
                            val todo =
                                LCObject.createWithoutData(
                                    SentFriend::class.java.simpleName,
                                    it?.objectId
                                )
                            todo.deleteInBackground().subscribe(object : Observer<LCNull?> {
                                override fun onSubscribe(@NonNull d: Disposable) {}
                                override fun onNext(response: LCNull) {
                                    deleteCallback.success()
                                }

                                override fun onError(@NonNull e: Throwable) {
                                    println("failed to delete a todo: " + e.message)
                                }

                                override fun onComplete() {}
                            })
                        }

                    }
                })

                //delete
                query = LCQuery<LCObject>(SentFriend::class.java.simpleName)
                query.whereEqualTo("fId", todoCreateObject.getString("mId"))
                query.whereEqualTo("mId", todoCreateObject.getString("fId"))
                query.findInBackground().subscribe(object : Observer<List<LCObject?>?> {
                    override fun onSubscribe(disposable: Disposable) {}
                    override fun onError(throwable: Throwable) {}
                    override fun onComplete() {}
                    override fun onNext(t: List<LCObject?>) {
                        t.forEach {
                            val todo =
                                LCObject.createWithoutData(
                                    SentFriend::class.java.simpleName,
                                    it?.objectId
                                )
                            todo.deleteInBackground().subscribe(object : Observer<LCNull?> {
                                override fun onSubscribe(@NonNull d: Disposable) {}
                                override fun onNext(response: LCNull) {
                                    deleteCallback.success()
                                }

                                override fun onError(@NonNull e: Throwable) {
                                    println("failed to delete a todo: " + e.message)
                                }

                                override fun onComplete() {}
                            })
                        }

                    }
                })

            }
        })
    }

}