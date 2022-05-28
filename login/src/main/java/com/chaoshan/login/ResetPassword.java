package com.chaoshan.login;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chaoshan.data_center.togetname.Failregister;
import com.chaoshan.data_center.togetname.ResetPasswordAC;

//忘记密码
public class ResetPassword extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_find_password_activity);

        //设定按键的点击事件
        TextView sendmess=findViewById(R.id.toreset);
        sendmess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //获得输入的邮箱
                EditText themail=findViewById(R.id.torepass100);
                String email=themail.getText().toString();
                //判断输入是否为空
                if(email.equals("")){
                    Toast.makeText(ResetPassword.this, "请输入密码", Toast.LENGTH_SHORT).show();
                }


                else{
                    ResetPasswordAC resetPasswordAC=new ResetPasswordAC();
                    resetPasswordAC.message(email, new Failregister() {
                        //发送失败
                        @Override
                        public void fail() {
                            Toast.makeText(ResetPassword.this, "邮箱错误，请重新输入", Toast.LENGTH_SHORT).show();

                        }

                        //发送成功
                        @Override
                        public void success() {
                            Toast.makeText(ResetPassword.this, "已向邮箱发送邮件", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }


            }
        });




    }
}
