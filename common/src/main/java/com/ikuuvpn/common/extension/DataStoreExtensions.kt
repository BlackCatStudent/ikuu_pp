package com.ikuuvpn.common.extension

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

suspend fun <T> DataStore<Preferences>.setValue(key: String, value: T) {
    edit { preferences ->
        when (value) {
            is String -> preferences[stringPreferencesKey(key)] = value
            is Int -> preferences[stringPreferencesKey(key)] = value.toString()
            is Long -> preferences[stringPreferencesKey(key)] = value.toString()
            is Float -> preferences[stringPreferencesKey(key)] = value.toString()
            is Boolean -> preferences[stringPreferencesKey(key)] = value.toString()
        }
    }
}

suspend fun DataStore<Preferences>.removeValue(key: String) {
    edit { preferences ->
        preferences.remove(stringPreferencesKey(key))
    }
}

fun DataStore<Preferences>.getString(key: String, default: String = ""): Flow<String> {
    return this.data.map { preferences ->
        preferences[stringPreferencesKey(key)] ?: default
    }
}

fun DataStore<Preferences>.getInt(key: String, default: Int = 0): Flow<Int> {
    return this.data.map { preferences ->
        preferences[stringPreferencesKey(key)]?.toIntOrNull() ?: default
    }
}

fun DataStore<Preferences>.getLong(key: String, default: Long = 0L): Flow<Long> {
    return this.data.map { preferences ->
        preferences[stringPreferencesKey(key)]?.toLongOrNull() ?: default
    }
}

fun DataStore<Preferences>.getFloat(key: String, default: Float = 0f): Flow<Float> {
    return this.data.map { preferences ->
        preferences[stringPreferencesKey(key)]?.toFloatOrNull() ?: default
    }
}

fun DataStore<Preferences>.getBoolean(key: String, default: Boolean = false): Flow<Boolean> {
    return this.data.map { preferences ->
        preferences[stringPreferencesKey(key)]?.toBoolean() ?: default
    }
}

fun SharedPreferences.getString(key: String, default: String = ""): String {
    return getString(key, default) ?: default
}

fun SharedPreferences.getInt(key: String, default: Int = 0): Int {
    return getInt(key, default)
}

fun SharedPreferences.getLong(key: String, default: Long = 0L): Long {
    return getLong(key, default)
}

fun SharedPreferences.getFloat(key: String, default: Float = 0f): Float {
    return getFloat(key, default)
}

fun SharedPreferences.getBoolean(key: String, default: Boolean = false): Boolean {
    return getBoolean(key, default)
}