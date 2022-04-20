package com.yubinma.app_debug

import androidx.multidex.MultiDexApplication
import cn.leancloud.LCLogger
import cn.leancloud.LeanCloud
import cn.leancloud.LCObject


class DebugApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        LeanCloud.initialize(
            this,
            "WFB1URKdIJqueBEjLd0P0xoy-gzGzoHsz",
            "9uuBkty0jB2T7HXyqDWmLOVj",
            "https://wfb1urkd.lc-cn-n1-shared.com"
        )
        LeanCloud.setLogLevel(LCLogger.Level.DEBUG);
    }
}