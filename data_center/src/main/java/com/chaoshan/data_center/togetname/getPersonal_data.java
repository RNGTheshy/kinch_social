package com.chaoshan.data_center.togetname;

import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

class getPersonal_data {
    void center_getname(String objectid , center_getname callback) {
        LCQuery<LCObject> query = new LCQuery<>("userdata");
        query.whereEqualTo("userid", objectid);
        query.getFirstInBackground().subscribe(new Observer<LCObject>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }
            @Override
            public void onNext(LCObject todo) {

                String name = todo.getString("name");
                callback.getname(name);
            }
            @Override
            public void onError(Throwable throwable) {
            }
            @Override
            public void onComplete() {
            }
        });
    }
}
