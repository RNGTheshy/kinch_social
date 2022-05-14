package com.yubinma.person_center;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.ByteBuffer;

import cn.leancloud.LCFile;
import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.LCUser;
import cn.leancloud.types.LCNull;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable
class Personal_data {
    String[] data = new String[5];



    //改id
    void saveId(String classname, String objectid, final String name) {
        LCQuery<LCObject> query = new LCQuery<>(classname);
        query.whereEqualTo("userid", objectid);
        query.getFirstInBackground().subscribe(new Observer<LCObject>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }
            @Override
            public void onNext(LCObject todo) {
                todo.put("name", name);
                todo.saveInBackground().subscribe(new Observer<LCObject>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                    }

                    @Override
                    public void onNext(LCObject savedTodo) {
                        Log.e("修改完成。", "succeed");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("修改失败。", throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onComplete() {
            }
        });

    }

    //改密码
//    void savePassword(LCObject todo, String password){
//        todo.put("password", password);
//        todo.saveInBackground().subscribe(new Observer<LCObject>() {
//            @Override
//            public void onSubscribe(Disposable disposable) {}
//            @Override
//            public void onNext(LCObject savedTodo) {
//                Log.e("修改完成。", "succeed");
//            }
//            @Override
//            public void onError(Throwable throwable) {
//                Log.e("修改失败。", throwable.toString());
//            }
//            @Override
//            public void onComplete() {}
//        });
//    }

    //改生日
    void saveBirth(String classname, String objectid, final String birthday) {
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
                        Log.e("修改完成。", "succeed");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("修改失败。", throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onComplete() {
            }
        });

    }

    //改邮箱
    void saveemail(LCObject todo, String email) {
        todo.put("email", email);
        todo.saveInBackground().subscribe(new Observer<LCObject>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }

            @Override
            public void onNext(LCObject savedTodo) {
                Log.e("修改完成。", "succeed");
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e("修改失败。", throwable.toString());
            }

            @Override
            public void onComplete() {
            }
        });
    }

    //改电话号
    void saveTelephone(String classname, String objectid, final String telephone) {
        LCQuery<LCObject> query = new LCQuery<>(classname);
        query.whereEqualTo("userid", objectid);
        query.getFirstInBackground().subscribe(new Observer<LCObject>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }

            @Override
            public void onNext(LCObject todo) {
                todo.put("mobilePhoneNumber", telephone);
                todo.saveInBackground().subscribe(new Observer<LCObject>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                    }

                    @Override
                    public void onNext(LCObject savedTodo) {
                        Log.e("修改完成。", "succeed");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("修改失败。", throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onComplete() {
            }
        });

    }

    //改性别
    void saveGender(String classname, String objectid, final String gender) {
        LCQuery<LCObject> query = new LCQuery<>(classname);
        query.whereEqualTo("userid", objectid);
        query.getFirstInBackground().subscribe(new Observer<LCObject>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }

            @Override
            public void onNext(LCObject todo) {
                // todo 是第一个满足条件的 Todo 对象
                todo.put("gender", gender);
                todo.saveInBackground().subscribe(new Observer<LCObject>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                    }

                    @Override
                    public void onNext(LCObject savedTodo) {
                        System.out.println("okkkk");
                        Log.e("修改完成。", "succeed");
                    }

                    @Override
                    public void onError(Throwable throwable) {

                        System.out.println("error");
                        Log.e("修改失败。", throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
            }

            public void onError(Throwable throwable) {
            }

            public void onComplete() {
            }
        });


    }


    //上传图片
    void savePicture(LCObject todo, Bitmap bitmap) {
        int bytes = bitmap.getByteCount();
        ByteBuffer buf = ByteBuffer.allocate(bytes);
        bitmap.copyPixelsToBuffer(buf);
        byte[] byteArray = buf.array();
        LCFile file = new LCFile("picture", byteArray);
        file.saveInBackground().subscribe(new Observer<LCFile>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(LCFile lcFile) {
                Log.e("修改完成。", "success");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("修改失败。", e.toString());
            }

            @Override
            public void onComplete() {
            }
        });
    }

    public void getdata(String classname, String objectid, final TextView textView0,
                        final TextView textView1, final TextView textView2, final TextView textView3, final TextView textView4) {

        LCQuery<LCObject> query = new LCQuery<>(classname);
        query.whereEqualTo("userid", objectid);
        query.getFirstInBackground().subscribe(new Observer<LCObject>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }

            @Override
            public void onNext(LCObject todo) {
                textView0.setText(todo.getString("name"));
                textView1.setText(todo.getString("gender"));
                textView2.setText(todo.getString("birthday"));
                textView3.setText(todo.getString("phone_number"));
                textView4.setText(todo.getString("password"));
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    void setPassword(String objectid) {
         LCQuery<LCObject> query = new LCQuery<>("userdata");
        query.whereEqualTo("userid", objectid);
        query.getFirstInBackground().subscribe(new Observer<LCObject>() {
            @Override
            public void onSubscribe(Disposable disposable) {}
            @Override
            public void onNext(LCObject todo) {

                LCUser.requestPasswordResetInBackground(todo.getString("useremail")).subscribe(new Observer<LCNull>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {}
                    @Override
                    public void onNext(LCNull lcNull) {
                        // 成功调用
                        Log.e("修改完成。", "success");
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        // 调用出错
                        Log.e("修改失败2。", throwable.toString());
                    }
                    @Override
                    public void onComplete() {}
                });
            }
            @Override
            public void onError(Throwable throwable) {}
            @Override
            public void onComplete() {}
        });
    }


    //改密码
    void changePassword(String email) {
        LCUser.requestPasswordResetInBackground(email).subscribe(new Observer<LCNull>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }

            @Override
            public void onNext(LCNull lcNull) {
                // 成功调用
                Log.e("修改完成。", "success");
            }

            @Override
            public void onError(Throwable throwable) {
                // 调用出错
                Log.e("修改失败2。", throwable.toString());
            }

            @Override
            public void onComplete() {
            }
        });
    }


    public void setcenter(String classname, String objectid, final TextView textView0,
                          final TextView textView1, final TextView textView2, final TextView textView3, final TextView textView4) {

        LCQuery<LCObject> query = new LCQuery<>(classname);
        query.whereEqualTo("userid", objectid);
        query.getFirstInBackground().subscribe(new Observer<LCObject>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }

            @Override
            public void onNext(LCObject todo) {
                // todo 是第一个满足条件的 Todo 对象
                textView0.setText(todo.getString("name"));
                textView2.setText(todo.getString("gender"));
                textView1.setText(todo.getString("birthday"));
                textView3.setText("状态：" + todo.getString("state"));
                textView4.setText(todo.getString("signature"));
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
