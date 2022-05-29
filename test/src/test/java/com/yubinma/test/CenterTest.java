package com.yubinma.test;

import android.util.Log;

import com.chaoshan.data_center.friend.GetAllDataListener;
import com.chaoshan.data_center.togetname.Personal_data2;
import junit.framework.TestCase;
import org.junit.Test;


import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.core.LeanCloud;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CenterTest {
    CenterTest()
    {
        LeanCloud.initialize(
                "WFB1URKdIJqueBEjLd0P0xoy-gzGzoHsz",
                "9uuBkty0jB2T7HXyqDWmLOVj",
                "https://wfb1urkd.lc-cn-n1-shared.com"
        );
    }


    @Test
    public void testsavebirth(){
        saveBirth("userdata","629373a90534fd5ba0adbc78","1999-1-1");
    }

    //改生日
    public void saveBirth(String classname, String objectid, final String birthday) {
        LCQuery<LCObject> query = new LCQuery<>(classname);
        query.whereEqualTo("userid", objectid);
        query.getFirstInBackground().subscribe(new Observer<LCObject>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }

            @Override
            public void onNext(LCObject todo) {
                todo.put("birthday", birthday);
                todo.saveInBackground().subscribe(new Observer<LCObject>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                    }

                    @Override
                    public void onNext(LCObject savedTodo) {

                        System.out.println("修改完成");
                        Log.e("修改完成。", "succeed");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("修改失败。", throwable.toString());
                        System.out.println("修改失败2");
                    }

                    @Override
                    public void onComplete() {
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("修改失败1");
            }

            @Override
            public void onComplete() {
            }
        });

    }


}
