package com.chaoshan.data_center.blindboxpersondata

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BlindBoxPerson::class], version = 1)
abstract class BlindBoxPersonDatabase : RoomDatabase() {


    abstract fun blindBoxPersonDao(): BlindBoxPersonDao

    companion object {
        @Volatile
        private var INSTANCE: BlindBoxPersonDatabase? = null

        fun getBlindBoxPersonDatabase(context: Context): BlindBoxPersonDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(

                    context.applicationContext,
                    BlindBoxPersonDatabase::class.java,
                    "blind_box_person_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}