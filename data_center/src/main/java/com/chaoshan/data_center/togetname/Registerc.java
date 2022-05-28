package com.chaoshan.data_center.togetname;

import cn.leancloud.LCObject;
import cn.leancloud.LCUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class Registerc {
    public void toregister(String login,String email,String password,Failregister failregister)
    {
        //创建用户
        LCUser user = new LCUser();

        //输入用户的账号密码和邮箱
        user.setUsername(login);
        user.setPassword(password);
        user.setEmail(email);

        user.signUpInBackground().subscribe(new Observer<LCUser>() {
            public void onSubscribe(Disposable disposable) {}
            public void onNext(LCUser user) {

                // 注册成功
                //为该用户创建相关信息表
                setdata(user.getObjectId().toString(),email);
                System.out.println("注册成功。objectId：" + user.getObjectId());
                failregister.success();
            }
            public void onError(Throwable throwable) {

                // 注册失败（通常是因为用户名已被使用）
                failregister.fail();
            }
            public void onComplete() {}
        });
    }


    private void setdata(String objectid,String email){//创建信息表
        // 构建对象
        LCObject todo = new LCObject("userdata");

        // 为属性赋默认值
        todo.put("name", "用户");
        todo.put("birthday", "2001-1-1");
        todo.put("gender", "男");
        todo.put("mobilePhoneNumber", "12345678910");
        todo.put("phone_number","12345678910");
        todo.put("signature","好好学习天天向上");
        todo.put("state","正在睡觉");
        todo.put("useremail",email);
        todo.put("userid",objectid);
        todo.put("picture","http://lc-WFB1URKd.cn-n1.lcfile.com/lL8lt7tkKOLue2vhRytGSxL5NWnhrmhk/test");

        // 将对象保存到云端
        todo.saveInBackground().subscribe(new Observer<LCObject>() {
            public void onSubscribe(Disposable disposable) {}
            public void onNext(LCObject todo) {

                // 成功保存之后，执行其他逻辑
                System.out.println("保存成功。objectId：" + todo.getObjectId());
            }
            public void onError(Throwable throwable) {
                // 异常处理
            }
            public void onComplete() {}
        });
    }
}
