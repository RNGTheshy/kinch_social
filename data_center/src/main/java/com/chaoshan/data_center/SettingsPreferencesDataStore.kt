package com.chaoshan.data_center

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore by preferencesDataStore(name = "settings")

object SettingsPreferencesDataStore {

    const val USER_NAME = "user_name"

    @Volatile
    private var currentUserObjetID = "NULL"
    fun getCurrentUserObjetID(): String {
        return currentUserObjetID
    }

    @DelicateCoroutinesApi
    fun setCurrentUserObjectID() {
        GetApplicationContext.context?.let {
            GlobalScope.launch {
                currentUserObjetID = getName(it, USER_NAME)

                Log.e(
                    "nameTest",
                    getName(it, USER_NAME)
                )
            }
        }
    }


    var EXAMPLE_COUNTER = stringPreferencesKey("example_counter")

    /**
     * 保存数据
     * */
    suspend fun saveData(context: Context, string: String, key: String) {
        currentUserObjetID = string
        EXAMPLE_COUNTER = stringPreferencesKey(key)
        context.dataStore.edit { settings ->
            settings[EXAMPLE_COUNTER] = string
        }
    }

    /**
     * 获取数据
     * */
    suspend fun getName(context: Context, key: String): String {
        EXAMPLE_COUNTER = stringPreferencesKey(key)
        val exampleCounterFlow: Flow<String> = context.dataStore.data
            .map { preferences ->
                // No type safety.
                preferences[EXAMPLE_COUNTER] ?: "NULL"
            }
        return exampleCounterFlow.first()
    }

    suspend fun getUserObjectId(): String {
        // 获取数据
        GetApplicationContext.context?.let {
            return getName(it, USER_NAME)
        }
        return "null"
    }


    fun setUserObjectId(id: String) {
        GlobalScope.launch {
            currentUserObjetID = id
            saveData(
                GetApplicationContext.context!!,
                id,
                USER_NAME
            )
        }
    }
}

object GetApplicationContext {
    var context: Context? = null

}