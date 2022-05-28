package com.example.friend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chaoshan.data_center.SettingsPreferencesDataStore
import com.chaoshan.data_center.friend.GetAllUer
import com.example.friend.databinding.SentInformationBinding

class SentActivity : AppCompatActivity() {
    lateinit var binding: SentInformationBinding
    private var fId: String? = null
    private var message: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SentInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLister()
    }

    private fun initLister() {
        message = binding.message.text.toString()
        fId = intent.getStringExtra("fId")

        binding.sent.setOnClickListener {
            message = binding.message.text.toString()
            fId?.let { fId ->
                message?.let { message ->
                    GetAllUer.addFriendWithMessage(
                        SettingsPreferencesDataStore.getCurrentUserObjetID(),
                        fId,
                        message
                    )
                }
            }

            finish()
        }

    }
}