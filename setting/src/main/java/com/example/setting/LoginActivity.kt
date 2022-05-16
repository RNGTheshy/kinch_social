package com.example.setting

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chaoshan.data_center.activitymanger.ActivityManager
import com.chaoshan.data_center.userManager.LoginCallBack
import com.chaoshan.data_center.userManager.UserManger
import com.example.setting.databinding.LoginMainActivityBinding


class LoginActivity : AppCompatActivity(), ActivityManager.IRecordPage,
    ActivityManager.IRecordPage2 {
    private lateinit var binding: LoginMainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginMainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {

        binding.login.setOnClickListener {
            UserManger.login(
                binding.editText.text.toString(),
                binding.editText2.text.toString(),
                object : LoginCallBack {
                    override fun success() {
                        Toast.makeText(this@LoginActivity, "登录成功", Toast.LENGTH_SHORT).show()
                        finish()
                    }

                    override fun fail() {
                        Toast.makeText(this@LoginActivity, "登录失败", Toast.LENGTH_SHORT).show()
                    }

                }
            )
        }
    }
}