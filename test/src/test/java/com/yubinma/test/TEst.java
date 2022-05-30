package com.yubinma.test;

import android.util.Log;

import androidx.annotation.NonNull;

import org.junit.Test;

import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.core.LeanCloud;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class TEst {
    public TEst() {
        LeanCloud.initialize(
                "WFB1URKdIJqueBEjLd0P0xoy-gzGzoHsz",
                "9uuBkty0jB2T7HXyqDWmLOVj",
                "https://wfb1urkd.lc-cn-n1-shared.com"
        );
    }


    @Test
    public void testsavebirth() throws InterruptedException {
        saveBirth("userdata", "629373a90534fd5ba0adbc78", "1999-1-1");
    }

    //改生日
    public void saveBirth(String classname, String objectid, final String birthday) throws InterruptedException {
        LCQuery<LCObject> query = new LCQuery<>(classname);
        query.whereEqualTo("userid", objectid);
        query.getFirstInBackground().subscribe(new Observer<LCObject>() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {
            }

            @Override
            public void onNext(@NonNull LCObject todo) {
                System.out.println("修改失败1");
                LeanCloud.initialize(
                        "WFB1URKdIJqueBEjLd0P0xoy-gzGzoHsz",
                        "9uuBkty0jB2T7HXyqDWmLOVj",
                        "https://wfb1urkd.lc-cn-n1-shared.com"
                );
                todo.put("birthday", birthday);
                todo.saveInBackground().subscribe(new Observer<LCObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                    }

                    @Override
                    public void onNext(@NonNull LCObject savedTodo) {

                        System.out.println("修改完成");
                        Log.e("修改完成。", "succeed");
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        Log.e("修改失败。", throwable.toString());
                        System.out.println("修改失败2");
                    }

                    @Override
                    public void onComplete() {
                    }
                });

            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                System.out.println("修改失败1");
            }

            @Override
            public void onComplete() {
                System.out.println("修改失败1");
            }
        });



    }
}
