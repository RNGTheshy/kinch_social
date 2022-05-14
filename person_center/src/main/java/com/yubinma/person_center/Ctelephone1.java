package com.yubinma.person_center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Ctelephone1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ctelephone);
        Intent intent = getIntent();
        String telephonenumber = intent.getStringExtra("telephone");
        final String objectid = intent.getStringExtra("objectid");
        final String classname = intent.getStringExtra("classname");
        TextView textView = findViewById(R.id.phonetext1);
        textView.setText("更换手机号后，下次登陆可使用新手机号登录，当前手机号： " + telephonenumber);

        Button nextstap = findViewById(R.id.nextstap);
        nextstap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText newtelephone = findViewById(R.id.newtelephone);
                String newphonenumber = newtelephone.getText().toString();
                if (newphonenumber.length() != 11) {
                    Toast.makeText(Ctelephone1.this, "请输入正确的号码", Toast.LENGTH_SHORT).show();
                } else {
                    Intent tonext = new Intent(Ctelephone1.this, Ctelephone2.class);
                    tonext.putExtra("newphonenumber", newphonenumber);
                    tonext.putExtra("objectid", objectid);
                    tonext.putExtra("classname", classname);
                    startActivity(tonext);
                    finish();
                }
            }
        });

        TextView textView1 = findViewById(R.id.back4);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
