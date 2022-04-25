package com.yubinma.app_debug

import androidx.multidex.MultiDexApplication
import cn.leancloud.LCInstallation
import cn.leancloud.LCLogger
import cn.leancloud.LeanCloud
import cn.leancloud.chatkit.LCChatKit
import cn.leancloud.im.LCIMOptions
import cn.leancloud.push.PushService
import com.example.chat.ChatActivity
import com.example.chat.CustomUserProvider


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
        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance())
        LCIMOptions.getGlobalOptions().setDisableAutoLogin4Push(true)
        LCChatKit.getInstance().init(this,
            "WFB1URKdIJqueBEjLd0P0xoy-gzGzoHsz",
            "9uuBkty0jB2T7HXyqDWmLOVj",
            "https://wfb1urkd.lc-cn-n1-shared.com")

        PushService.setDefaultPushCallback(this, ChatActivity::class.java)
        PushService.setAutoWakeUp(true)
        PushService.setDefaultChannelId(this, "default")
        LCInstallation.getCurrentInstallation().saveInBackground().subscribe{
            val installationId = LCInstallation.getCurrentInstallation().installationId
            println("---  $installationId")
        }
    }
}






