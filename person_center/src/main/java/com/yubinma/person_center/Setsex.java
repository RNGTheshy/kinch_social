package com.yubinma.person_center;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cn.leancloud.LCObject;

public class Setsex extends AppCompatActivity {
    String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setsex);

        LinearLayout setmale = findViewById(R.id.setmale);
        LinearLayout setfemale = findViewById(R.id.setfemale);
        final TextView smale = findViewById(R.id.smale);
        final TextView sfemale = findViewById(R.id.sfemale);
        setmale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender = "男";
                smale.setText("√");
                sfemale.setText("");
            }
        });
        setfemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender = "女";
                sfemale.setText("√");
                smale.setText("");
            }
        });


        Button button = findViewById(R.id.backb2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gender.equals("")) {
                    Toast.makeText(Setsex.this, "请选择性别", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = getIntent();
                    final String objectid = intent.getStringExtra("objectid");

                    final String classname = intent.getStringExtra("classname");
                    Toast.makeText(Setsex.this, classname, Toast.LENGTH_SHORT).show();
                    LCObject todo = LCObject.createWithoutData(classname, objectid);

                    Personal_data personal_data = new Personal_data();
                    personal_data.saveGender(classname, objectid, gender);
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent tocenter = new Intent(Setsex.this, Setting.class);

                    tocenter.putExtra("objectid", objectid);
                    startActivity(tocenter);
                    finish();
                }
            }
        });


        TextView textView = findViewById(R.id.back2);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}