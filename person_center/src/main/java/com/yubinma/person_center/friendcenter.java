package com.yubinma.person_center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.chaoshan.data_center.togetname.Headport;

import cn.leancloud.chatkit.activity.LCIMConversationActivity;


public class friendcenter extends AppCompatActivity {
    final String classname = "userdata";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendcenter);




        Intent intent=getIntent();
        String objectid=intent.getStringExtra("objectid");


        Personal_data personal_data = new Personal_data();
        TextView textView1 = findViewById(R.id.name99);
        TextView textView2 = findViewById(R.id.gender99);
        TextView textView3 = findViewById(R.id.birth99);
        TextView textView4 = findViewById(R.id.state99);
        TextView textView5 = findViewById(R.id.sign99);
        personal_data.setcenter(classname, objectid, textView1, textView3, textView2, textView4, textView5);
        Headport headport = new Headport();
        ImageView img=findViewById(R.id.heads99);
        headport.setImage(objectid, img);
        friendto friendto=new friendto();
        friendto.setRadius(img,15);




        ImageView dt1=findViewById(R.id.dt4);
        headport.saveToImage("http://lc-WFB1URKd.cn-n1.lcfile.com/yIE0P8Nil2VQ7JCbHzmGyQM4oAhTzIXy/test", dt1.getContext(), dt1);
        ImageView dt2=findViewById(R.id.dt5);
        headport.saveToImage("http://lc-WFB1URKd.cn-n1.lcfile.com/opH930Idjxg4WTYCmCNYzbgpFg3Cwhzt/test", dt2.getContext(), dt2);
        ImageView dt3=findViewById(R.id.dt6);
        headport.saveToImage("http://lc-WFB1URKd.cn-n1.lcfile.com/AHwgtrGr75yjXsJ8tppmziwRbAWRAx8Q/test", dt3.getContext(), dt3);

        TextView tochat=findViewById(R.id.tochat);
        tochat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LCIMConversationActivity.startChatWithFriend(tochat.getContext(), objectid);
            }
        });


    }
}
