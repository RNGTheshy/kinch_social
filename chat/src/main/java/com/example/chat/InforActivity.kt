package com.example.chat

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cn.leancloud.chatkit.LCChatKit
import kotlinx.android.synthetic.main.templogin.*


class InforActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.templogin)
        btn.setOnClickListener{
            val use = user.text.toString()
            val pass = pass.text.toString()
            ChatActivity.goToChat(this,use,pass)
        }
    }
}