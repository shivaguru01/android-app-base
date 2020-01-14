package com.app.base.repo.local

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class PreferenceDelegate<T> : ReadWriteProperty<Any, T> {
    abstract val sharedPreferences: SharedPreferences
}

class IntPreference(
    override var sharedPreferences: SharedPreferences,
    private val key: String,
    private val default: Int = -1
) : PreferenceDelegate<Int>() {
    override fun getValue(thisRef: Any, property: KProperty<*>): Int =
        sharedPreferences.getInt(key, default);

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) =
        sharedPreferences.edit { putInt(key, value) }
}

class StringPreference(
    override var sharedPreferences: SharedPreferences,
    private val key: String,
    private val default: String? = null
) : PreferenceDelegate<String?>() {
    override fun getValue(thisRef: Any, property: KProperty<*>): String? = sharedPreferences.getString(key, default);

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) =
        sharedPreferences.edit { putString(key, value) }
}

class BooleanPreference(
    override var sharedPreferences: SharedPreferences,
    private val key: String,
    private val default: Boolean = false
) : PreferenceDelegate<Boolean>() {
    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean =
        sharedPreferences.getBoolean(key, default);

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) =
        sharedPreferences.edit { putBoolean(key, value) }

}

class FloatPreference(
    override val sharedPreferences: SharedPreferences,
    private val key: String,
    private val default: Float = 0.toFloat()
) : PreferenceDelegate<Float>() {
    override fun getValue(thisRef: Any, property: KProperty<*>): Float =
        sharedPreferences.getFloat(key, default);

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Float) =
        sharedPreferences.edit { putFloat(key, value) }
}

class LongPreference(
    override var sharedPreferences: SharedPreferences,
    private val key: String,
    private val default: Long = -1
) : PreferenceDelegate<Long>() {
    override fun getValue(thisRef: Any, property: KProperty<*>): Long =
        sharedPreferences.getLong(key, default);

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) =
        sharedPreferences.edit { putLong(key, value) }
}

fun SharedPreferences.int(key: String): ReadWriteProperty<Any, Int> =
    IntPreference(sharedPreferences = this, key = key)

fun SharedPreferences.string(key: String): ReadWriteProperty<Any, String?> =
    StringPreference(sharedPreferences = this, key = key)

fun SharedPreferences.float(key: String): ReadWriteProperty<Any, Float> =
    FloatPreference(sharedPreferences = this, key = key)

fun SharedPreferences.long(key: String): ReadWriteProperty<Any, Long> =
    LongPreference(sharedPreferences = this, key = key)

fun SharedPreferences.boolean(key: String): ReadWriteProperty<Any, Boolean> =
    BooleanPreference(sharedPreferences = this, key = key)