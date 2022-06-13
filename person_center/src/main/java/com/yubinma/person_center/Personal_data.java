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
import io.reactivex.disposables.Disposable;
public class Personal_data {
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
    public void saveTelephone(String classname, String objectid, final String telephone) {
        LCQuery<LCObject> query = new LCQuery<>(classname);
        query.whereEqualTo("userid", objectid);
        query.getFirstInBackground().subscribe(new Observer<LCObject>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }

            @Override
            public void onNext(LCObject todo) {
                todo.put("mobilePhoneNumber", telephone);
                todo.put("phone_number", telephone);
                todo.saveInBackground().subscribe(new Observer<LCObject>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {System.out.println("测试成功");
                    }

                    @Override
                    public void onNext(LCObject savedTodo) {
                        System.out.println("修改成功2");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("修改失败");
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

    //改状态
    void saveState(String classname, String objectid, final String state) {
        LCQuery<LCObject> query = new LCQuery<>(classname);
        query.whereEqualTo("userid", objectid);
        query.getFirstInBackground().subscribe(new Observer<LCObject>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }
            @Override
            public void onNext(LCObject todo) {
                // todo 是第一个满足条件的 Todo 对象
                todo.put("state", state);
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

    //改状态
    void savesign(String classname, String objectid, final String sign) {
        LCQuery<LCObject> query = new LCQuery<>(classname);
        query.whereEqualTo("userid", objectid);
        query.getFirstInBackground().subscribe(new Observer<LCObject>() {
            @Override
            public void onSubscribe(Disposable disposable) {}
            @Override
            public void onNext(LCObject todo) {
                // todo 是第一个满足条件的 Todo 对象
                todo.put("signature", sign);
                todo.saveInBackground().subscribe(new Observer<LCObject>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {}
                    @Override
                    public void onNext(LCObject savedTodo) {
                        System.out.println("okkkk");
                        Log.e("修改完成。", "succeed");}
                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("error");
                        Log.e("修改失败。", throwable.toString());
                    }

                    @Override
                    public void onComplete() {}
                });
            }

            public void onError(Throwable throwable) {
            }

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


    //改经纬度
    public void saveplace(double longitude,double latitude,String objectid) {
        LCQuery<LCObject> query = new LCQuery<>("userdata");
        query.whereEqualTo("userid", objectid);
        query.getFirstInBackground().subscribe(new Observer<LCObject>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }
            @Override
            public void onNext(LCObject todo) {
                todo.put("latitude", latitude);
                todo.put("longitude", longitude);
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
            public void onError(Throwable throwable) {}
            @Override
            public void onComplete() {}
        });


    }







    //浏览
    public void setbrowse(String objectid){
        LCQuery<LCObject> query = new LCQuery<>("userdata");
        query.whereEqualTo("userid", objectid);
        query.getFirstInBackground().subscribe(new Observer<LCObject>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }
            @Override
            public void onNext(LCObject todo) {
                int browse=todo.getInt("browse")+1;
                todo.put("browse", browse);
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
            public void onError(Throwable throwable) {}
            @Override
            public void onComplete() {}
        });

    }

    //获取浏览次数与点赞次数
    public void getbrowse(String objectid,Getbrowse getbrowse){
        LCQuery<LCObject> query = new LCQuery<>("userdata");
        query.whereEqualTo("userid", objectid);
        query.getFirstInBackground().subscribe(new Observer<LCObject>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }
            @Override
            public void onNext(LCObject todo) {
                int browse=todo.getInt("browse");
                int thumbsup=todo.getInt("thumbsup");
                getbrowse.getbandt(browse,thumbsup);
            }
            @Override
            public void onError(Throwable throwable) {}
            @Override
            public void onComplete() {}
        });
    }






    //点赞
    public void thumbsup(String objectid){
        LCQuery<LCObject> query = new LCQuery<>("userdata");
        query.whereEqualTo("userid", objectid);
        query.getFirstInBackground().subscribe(new Observer<LCObject>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }
            @Override
            public void onNext(LCObject todo) {
                int thumbsup=todo.getInt("thumbsup")+1;
                todo.put("thumbsup", thumbsup);
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
            public void onError(Throwable throwable) {}
            @Override
            public void onComplete() {}
        });

    }

    //取消点赞
    public void cancel(String objectid){
        LCQuery<LCObject> query = new LCQuery<>("userdata");
        query.whereEqualTo("userid", objectid);
        query.getFirstInBackground().subscribe(new Observer<LCObject>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }
            @Override
            public void onNext(LCObject todo) {
                int thumbsup=todo.getInt("thumbsup")-1;
                todo.put("thumbsup", thumbsup);
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
            public void onError(Throwable throwable) {}
            @Override
            public void onComplete() {}
        });
    }


    //好友点赞获取
    public void getthumbsup(String objectid1,String objectid2,Getthumbsup getthumbsup){
        LCQuery<LCObject> query = new LCQuery<>("FriendList");
        query.whereEqualTo("fId", objectid1);
        query.whereEqualTo("mId", objectid2);
        query.getFirstInBackground().subscribe(new Observer<LCObject>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }
            @Override
            public void onNext(LCObject todo) {
                String ifthumbsup=todo.getString("ifthumbsup");
                getthumbsup.getthumbsup(ifthumbsup);
            }
            @Override
            public void onError(Throwable throwable) {}
            @Override
            public void onComplete() {}
        });
    }

    //好友点赞修改
    public void setno(String objectid1,String objectid2,String ifs){
        LCQuery<LCObject> query = new LCQuery<>("FriendList");
        query.whereEqualTo("fId", objectid1);
        query.whereEqualTo("mId", objectid2);
        query.getFirstInBackground().subscribe(new Observer<LCObject>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }
            @Override
            public void onNext(LCObject todo) {
                if (ifs.equals("yes")){
                    todo.put("ifthumbsup","yes");
                }
                else{
                    todo.put("ifthumbsup","no");
                }
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
            public void onError(Throwable throwable) {}
            @Override
            public void onComplete() {}
        });
    }




}
