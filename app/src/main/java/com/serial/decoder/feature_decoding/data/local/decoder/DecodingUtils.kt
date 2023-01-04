package com.serial.decoder.feature_decoding.data.local.decoder

import com.serial.decoder.feature_decoding.data.local.entity.Brands
import com.serial.decoder.feature_decoding.data.local.entity.ManufactureEntity
import com.serial.decoder.feature_decoding.data.local.factory.DecoderFactory

class DecodingUtils(val decoderFactory: DecoderFactory) {

    fun isCorrectSerial(serial: String, brands: Brands): Boolean {
        return decoderFactory.createDecoder(brands).isCorrectSerial(serial)
    }

    fun decodeSerial(serial: String, brands: Brands): ManufactureEntity {
        return decoderFactory.createDecoder(brands).decodeSerial(serial)
    }


}