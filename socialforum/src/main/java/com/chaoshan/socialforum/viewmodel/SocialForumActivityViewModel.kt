package com.chaoshan.socialforum.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chaoshan.data_center.dynamic.dynamic.Dynamic
import com.chaoshan.data_center.dynamic.dynamic.DynamicDao
import com.chaoshan.data_center.dynamic.GetDateCallBack

class SocialForumActivityViewModel : ViewModel() {
    val dynamicDaoList: MutableLiveData<List<Dynamic>> by lazy {
        MutableLiveData<List<Dynamic>>()
    }
    private val dynamicDao = DynamicDao(getDateCallBack = object : GetDateCallBack<Dynamic> {
        override fun updateDate(list: List<Dynamic>) {
            dynamicDaoList.value = list
        }
    })

    fun getDynamicData() {
        dynamicDao.updateObjectList()
    }

}