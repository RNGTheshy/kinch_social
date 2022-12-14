package com.chaoshan.data_center.togetname

import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.core.view.drawToBitmap
import cn.leancloud.LCFile
import cn.leancloud.LCObject
import cn.leancloud.LCQuery
import com.bumptech.glide.Glide
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.*


public class Headport {
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

    //查找objectid对应位置
    private fun pushpicutre(objectid: String, theurl: String) {
        val query = LCQuery<LCObject>("userdata")
        query.whereEqualTo("userid", objectid)
        query.firstInBackground.subscribe(object : Observer<LCObject?> {
            override fun onSubscribe(disposable: Disposable) {}
            override fun onError(throwable: Throwable) {}
            override fun onComplete() {}
            override fun onNext(t: LCObject) {
                t.put("picture", theurl);
                pushObject(t);

            }
        })
    }


    //保存图片
    fun savepicture(objectid: String, bitmap: ByteArray) {
        val file = LCFile("test", bitmap)
        file.saveInBackground().subscribe(object : Observer<LCFile> {
            override fun onSubscribe(disposable: Disposable) {}
            override fun onNext(file: LCFile) {
                pushpicutre(objectid, file.url)
                Log.e("文件保存完成。", "URL：" + file.url)
            }

            override fun onError(throwable: Throwable) {
                Log.e("文件保存失败。", throwable.toString())
                // 保存失败，可能是文件无法被读取，或者上传过程中出现问题
            }

            override fun onComplete() {}
        })

    }

    fun saveToImage(url: String, context: Context, imageView: ImageView) {
        if (url == "") {
            return
        }
        Glide.with(context)
            .load(url)
            .centerCrop()
            .into(imageView)
    }


    fun setImage(objectid: String, imageView: ImageView) {
        val query = LCQuery<LCObject>("userdata")
        query.whereEqualTo("userid", objectid)
        query.firstInBackground.subscribe(object : Observer<LCObject?> {
            override fun onSubscribe(disposable: Disposable) {}
            override fun onError(throwable: Throwable) {}
            override fun onComplete() {}
            override fun onNext(t: LCObject) {
                t.getString("picture")?.let {
                    saveToImage(t.getString("picture"), imageView.context, imageView);
                }
            }
        })

    }

    fun getAllUrlByObject(objectid: List<String>, callBackListUrl: CallBackListUrl) {
        val list: MutableList<LCQuery<LCObject>> = mutableListOf()
        objectid.forEach {
            val query = LCQuery<LCObject>("userdata")
            query.whereEqualTo("userid", it)
            list.add(query)
        }
        val query: LCQuery<LCObject> = LCQuery.or(list.toList())
        query.findInBackground().subscribe(object : Observer<List<LCObject>> {
            override fun onSubscribe(disposable: Disposable) {}
            override fun onError(throwable: Throwable) {}
            override fun onComplete() {}
            override fun onNext(t: List<LCObject>) {
                val list2: MutableList<String> = mutableListOf()
                t.forEach {
                    list2.add(it.getString("picture") ?: "")
                }
                callBackListUrl.success(list2)
            }
        })
    }


}