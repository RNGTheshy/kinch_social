package com.yubinma.person_center;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chaoshan.data_center.SettingsPreferencesDataStore;

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
            Toast.makeText(PersonCenter2Activity.this, objectid, Toast.LENGTH_SHORT).show();



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

