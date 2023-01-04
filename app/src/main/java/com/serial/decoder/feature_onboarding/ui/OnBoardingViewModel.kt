package com.serial.decoder.feature_onboarding.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serial.decoder.feature_onboarding.data.local.SharedPreferenceManagement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OnBoardingUIState(val isLoading: Boolean)

sealed class OnBoardingUIEvents {
    object UpdateFirstTime : OnBoardingUIEvents()
}

@HiltViewModel
class OnBoardingViewModel @Inject constructor(val sharedPreferenceManager: SharedPreferenceManagement) :
    ViewModel() {

    private val _uiState: MutableStateFlow<OnBoardingUIState> =
        MutableStateFlow(OnBoardingUIState(isLoading = false))
    val uiState: StateFlow<OnBoardingUIState> = _uiState.asStateFlow()

    private val _uiEvents: MutableSharedFlow<OnBoardingUIEvents> = MutableSharedFlow()
    val uiEvent: SharedFlow<OnBoardingUIEvents> = _uiEvents.asSharedFlow()

    fun setLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }

    fun updateFirstTime() {
        setLoading(true)
        sharedPreferenceManager.updateIsFirstTime(false)
        viewModelScope.launch {
            _uiEvents.emit(OnBoardingUIEvents.UpdateFirstTime)
            setLoading(false)
        }
    }

}