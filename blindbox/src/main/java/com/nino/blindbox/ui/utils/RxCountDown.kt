package com.nino.blindbox.ui.utils

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

object RxCountDown {
    fun countdown(time: Int): Observable<Int?>? {
        var time = time
        if (time < 0) time = 0
        val countTime = time
        return Observable.interval(0, 1, TimeUnit.SECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .map { increaseTime: Long? -> countTime - increaseTime!!.toInt() }
            .take((countTime + 1).toLong())
    }
}