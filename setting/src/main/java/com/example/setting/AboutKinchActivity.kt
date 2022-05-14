package com.example.setting

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar

class AboutKinchActivity : AppCompatActivity() {
    private var backBtn : ImageView ?= null
    private var updateBtn : LinearLayout?=null
    private var introduceBtn : LinearLayout?=null
    private var helpBtn : LinearLayout?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_kinch)
        initView()
        initClickListener()
    }

    private fun initView() {
        backBtn = findViewById(R.id.back_toolBar)
        updateBtn = findViewById(R.id.update)
        introduceBtn = findViewById(R.id.introduce)
        helpBtn = findViewById(R.id.help)
    }

    private fun initClickListener() {
        backBtn?.setOnClickListener {
            finish()
        }
        updateBtn?.setOnClickListener{
            Snackbar.make(window.decorView.rootView, "已经是最新版本", Snackbar.LENGTH_SHORT).show()
        }
    }

}