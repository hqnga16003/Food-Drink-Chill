package com.example.fooddrinkchill.data.repository

import android.content.Context
import androidx.core.content.edit

class PreferenceRepository(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun isFirstTimeLaunch(): Boolean {
        return sharedPreferences.getBoolean("is_first_time", true)
    }

    fun setFirstTimeLaunch(isFirstTime: Boolean) {
        sharedPreferences.edit { putBoolean("is_first_time", isFirstTime) }
    }
}