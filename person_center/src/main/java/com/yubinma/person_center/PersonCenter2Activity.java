package com.yubinma.person_center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chaoshan.data_center.SettingsPreferencesDataStore;
import com.chaoshan.data_center.togetname.Headport;

import cn.leancloud.LeanCloud;

public class PersonCenter2Activity extends AppCompatActivity {
    final String classname = "userdata";
    final String objectid = SettingsPreferencesDataStore.INSTANCE.getCurrentUserObjetID();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.center);
//        System.out.println("hello");
        LeanCloud.initialize(this, "WFB1URKdIJqueBEjLd0P0xoy-gzGzoHsz", "9uuBkty0jB2T7HXyqDWmLOVj", "https://wfb1urkd.lc-cn-n1-shared.com");

            Personal_data personal_data = new Personal_data();
            TextView textView1 = findViewById(R.id.name100);
            TextView textView2 = findViewById(R.id.gender100);
            TextView textView3 = findViewById(R.id.birth100);
            TextView textView4 = findViewById(R.id.state100);
            TextView textView5 = findViewById(R.id.sign11);
            personal_data.setcenter(classname, objectid, textView1, textView3, textView2, textView4, textView5);
        Headport headport = new Headport();
        ImageView img=findViewById(R.id.heads);
        headport.setImage(objectid, img);
        friendto friendto=new friendto();
        friendto.setRadius(img,15);


        //暂时设置的好友框
        ImageView friend1=findViewById(R.id.friend1);
        headport.setImage("6280f0b74fb5b8572d28e623",friend1);
        friendto.setRadius(friend1,15);
        ImageView friend2=findViewById(R.id.friend2);
        headport.setImage("6280ee567a6d3118ac0d0352",friend2);
        friendto.setRadius(friend2,15);
        ImageView friend3=findViewById(R.id.friend3);
        headport.setImage("626684f51c11246b6f372cd2",friend3);
        friendto.setRadius(friend3,15);
        ImageView dt1=findViewById(R.id.dt1);
        headport.saveToImage("http://lc-WFB1URKd.cn-n1.lcfile.com/yIE0P8Nil2VQ7JCbHzmGyQM4oAhTzIXy/test", dt1.getContext(), dt1);
        ImageView dt2=findViewById(R.id.dt2);
        headport.saveToImage("http://lc-WFB1URKd.cn-n1.lcfile.com/opH930Idjxg4WTYCmCNYzbgpFg3Cwhzt/test", dt2.getContext(), dt2);
        ImageView dt3=findViewById(R.id.dt3);
        headport.saveToImage("http://lc-WFB1URKd.cn-n1.lcfile.com/AHwgtrGr75yjXsJ8tppmziwRbAWRAx8Q/test", dt3.getContext(), dt3);





        personal_data.getbrowse(objectid, new Getbrowse() {
            @Override
            public void getbandt(int browse, int thumbsup) {
                TextView browse1=findViewById(R.id.mybrowse);
                browse1.setText(browse+"");
                TextView thumpsup1=findViewById(R.id.mythumbsup);
                thumpsup1.setText(thumbsup+"");

            }
        });






        GridLayout tosetting1 = findViewById(R.id.turnto1);
        tosetting1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonCenter2Activity.this, Setting.class);
                intent.putExtra("objectid", objectid);
                startActivity(intent);
            }
        });
        GridLayout tosetting2 = findViewById(R.id.turnto2);
        tosetting2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonCenter2Activity.this, Setting.class);
                intent.putExtra("objectid", objectid);
                startActivity(intent);
            }
        });
        TextView cstate=findViewById(R.id.state100);
        cstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonCenter2Activity.this, changestate.class);
                intent.putExtra("classname", classname);
                intent.putExtra("objectid", objectid);
                startActivity(intent);
            }
        });
        GridLayout csignn=findViewById(R.id.sign100);
        csignn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonCenter2Activity.this, csign.class);
                intent.putExtra("classname", classname);
                intent.putExtra("objectid", objectid);
                startActivity(intent);
            }
        });

        TextView deletemyself=findViewById(R.id.deletemyself);
        deletemyself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }


}

