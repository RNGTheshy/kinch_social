package com.nino.blindbox.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.nino.blindbox.R
import com.nino.blindbox.base.BaseActivity
import java.io.*


class LoginActivity : BaseActivity() {

    val accountList = mutableMapOf<String, String>()
    private var mAccount: EditText? = null
    private var mRemember: EditText? = null
    private var mPass: CheckBox? = null
    private var mLogin: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.statusBarColor = resources.getColor(R.color.pink)
        supportActionBar?.hide()
        //一周内免登录功能(记住密码）
        val prefs=getPreferences(Context.MODE_PRIVATE)
        val isRemember=prefs.getBoolean("remember_password",false)
        mAccount = findViewById<EditText>(R.id.accountEdit)
        mRemember = findViewById<EditText>(R.id.passwordEdit)
        mPass=findViewById<CheckBox>(R.id.rememberPass)
        mLogin=findViewById<Button>(R.id.login)
        if(isRemember){
            val account=prefs.getString("account","")
            val password=prefs.getString("password","")
            mAccount?.setText(account)
            mRemember?.setText(password)
            mPass?.isChecked=true
        }

        //加入默认账号密码
        //学生账号：2019151032 密码：123456
        addDefaultAccount()

        mLogin?.setOnClickListener {
            //设置正确账号密码Map
            setAccountList()
            val account = mAccount?.text.toString()
            val password = mRemember?.text.toString()
            //账号密码匹配成功
            if (accountList[account] == password) {
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
                val editor=prefs.edit()
                if(mPass?.isChecked == true){
                    editor.putBoolean("remember_password",true)
                    editor.putString("account",account)
                    editor.putString("password",password)
                }else{
                    editor.clear()
                }
                //进入界面
                editor.apply()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                AlertDialog.Builder(this).apply {
                    setTitle("登录失败")
                    setMessage("请重新输入用户名和密码")
                    setCancelable(false)
                    setPositiveButton("OK") { _, _ -> }
                    show()
                }
                mRemember?.text = null
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
            val output = openFileOutput("account_password.txt", MODE_APPEND)
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