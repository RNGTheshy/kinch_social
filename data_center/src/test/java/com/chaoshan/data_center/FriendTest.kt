package com.chaoshan.data_center

import cn.leancloud.LCLogger
import cn.leancloud.LCObject
import cn.leancloud.LeanCloud
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
}