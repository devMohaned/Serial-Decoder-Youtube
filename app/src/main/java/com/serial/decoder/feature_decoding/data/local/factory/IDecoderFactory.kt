package com.serial.decoder.feature_decoding.data.local.factory

import com.serial.decoder.feature_decoding.data.local.entity.Brands
import com.serial.decoder.feature_decoding.data.local.decoder.Decoder

interface IDecoderFactory {
    fun createDecoder(brandType: Brands): Decoder
}