package com.yubinma.fishprawncrab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yubinma.fishprawncrab.databinding.FishPrawnCrabMainActivityBinding

class FishPrawnCrabMainActivity : AppCompatActivity() {
    private lateinit var binding: FishPrawnCrabMainActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FishPrawnCrabMainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}