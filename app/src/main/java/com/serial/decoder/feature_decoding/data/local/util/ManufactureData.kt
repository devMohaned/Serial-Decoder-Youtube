package com.serial.decoder.feature_decoding.data.local.util

interface ManufactureData<CountryKey,CountryValue,DateKey,DateValue> {
     val countryMap: HashMap<CountryKey, CountryValue>
     val monthMap:  HashMap<DateKey, DateValue>
     val yearMap: HashMap<DateKey, DateValue>
    fun fillYear()
    fun fillMonth()
    fun fillCountries()

}