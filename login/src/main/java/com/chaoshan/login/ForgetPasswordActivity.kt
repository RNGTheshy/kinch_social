package com.chaoshan.login

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.chaoshan.login.databinding.LoginFindPasswordActivityBinding
import com.chaoshan.login.databinding.LoginMainActivityBinding


class ForgetPasswordActivity : AppCompatActivity() {
    lateinit var binding: LoginFindPasswordActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginFindPasswordActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}