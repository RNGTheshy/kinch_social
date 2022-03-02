package com.nino.blindbox.ui.activity

import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.nino.blindbox.R
import com.nino.blindbox.ui.utils.RxCountDown
import io.reactivex.*
import io.reactivex.disposables.Disposable

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = resources.getColor(R.color.gray)
        RxCountDown.countdown(2)?.doOnSubscribe { disposable: Disposable? -> }
            ?.subscribe(object : Observer<Int?> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: Int) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
                    finish()
                }
            })
    }
}