package com.yubinma.person_center;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.LCUser;
import cn.leancloud.types.LCNull;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class Cpassword extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cpassword);

        Button button=findViewById(R.id.backb5);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=getIntent();
                String objectid=intent.getStringExtra("objectid");
                Toast.makeText(Cpassword.this,objectid,Toast.LENGTH_SHORT).show();
                Personal_data personal_data = new Personal_data();
                personal_data.setPassword(objectid);






                System.out.println();
                // changePassword(todo.getString("email"));


            }
        });


//                Toast.makeText(Cpassword.this,objectid,Toast.LENGTH_SHORT).show();
//                Personal_data personal_data=new Personal_data();
//                personal_data.setPassword(objectid);


        TextView textView=findViewById(R.id.back5);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}