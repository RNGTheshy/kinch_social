package com.chaoshan.data_center.blindboxpersondata

import androidx.lifecycle.LiveData

class BlindBoxPersonRepository(private val blindBoxPersonDao: BlindBoxPersonDao) {
    val readAllData :LiveData<List<BlindBoxPerson>> = blindBoxPersonDao.findAll()

    suspend fun addBlindBoxPerson(blindBoxPerson: BlindBoxPerson){
        blindBoxPersonDao.insertBlindBoxPerson(blindBoxPerson)
    }

}