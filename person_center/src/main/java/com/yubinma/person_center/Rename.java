package com.yubinma.person_center;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cn.leancloud.LCObject;

public class Rename extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rename);

        Intent intent=getIntent();
        final String objectid=intent.getStringExtra("objectid");
        final String classname=intent.getStringExtra("classname");

       Button button=findViewById(R.id.backb1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText namee=findViewById(R.id.namee);
                String namee1=namee.getText().toString();
                if(namee1.isEmpty()!=true) {

                    LCObject todo = LCObject.createWithoutData(classname, objectid);
                    com.example.myapplication.Personal_data personal_data=new com.example.myapplication.Personal_data();
                    personal_data.saveId(classname,objectid,namee1);

                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent tocenter=new Intent(Rename.this, com.example.myapplication.Setting.class);
                    tocenter.putExtra("objectid", objectid);
                    startActivity(tocenter);
                    Toast.makeText(Rename.this,"修改成功",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(Rename.this,"姓名不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        TextView textView=findViewById(R.id.back1);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


}
}
