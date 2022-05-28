package com.chaoshan.login;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chaoshan.data_center.togetname.Failregister;
import com.chaoshan.data_center.togetname.Registerc;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register_activity);


        TextView register=findViewById(R.id.toregister);//注册按钮
        register.setOnClickListener(new View.OnClickListener() {//对注册按钮设置点击事件
            @Override
            public void onClick(View view) {

                //获得输入的账号
                EditText createlog=findViewById(R.id.createlog);
                String login=createlog.getText().toString();

                //获得输入的邮箱
                EditText createemail=findViewById(R.id.createemail);
                String email=createemail.getText().toString();

                //获得输入的密码
                EditText passw=findViewById(R.id.passw);
                String password=passw.getText().toString();

                //获得重新输入的密码
                passw=findViewById(R.id.passw1);
                String repassword=passw.getText().toString();

                //判断输入密码是否为空
                if (login.equals(""))
                {
                    Toast.makeText(RegisterActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
                }

                //判断输入邮箱是否为空
                else if(email.equals(""))
                {
                    Toast.makeText(RegisterActivity.this, "请输入邮箱", Toast.LENGTH_SHORT).show();
                }

                //密码非空
                else if(password.equals("")&&repassword.equals(""))
                {
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                }

                //判断两次输入密码是否相同
                else if(!password.equals(repassword)){
                    Toast.makeText(RegisterActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                }

                //确认密码长度
                else if(password.length()<6||password.length()>15)
                {
                    Toast.makeText(RegisterActivity.this, "密码长度应为6-15位", Toast.LENGTH_SHORT).show();
                }

                //都符合条件则进行用户创建
                else
                {
                    Registerc registerc=new Registerc();
                    registerc.toregister(login, email, password, new Failregister() {

                        //注册失败
                        @Override
                        public void fail() {
                            Toast.makeText(RegisterActivity.this, "该用户名或邮箱已被使用", Toast.LENGTH_SHORT).show();
                        }

                        //注册成功
                        @Override
                        public void success() {
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }

            }
        });


    }
}
