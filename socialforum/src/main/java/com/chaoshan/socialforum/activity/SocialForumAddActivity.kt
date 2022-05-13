package com.chaoshan.socialforum.activity

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.chaoshan.data_center.GetApplicationContext
import com.chaoshan.data_center.SettingsPreferencesDataStore
import com.chaoshan.data_center.dynamic.Dynamic
import com.chaoshan.data_center.dynamic.DynamicClient
import com.chaoshan.socialforum.databinding.SocialAddFragmentBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream


class SocialForumAddActivity : AppCompatActivity() {
    private var mImageViewSelect: ImageView? = null
    private var mImageBytes: ByteArray? = null

    private lateinit var binding: SocialAddFragmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SocialAddFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initAction()
    }

    private fun initView() {
        mImageViewSelect = binding.imageView

    }

    private fun initAction() {
        binding.backSocial.setOnClickListener {
            finish()
        }
        binding.sent.setOnClickListener {
            sent()
            finish()
        }
        binding.imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 42)
        }
    }

    @DelicateCoroutinesApi
    private fun sent() {
        // 获取数据
        GetApplicationContext.context!!.let {
            GlobalScope.launch {
                val userId = SettingsPreferencesDataStore.USER_NAME
                val newDynamic = Dynamic(
                    "0",
                    userId,
                    null,
                    binding.mainText.text.toString()
                )
                DynamicClient.saveDate(newDynamic, mImageBytes)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 42 && resultCode == RESULT_OK) {
            try {
                mImageViewSelect?.setImageBitmap(MediaStore.Images.Media.getBitmap(this.contentResolver, data!!.data))
                mImageBytes = contentResolver.openInputStream(data?.data!!)?.let { getBytes(it) }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len: Int
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteArrayOutputStream.write(buffer, 0, len)
        }
        return byteArrayOutputStream.toByteArray()
    }


}