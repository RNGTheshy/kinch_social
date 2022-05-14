package com.chaoshan.data_center.dynamic.comment

import android.util.Log
import cn.leancloud.LCObject
import cn.leancloud.LCQuery
import com.chaoshan.data_center.dynamic.like.Like
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class CommentDao {
    fun createNewObject(comment: Comment) {
        val todoCreateObject = LCObject(comment.javaClass.simpleName)
        createNewObject(todoCreateObject, comment);
        pushObject(todoCreateObject)
    }

    // 拉取对象数据
    private fun updateObjectList(todoCreateObject: LCObject, comment: Comment) {
        val query = LCQuery<LCObject>(Like::class.java.simpleName)
        query.orderByDescending("createdAt")
        query.findInBackground().subscribe(object : Observer<List<LCObject?>?> {
            override fun onSubscribe(disposable: Disposable) {}
            override fun onError(throwable: Throwable) {}
            override fun onComplete() {}
            override fun onNext(t: List<LCObject?>) {

            }
        })
    }

    fun getAllCommentByDynamicId(string: String, listen: GetCommentDataListener) {
        val query = LCQuery<LCObject>(Comment::class.java.simpleName)
        query.orderByDescending("createdAt")
        query.findInBackground().subscribe(object : Observer<List<LCObject?>?> {
            override fun onSubscribe(disposable: Disposable) {}
            override fun onError(throwable: Throwable) {}
            override fun onComplete() {}
            override fun onNext(t: List<LCObject?>) {
                val commentList: MutableList<Comment> = mutableListOf()
                t.forEach {
                    if (it?.getString("dynamic_id") == string) {
                        commentList.add(
                            Comment(
                                it.getString("dynamic_id"),
                                it.getString("commentator_id") ?: "",
                                it.createdAt.toString(),
                                it.getString("comment") ?: ""
                            )
                        )
                    }
                }
                listen.getData(commentList)
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

    private fun createNewObject(todoCreateObject: LCObject, comment: Comment) {
        todoCreateObject.put("dynamic_id", comment.dynamicId)
        todoCreateObject.put("comment", comment.comment)
        todoCreateObject.put("commentator_id", comment.commentatorId)
        todoCreateObject.put("time", comment.time)
    }

    fun getCount(id: String, listener: GetCommentCountListener) {
        val query = LCQuery<LCObject>(Comment::class.java.simpleName)
        query.orderByDescending("createdAt")
        query.findInBackground().subscribe(object : Observer<List<LCObject?>?> {
            override fun onSubscribe(disposable: Disposable) {}
            override fun onError(throwable: Throwable) {}
            override fun onComplete() {}
            override fun onNext(t: List<LCObject?>) {
                val commentList: MutableList<Comment> = mutableListOf()
                t.forEach {
                    if (it?.getString("dynamic_id") == id) {
                        commentList.add(
                            Comment(
                                it.getString("dynamic_id"),
                                it.getString("commentator_id") ?: "",
                                it.createdAt.toString(),
                                it.getString("comment")
                            )
                        )
                    }
                }
                listener.getCount(commentList.size)
            }
        })
    }
}