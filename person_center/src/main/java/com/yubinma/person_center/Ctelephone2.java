package com.yubinma.person_center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cn.leancloud.LCObject;

public class Ctelephone2 extends AppCompatActivity {
    String code="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ctelephone2);
        Button button=findViewById(R.id.ctelephonefinish);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText=findViewById(R.id.checkcode);
                code=editText.getText().toString();

                if(code.length()!=6){
                    Toast.makeText(Ctelephone2.this,"验证码错误",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent=getIntent();
                    String newtelephone=intent.getStringExtra("newphonenumber").toString();
                    final String objectid=intent.getStringExtra("objectid");
                    final String classname=intent.getStringExtra("classname");
                    LCObject todo = LCObject.createWithoutData(classname, objectid);
                    Personal_data personal_data=new Personal_data();
                    personal_data.saveTelephone(classname,objectid,newtelephone);

                    Intent tocenter=new Intent(Ctelephone2.this,Setting.class);
                    tocenter.putExtra("objectid", objectid);
                    startActivity(tocenter);
                    finish();
                }
            }
        });
        TextView textView1=findViewById(R.id.reback4);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



}
