package com.yubinma.test

import org.junit.Before
import org.junit.Test

class WelcomeTest {

    private var count = 5
    private val sensorEvent = SensorEvent(10)
    private fun getCount(): Int {
        count--
        return count
    }

    @Before
    fun init() {
        for (i in 1..9) {
            sensorEvent.values!![i] = (i * 3.0).toFloat()
        }
    }

    //倒计时测试
    @Test
    fun countDownTest() {
        while (count != 0) {
            println(count)
            getCount()
        }
    }

    //传感器测试
    @Test
    fun onSensorTest() {
        for (i in 1..9) {
            if (i == 5) {
                println(sensorEvent.values?.get(i))
            }
        }
    }
}

class SensorEvent(valueSize: Int) {
    var values: FloatArray? = null

    init {
        values = FloatArray(valueSize)
    }
}