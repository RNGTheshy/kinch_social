package com.chaoshan.login

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.InputStreamReader
import java.io.OutputStreamWriter

abstract class BaseLoginActivity :AppCompatActivity() {

    open val accountList = mutableMapOf<String, String>()
    private var mAccount: EditText? = null
    private var mRemember: EditText? = null
    private var mPass: CheckBox? = null
    private var mLogin: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    open fun initView(){

    }


}