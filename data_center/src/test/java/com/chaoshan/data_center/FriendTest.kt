package com.chaoshan.data_center

import android.util.Log
import cn.leancloud.LCLogger
import cn.leancloud.LCObject
import cn.leancloud.LCQuery
import com.chaoshan.data_center.friend.*
import io.reactivex.Observer
import cn.leancloud.core.LeanCloud
import io.reactivex.disposables.Disposable
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class FriendTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testGetAllUerDao() {

        LeanCloud.initialize(
            "WFB1URKdIJqueBEjLd0P0xoy-gzGzoHsz",
            "9uuBkty0jB2T7HXyqDWmLOVj",
            "https://wfb1urkd.lc-cn-n1-shared.com"
        )
        LeanCloud.setLogLevel(LCLogger.Level.DEBUG);

        val testObject = LCObject("TestObject")
        testObject.put("words", "Hello world!")
        testObject.saveInBackground().blockingSubscribe()
    }

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
}