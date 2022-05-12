package com.chaoshan.login

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

object SettingsPreferencesDataStore {
    var EXAMPLE_COUNTER = stringPreferencesKey("example_counter")

    /**
     * 保存数据
     * */
    suspend fun saveData(context: Context, string: String, key: String) {
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
}

object GetApplicationContext {
    var context: Context? = null

}