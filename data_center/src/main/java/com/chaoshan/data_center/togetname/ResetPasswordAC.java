package com.chaoshan.data_center.togetname;


import cn.leancloud.LCUser;
import cn.leancloud.types.LCNull;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ResetPasswordAC {

    //发送重置密码邮件
    public void message(String email,Failregister failregister){
        LCUser.requestPasswordResetInBackground(email).subscribe(new Observer<LCNull>() {
            public void onSubscribe(Disposable disposable) {}

            //发送成功
            @Override
            public void onNext(LCNull lcNull) {
                failregister.success();
            }

            //发送失败
            @Override
            public void onError(Throwable throwable) {
                // 调用出错
                failregister.fail();
            }
            @Override
            public void onComplete() {}
        }
        );


    }
}
