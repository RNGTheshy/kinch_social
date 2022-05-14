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
public class changestate  extends AppCompatActivity {
    String state = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cstate);

        LinearLayout state1 = findViewById(R.id.state1);
        LinearLayout state2 = findViewById(R.id.state2);
        LinearLayout state3 = findViewById(R.id.state3);
        final TextView sstate1 = findViewById(R.id.sstate1);
        final TextView sstate2 = findViewById(R.id.sstate2);
        final TextView sstate3 = findViewById(R.id.sstate3);
        state1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state="正在睡觉";
                sstate1.setText("√");
                sstate2.setText("");
                sstate3.setText("");
            }
        });
        state2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state="沉迷学习";
                sstate1.setText("");
                sstate2.setText("√");
                sstate3.setText("");
            }
        });
        state3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state="胡思乱想";
                sstate1.setText("");
                sstate2.setText("");
                sstate3.setText("√");
            }
        });
        Button stateback=findViewById(R.id.stateback);
        stateback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state.equals("")) {
                    Toast.makeText(changestate.this, "请选择状态", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = getIntent();
                    final String objectid = intent.getStringExtra("objectid");
                    final String classname = intent.getStringExtra("classname");
                    Personal_data personal_data = new Personal_data();
                    personal_data.saveState(classname, objectid, state);
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent tocenter = new Intent(changestate.this, PersonCenter2Activity.class);
                    tocenter.putExtra("objectid", objectid);
                    startActivity(tocenter);
                    finish();
                }
            }
        });




    }
    }
