package com.chaoshan.data_center.friend

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import cn.leancloud.LCLogger
import cn.leancloud.LCObject
import cn.leancloud.LCQuery
import cn.leancloud.LeanCloud
import com.chaoshan.data_center.SettingsPreferencesDataStore
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import junit.framework.TestCase
import org.junit.Test

class GetAllUerTest : TestCase() {

    @Test
    fun testGetAllUerDao() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        LeanCloud.initialize(
            context,
            "WFB1URKdIJqueBEjLd0P0xoy-gzGzoHsz",
            "9uuBkty0jB2T7HXyqDWmLOVj",
            "https://wfb1urkd.lc-cn-n1-shared.com"
        )
        LeanCloud.setLogLevel(LCLogger.Level.DEBUG);

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
                    print(it?.getString("objectId").toString())
                }
            }
        })
    }

    fun testGetUerDaoByNameOrId() {}

    fun testAddFriendWithMessage() {}

    fun testAddFriend() {}

    fun testGetAllFriend() {}
}