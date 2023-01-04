package com.serial.decoder.feature_onboarding.data.local

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPreferenceManager @Inject constructor(val sharedPreferences: SharedPreferences) :
    SharedPreferenceManagement {
    companion object {
        const val KEY_IS_FIRST_TIME = "is_first_time"
        const val SHARED_PREF_NAME = "shared_pref"
    }


    override val editor: SharedPreferences.Editor
        get() = sharedPreferences.edit()

    override fun updateIsFirstTime(isFirstTime: Boolean) {
        with(editor) {
            putBoolean(KEY_IS_FIRST_TIME, isFirstTime)
            apply()
        }
    }

    override fun getIsFirstTime(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_FIRST_TIME, true)
    }
}