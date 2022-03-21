package com.chaoshan.login

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.chaoshan.login.databinding.LoginFindPasswordActivityBinding
import com.chaoshan.login.databinding.LoginMainActivityBinding
import com.chaoshan.login.databinding.LoginRegisterActivityBinding


class RegisterActivity : AppCompatActivity() {
    lateinit var binding: LoginRegisterActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginRegisterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}