package com.example.kinch_home

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED; //设置为竖屏
        window.navigationBarColor = resources.getColor(R.color.gray)
        setContentView(R.layout.activity_home)
        initViews();
    }
}
fun initViews(){

}