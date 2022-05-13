package com.example.setting

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar

class AboutKinchActivity:AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_kinch)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.update ->{
                Snackbar.make(window.decorView.rootView,"已经是最新版本",Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}