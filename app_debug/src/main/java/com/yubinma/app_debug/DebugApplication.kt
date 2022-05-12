package com.yubinma.app_debug

import android.util.Log
import androidx.multidex.MultiDexApplication
import cn.leancloud.LCInstallation
import cn.leancloud.LCLogger
import cn.leancloud.LeanCloud
import com.chaoshan.login.GetApplicationContext
import com.chaoshan.login.SettingsPreferencesDataStore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import cn.leancloud.chatkit.LCChatKit
import cn.leancloud.im.LCIMOptions
import cn.leancloud.push.PushService
import com.example.chat.ChatActivity
import com.example.chat.CustomUserProvider


class DebugApplication : MultiDexApplication() {
    companion object {
        const val USER_NAME = "user_name"
    }

    @DelicateCoroutinesApi
    override fun onCreate() {
        super.onCreate()
        LeanCloud.initialize(
            this,
            "WFB1URKdIJqueBEjLd0P0xoy-gzGzoHsz",
            "9uuBkty0jB2T7HXyqDWmLOVj",
            "https://wfb1urkd.lc-cn-n1-shared.com"
        )
        LeanCloud.setLogLevel(LCLogger.Level.DEBUG);
        GetApplicationContext.context = this.applicationContext
        // 暂时测试时保留用户的ID
        GlobalScope.launch {
            SettingsPreferencesDataStore.saveData(
                this@DebugApplication.applicationContext,
                "6279d9b47a6d3118ac0283c5",
                USER_NAME
            )
        }
        // 获取数据
        GetApplicationContext.context?.let {
            GlobalScope.launch {
                Log.e("nameTest", SettingsPreferencesDataStore.getName(it, "UserName"))
            }
        }

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






