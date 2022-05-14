package com.example.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.AppUtils

class SettingMainActivity : AppCompatActivity() {
    private var backBtn : ImageView ?= null
    private var aboutKinchBtn: LinearLayout? = null
    private var evaluationBtn: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_main_menu)
        initView()
        initClickListener()
    }

    private fun initView() {
        backBtn = findViewById(R.id.back_toolBar)
        aboutKinchBtn = findViewById(R.id.about_kinch)
        evaluationBtn = findViewById(R.id.evaluation)
    }

    private fun initClickListener() {
        backBtn?.setOnClickListener {
            finish()
        }
        aboutKinchBtn?.setOnClickListener {
            //关于Kinch
            val intent = Intent(this, AboutKinchActivity::class.java)
            startActivity(intent)
        }
        evaluationBtn?.setOnClickListener {
            //评价
            val i = Intent(Intent.ACTION_VIEW)
            i.data =
                Uri.parse("market://details?id=" + AppUtils.getAppPackageName()) // 打开市场
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
        }
    }


}