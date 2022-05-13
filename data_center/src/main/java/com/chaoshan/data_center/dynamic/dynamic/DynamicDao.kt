package com.chaoshan.data_center.dynamic.dynamic

import android.util.Log
import cn.leancloud.LCFile
import cn.leancloud.LCObject
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import cn.leancloud.LCQuery
import com.chaoshan.data_center.dynamic.GetDateCallBack


class DynamicDao {
    private var getDateCallBack: GetDateCallBack<Dynamic>? = null

    constructor()
    constructor(getDateCallBack: GetDateCallBack<Dynamic>) {
        this.getDateCallBack = getDateCallBack
    }

    // 创建对象
    fun createNewObject(dynamic: Dynamic, bitmap: ByteArray?) {
        val todoCreateObject: LCObject = LCObject(dynamic.javaClass.simpleName)
        createNewObject(todoCreateObject, dynamic)
        if (bitmap == null) {
            pushObject(todoCreateObject)
        } else {
            saveImage(todoCreateObject, bitmap)
        }

    }

    // push对象
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

    // push 图片
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
                Log.e("文件保存失败。", throwable.toString())
                // 保存失败，可能是文件无法被读取，或者上传过程中出现问题
            }

            override fun onComplete() {}
        })

    }

    // 拉取对象数据
    fun updateObjectList() {
        val query = LCQuery<LCObject>(Dynamic::class.java.simpleName)
        query.orderByDescending("createdAt")
        query.findInBackground().subscribe(object : Observer<List<LCObject?>?> {
            override fun onSubscribe(disposable: Disposable) {}
            override fun onError(throwable: Throwable) {}
            override fun onComplete() {}
            override fun onNext(t: List<LCObject?>) {
                getDateCallBack?.updateDate(lCObject2Dynamic(t))
            }
        })
    }

    fun lCObject2Dynamic(list: List<LCObject?>): List<Dynamic> {
        val dynamicList: MutableList<Dynamic> = mutableListOf()
        list.forEach {
            it?.let {
                dynamicList.add(lCObject2Dynamic(it))
            }
        }
        return dynamicList.toList()

    }

    private fun lCObject2Dynamic(lCObject: LCObject): Dynamic {
        return Dynamic(
            lCObject.getString("dynamic_id"),
            lCObject.getString("user_id"),
            lCObject.getString("theme"),
            lCObject.getString("text"),
            lCObject.getString("picture"),
            lCObject.getDate("createdAt").toGMTString().toString(),
            "0",
            lCObject.getString("comment_num"),
        )
    }
}