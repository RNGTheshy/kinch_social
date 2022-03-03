package com.chaoshan.data_center.blindboxpersondata

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BlindBoxPersonDao {
    @Insert
    suspend fun insertBlindBoxPerson(blindBoxPerson: BlindBoxPerson)

    @Update
    suspend fun updateBlindBoxPerson(blindBoxPerson: BlindBoxPerson)

    @Delete
    suspend fun deleteBlindBoxPerson(blindBoxPerson: BlindBoxPerson)

    @Query("select * from blind_box_person")
    fun findAll(): LiveData<List<BlindBoxPerson>>
}