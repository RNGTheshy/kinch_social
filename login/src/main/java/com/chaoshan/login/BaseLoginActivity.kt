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

    inner class ForceOfflineReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            android.app.AlertDialog.Builder(context).apply {
                setTitle("警告")
                setMessage("请重新登录")
                setCancelable(false)
                setPositiveButton("OK") { _, _ ->
                    finish()
                    val i = Intent(context, BaseLoginActivity::class.java)
                    context.startActivity(i)
                }
                show()
            }
        }

    }

    //设置正确账号map表
    private fun setAccountList() {
        val input = openFileInput("account_password.txt")
        val reader = BufferedReader(InputStreamReader(input))
        var line = 0
        val account = ArrayList<String>()
        val password = ArrayList<String>()
        reader.use {
            reader.forEachLine {
                line += 1
                if (line % 2 == 1)
                    account.add(it)
                else if (line % 2 == 0)
                    password.add(it)
            }
        }
        for (i in account.indices) {
            accountList[account[i]] = password[i]
        }
    }

    //初始化默认账号
    @SuppressLint("SdCardPath")
    private fun addDefaultAccount() {
        //判断文件是否存在
        val file = File("/data/data/com.tool.rss/files/account_password.txt")
        if (!file.exists()) {
            val output = openFileOutput("account_password.txt", AppCompatActivity.MODE_APPEND)
            val writer = BufferedWriter(OutputStreamWriter(output))
            writer.use {
                it.write("2019151032")
                it.newLine()
                it.write("123456")
                it.newLine()
            }
        }
    }
}