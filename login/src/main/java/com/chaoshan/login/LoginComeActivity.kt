package com.chaoshan.login

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.chaoshan.login.databinding.LoginComeActivityBinding

class LoginComeActivity : AppCompatActivity() {
    private lateinit var binding: LoginComeActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginComeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}