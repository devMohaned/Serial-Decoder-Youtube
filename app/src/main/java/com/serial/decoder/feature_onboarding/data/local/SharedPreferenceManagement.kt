package com.serial.decoder.feature_onboarding.data.local

import android.content.SharedPreferences

interface SharedPreferenceManagement {
    val editor: SharedPreferences.Editor
    fun updateIsFirstTime(isFirstTime: Boolean)
    fun getIsFirstTime() : Boolean
}