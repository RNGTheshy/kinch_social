package com.yubinma.person_center

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yubinma.person_center.databinding.PersonCenterActivittyBinding

class PersonCenterActivity : AppCompatActivity() {
    lateinit var binding: PersonCenterActivittyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PersonCenterActivittyBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}