package com.serial.decoder.feature_decoding.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serial.decoder.R
import com.serial.decoder.core.util.UIText
import com.serial.decoder.feature_decoding.data.local.entity.Brands
import com.serial.decoder.feature_decoding.data.local.entity.ManufactureEntity
import com.serial.decoder.feature_decoding.data.local.factory.DecoderFactory
import com.serial.decoder.feature_decoding.data.local.datastore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

sealed class HomeUIEvent {
    object ShowDialog : HomeUIEvent()
    class ShowSnackBar(val message: String) : HomeUIEvent()
}

data class HomeUIState(
    val isLoading: Boolean,
    val serial: String,
    val brand: String,
    val manufactureEntity: ManufactureEntity
)

const val SERIAL_INDEX = 0
const val BRAND_INDEX = 1

@HiltViewModel
class HomeViewModel @Inject constructor(
    val dataStoreManager: DataStoreManager,
    val decoderFactory: DecoderFactory
) : ViewModel() {

    val brands: Array<Brands> = Brands.values()

    private val _uiEvents: MutableSharedFlow<HomeUIEvent> = MutableSharedFlow<HomeUIEvent>()
    val uiEvent: SharedFlow<HomeUIEvent> = _uiEvents.asSharedFlow()


    private val _uiState: MutableStateFlow<HomeUIState> = MutableStateFlow(
        HomeUIState(
            isLoading = true,
            "",
            brands[0].name,
            getManufactureEntityBySerialOrBrand("", brands[0].name)
        )
    )
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    val serialFlow: Flow<String> = dataStoreManager.getSerial()
    val brandFlow: Flow<String> = dataStoreManager.getBrand()


    val serialWithBrandFlow = serialFlow.combine(brandFlow) { serial, brand ->
        listOf(serial, brand)
    }.onEach { serialWithBrandList ->
        val serial = serialWithBrandList[SERIAL_INDEX]
        val brand = serialWithBrandList[BRAND_INDEX]

        _uiState.value = uiState.value.copy(
            isLoading = false,
            serial = serial,
            brand = brand,
            manufactureEntity = getManufactureEntityBySerialOrBrand(serial, brand)
        )
    }

    private fun getManufactureEntityBySerialOrBrand(
        serial: String,
        brand: String
    ): ManufactureEntity {
        val decoder = decoderFactory.createDecoder(Brands.valueOf(brand))

        if (decoder.isCorrectSerial(serial)) return decoder.decodeSerial(serial)

        return ManufactureEntity(
            UIText.StringResource(
                R.string.couldnot_identify_type
            ),
            UIText.StringResource(R.string.couldnot_identify_country),
            UIText.StringResource(R.string.couldnot_identify_date),
        )
    }

    fun updateSerial(serial: String) = runBlocking {
        dataStoreManager.updateSerial(serial)
    }

    fun updateBrand(brand: String) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreManager.updateBrand(brand)
    }

    fun showDialog() = viewModelScope.launch {
        _uiEvents.emit(HomeUIEvent.ShowDialog)
    }

    fun showSnackBar(localizedMessage: String) = viewModelScope.launch {
        _uiEvents.emit(HomeUIEvent.ShowSnackBar(localizedMessage))
    }

}