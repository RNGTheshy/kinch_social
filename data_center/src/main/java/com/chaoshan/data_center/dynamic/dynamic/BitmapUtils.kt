package com.chaoshan.data_center.dynamic.dynamic

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

object BitmapUtils {
    private const val TAG = "BitmapUtils"

    @JvmStatic
    fun bmpToByteArray(bmp: Bitmap, needRecycle: Boolean): ByteArray {
        val output = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output)//设置图片格式压缩相关
        if (needRecycle) {
            bmp.recycle()
        }

        val result = output.toByteArray()
        try {
            output.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result//输出
    }
}

