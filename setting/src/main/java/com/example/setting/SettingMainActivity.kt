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

class SettingMainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_main_menu)
    }


    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                R.id.permission -> {
                    //TODO : yubinma
                }
                R.id.back_toolBar -> {
                    //TODO
                }
                R.id.question -> {
                    //TODO
                }
                R.id.clause -> {
                    //TODO
                }
                R.id.policy -> {
                    //TODO
                }
                R.id.about_kinch -> {
                    //关于Kinch
                    val intent = Intent(this, AboutKinchActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.connect -> {

                }
                R.id.evaluation -> {
                    //评价
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse("market://details?id=" + AppUtils.getAppPackageName()) // 打开市场
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(i)
                }
                R.id.exit ->{
                    //TODO : C1033荣耀王者神威马超
                }
            }
        }

    }

}