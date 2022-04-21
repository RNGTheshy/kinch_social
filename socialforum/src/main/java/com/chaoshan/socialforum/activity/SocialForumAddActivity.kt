package com.chaoshan.socialforum.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.chaoshan.data_center.dynamic.Dynamic
import com.chaoshan.data_center.dynamic.DynamicClient
import com.chaoshan.socialforum.databinding.SocialAddFragmentBinding
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
        }
        binding.imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 42)
        }
    }

    private fun sent() {

        val newDynamic = Dynamic(
            0,
            111,
            "111",
            binding.mainText.text.toString()
        )
        DynamicClient.saveDate(newDynamic, mImageBytes)

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