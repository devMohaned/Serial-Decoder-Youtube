package com.serial.decoder.core.ui

import androidx.lifecycle.ViewModel
import com.serial.decoder.feature_onboarding.data.local.SharedPreferenceManagement
import com.serial.decoder.feature_onboarding.data.local.SharedPreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(sharedPreferenceManager: SharedPreferenceManagement) :
    ViewModel() {

        val isFirstTime = sharedPreferenceManager.getIsFirstTime() == true
}