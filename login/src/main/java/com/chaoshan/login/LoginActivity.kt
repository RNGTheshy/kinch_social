package com.chaoshan.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chaoshan.data_center.GetApplicationContext
import com.chaoshan.data_center.userManager.LoginCallBack
import com.chaoshan.data_center.userManager.UserManger
import com.chaoshan.login.databinding.LoginMainActivityBinding
import com.example.kinch_home.Home_Activity
import kotlinx.coroutines.DelicateCoroutinesApi


class LoginActivity : AppCompatActivity() {
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
                    }

                    override fun fail() {

                    }

                }
            )
        }
    }
}