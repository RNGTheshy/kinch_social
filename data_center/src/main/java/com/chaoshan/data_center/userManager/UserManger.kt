package com.chaoshan.data_center.userManager

import android.util.Log
import cn.leancloud.LCUser
import com.chaoshan.data_center.GetApplicationContext
import com.chaoshan.data_center.SettingsPreferencesDataStore
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object UserManger {

    @DelicateCoroutinesApi
    fun login(id: String, password: String, callBack: LoginCallBack) {
        LCUser.logIn(id, password)
            .subscribe(object : Observer<LCUser?> {
                override fun onComplete() {}
                override fun onNext(t: LCUser) {
                    Log.e("UserManger", t.toJSONString())

                    GetApplicationContext.context?.let {
                        GlobalScope.launch {
                            SettingsPreferencesDataStore.saveData(
                                GetApplicationContext.context!!,
                                t.objectId,
                                SettingsPreferencesDataStore.USER_NAME,
                            )
                            Log.e(
                                "nameTest",
                                SettingsPreferencesDataStore.getName(
                                    GetApplicationContext.context!!,
                                    SettingsPreferencesDataStore.USER_NAME
                                )
                            )
                        }
                    }
                    callBack.success()
                }

                override fun onError(e: Throwable) {
                    callBack.fail()
                }

                override fun onSubscribe(d: Disposable) {
                }
            })

    }

}