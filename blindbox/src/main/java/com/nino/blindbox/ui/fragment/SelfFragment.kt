package com.nino.blindbox.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nino.blindbox.R
import com.nino.blindbox.base.BaseFragment

class SelfFragment : BaseFragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_self, null)
        return view
    }
}