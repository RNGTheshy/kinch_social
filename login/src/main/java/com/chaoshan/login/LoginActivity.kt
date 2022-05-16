package com.chaoshan.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chaoshan.data_center.GetApplicationContext
import com.chaoshan.data_center.activitymanger.ActivityManager
import com.chaoshan.data_center.userManager.LoginCallBack
import com.chaoshan.data_center.userManager.UserManger
import com.chaoshan.login.databinding.LoginMainActivityBinding
import com.example.kinch_home.Home_Activity
import kotlinx.coroutines.DelicateCoroutinesApi


class LoginActivity : AppCompatActivity(), ActivityManager.IRecordPage {
    lateinit var binding: LoginMainActivityBinding

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginMainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    @DelicateCoroutinesApi
    private fun initView() {
        binding.forgetPassword.setOnClickListener {
            val intent = Intent(this, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }

        binding.login.setOnClickListener {
            UserManger.login(
                binding.editText.text.toString(),
                binding.editText2.text.toString(),
                object : LoginCallBack {
                    override fun success() {
                        Home_Activity.goTo(GetApplicationContext.context!!)
                        Toast.makeText(this@LoginActivity, "登录成功", Toast.LENGTH_SHORT).show()
                    }

                    override fun fail() {
                        Toast.makeText(this@LoginActivity, "登录失败", Toast.LENGTH_SHORT).show()
                    }

                }
            )
        }
    }
}