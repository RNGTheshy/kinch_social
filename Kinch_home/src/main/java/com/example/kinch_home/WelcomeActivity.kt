package com.example.kinch_home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.chaoshan.data_center.SettingsPreferencesDataStore
import com.example.setting.LoginActivity

class WelcomeActivity : AppCompatActivity() {
    // 声明控件对象
    private var textView: TextView? = null
    private var button : Button ?=null
    //声明时间有多少;
    private var count = 5
    private var animation: Animation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 下面的话就是去除标题的方法
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_welcome)
        // 初始化控件对象textView
        textView = findViewById<View>(R.id.textView) as TextView

        button = findViewById(R.id.jumpButton)
        button?.setOnClickListener{
            val intent: Intent = if (SettingsPreferencesDataStore.getCurrentUserObjetID() == "NULL") {
                Intent(this, LoginActivity::class.java)
            } else {
                Intent(this, Home_Activity::class.java)
            }
            startActivity(intent)
            finish()
        }

        animation = AnimationUtils.loadAnimation(this, R.anim.animation_text)
        handler.sendEmptyMessageDelayed(0, 1000)
    }

    //咱在写一个计算Welcome界面的广告时间结束后进入主界面的方法
    private fun getCount(): Int {
        count--
        if (count == 0) {
            val intent: Intent = if (SettingsPreferencesDataStore.getCurrentUserObjetID() == "NULL") {
                Intent(this, LoginActivity::class.java)
            } else {
                Intent(this, Home_Activity::class.java)
            }
            startActivity(intent)
            finish()
        }
        return count
    }

    //进行一个消息的处理
    @SuppressLint("HandlerLeak")
    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == 0) {
                textView?.setText(getCount().toString() + "")
                this.sendEmptyMessageDelayed(0, 1000)
                animation?.reset()
                textView?.startAnimation(animation)
            }
        }
    }
}