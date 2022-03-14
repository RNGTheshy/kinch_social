package com.chaoshan.login

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.chaoshan.login.databinding.LoginMainActivityBinding


class LoginActivity : AppCompatActivity() {
    lateinit var binding: LoginMainActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginMainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}