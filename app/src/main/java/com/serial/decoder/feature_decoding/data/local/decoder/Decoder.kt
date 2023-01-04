package com.serial.decoder.feature_decoding.data.local.decoder

import com.serial.decoder.core.util.UIText
import com.serial.decoder.feature_decoding.data.local.entity.ManufactureEntity

const val UNSPECIFIED_TYPE = "UNSPECIFIED TYPE"
const val UNDEFINED_COUNTRY = "UNDEFINED COUNTRY"
const val UNDEFINED_MONTH = "UNDEFINED MONTH"
const val UNDEFINED_YEAR = "UNDEFINED YEAR"

interface Decoder {
    fun isCorrectSerial(serial: String) : Boolean
    fun getTypeFromSerial(serial: String): UIText
    fun getCountryFromSerial(serial: String): UIText
    fun getDateFromSerial(serial: String): UIText
    fun decodeSerial(serial: String): ManufactureEntity
}