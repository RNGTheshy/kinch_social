package com.yubinma.person_center;
import static java.lang.Thread.sleep;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cn.leancloud.LCObject;
import androidx.appcompat.app.AppCompatActivity;

public class csign extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.csign);
        Button savesign=findViewById(R.id.savesign);
        savesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText=findViewById(R.id.sign);
                String sign=editText.getText().toString();
                Intent intent=getIntent();
                final String objectid = intent.getStringExtra("objectid");
                final String classname = intent.getStringExtra("classname");
                Personal_data personal_data = new Personal_data();
                personal_data.savesign(classname, objectid, sign);
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent tocenter = new Intent(csign.this, PersonCenter2Activity.class);
                startActivity(tocenter);
                Toast.makeText(csign.this, "修改成功", Toast.LENGTH_SHORT).show();
                finish();


            }
        });
        TextView back=findViewById(R.id.signback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }
}
