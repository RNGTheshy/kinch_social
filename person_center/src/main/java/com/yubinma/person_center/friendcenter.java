package com.yubinma.person_center;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chaoshan.data_center.SettingsPreferencesDataStore;
import com.chaoshan.data_center.togetname.Headport;

import cn.leancloud.chatkit.activity.LCIMConversationActivity;
import cn.leancloud.chatkit.utils.LCIMConstants;


public class friendcenter extends AppCompatActivity {
    final String classname = "userdata";
    final String myobjectid = SettingsPreferencesDataStore.INSTANCE.getCurrentUserObjetID();
    static String ifs="";
    static int dzan=0;
    //从其他页面跳转至该好友中心界面
    public static void startFriendcenter(Context context, String friendObjId){
        Intent intent = new Intent(context, friendcenter.class);
        intent.putExtra("objectid", friendObjId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendcenter);
        //获取从上个页面传送来的用户id
        Intent intent=getIntent();
        String objectid=intent.getStringExtra("objectid");

        //设置用户个人中心内容
        Personal_data personal_data = new Personal_data();
        TextView textView1 = findViewById(R.id.name99);
        TextView textView2 = findViewById(R.id.gender99);
        TextView textView3 = findViewById(R.id.birth99);
        TextView textView4 = findViewById(R.id.state99);
        TextView textView5 = findViewById(R.id.sign1111);

        //调用设置方法
        personal_data.setcenter(classname, objectid, textView1, textView3, textView2, textView4, textView5);
        Headport headport = new Headport();
        ImageView img=findViewById(R.id.heads99);
        headport.setImage(objectid, img);

        //给图片加圆角
        friendto friendto=new friendto();
        friendto.setRadius(img,15);

        //显示点赞和浏览
        personal_data.getbrowse(objectid, new Getbrowse() {
            @Override
            public void getbandt(int browse, int thumbsup) {
                TextView browse1=findViewById(R.id.fbrowse);
                dzan=thumbsup;
                int brow1=browse+1;
                browse1.setText(brow1+"");
                TextView thumpsup1=findViewById(R.id.fthumbsup);

                thumpsup1.setText(thumbsup+"");
                personal_data.setbrowse(objectid);
            }
        });

        //获取是否点赞
        personal_data.getthumbsup(myobjectid, objectid, new Getthumbsup() {
            @Override
            public void getthumbsup(String thumbsup) {
                TextView zan=findViewById(R.id.zan);
                if (thumbsup.equals("no")){
                    zan.setBackgroundResource(R.drawable.bzan);
                }
                else{
                    zan.setBackgroundResource(R.drawable.zan);
                }
                ifs=thumbsup;
            }
        });



        //设置朋友圈界面图片
        ImageView dt1=findViewById(R.id.dt4);
        headport.saveToImage("http://lc-WFB1URKd.cn-n1.lcfile.com/yIE0P8Nil2VQ7JCbHzmGyQM4oAhTzIXy/test", dt1.getContext(), dt1);
        ImageView dt2=findViewById(R.id.dt5);
        headport.saveToImage("http://lc-WFB1URKd.cn-n1.lcfile.com/opH930Idjxg4WTYCmCNYzbgpFg3Cwhzt/test", dt2.getContext(), dt2);
        ImageView dt3=findViewById(R.id.dt6);
        headport.saveToImage("http://lc-WFB1URKd.cn-n1.lcfile.com/AHwgtrGr75yjXsJ8tppmziwRbAWRAx8Q/test", dt3.getContext(), dt3);

        //给发消息按钮设立跳转到聊天页面的点击事件
        TextView tochat=findViewById(R.id.tochat);
        tochat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LCIMConversationActivity.startChatWithFriend(tochat.getContext(), objectid);
            }
        });

        //给返回按钮设立finish的点击事件
        TextView tofinish=findViewById(R.id.tttfini);
        tofinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        //点赞按钮的点击事件
        TextView zan=findViewById(R.id.zan);
        zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView zan=findViewById(R.id.zan);
                TextView thumpsup1=findViewById(R.id.fthumbsup);
                if(ifs.equals("no")){
                    ifs="yes";
                    zan.setBackgroundResource(R.drawable.zan);
                    dzan++;
                    thumpsup1.setText(dzan+"");
                    personal_data.thumbsup(objectid);
                }
                else
                {
                    ifs="no";
                    zan.setBackgroundResource(R.drawable.bzan);
                    dzan--;
                    thumpsup1.setText(dzan+"");
                    personal_data.cancel(objectid);
                }
                personal_data.setno(myobjectid, objectid,ifs);
            }
        });

    }
}
