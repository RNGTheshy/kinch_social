package com.nino.blindbox.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaoshan.data_center.blindboxpersondata.BlindBoxPerson
import com.chaoshan.data_center.blindboxpersondata.BlindBoxPersonDatabase
import com.chaoshan.data_center.blindboxpersondata.BlindBoxPersonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivityViewModel(context: Context) : ViewModel() {

    lateinit var repository: BlindBoxPersonRepository
    var blindBoxPersonList: LiveData<List<BlindBoxPerson>>

    init {
        val repository =
            BlindBoxPersonRepository(BlindBoxPersonDatabase.getBlindBoxPersonDatabase(context).blindBoxPersonDao())
        blindBoxPersonList = repository.readAllData
    }

    fun addBlindBoxPerson(blindBoxPerson: BlindBoxPerson) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addBlindBoxPerson(blindBoxPerson)
        }
    }

}