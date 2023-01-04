package com.serial.decoder.feature_decoding.data.local.factory

import com.serial.decoder.feature_decoding.data.local.entity.Brands
import com.serial.decoder.feature_decoding.data.local.decoder.Decoder
import com.serial.decoder.feature_decoding.data.local.decoder.LGDecoder
import com.serial.decoder.feature_decoding.data.local.decoder.SamsungDecoder

class DecoderFactory : IDecoderFactory {

    override fun createDecoder(brandType: Brands): Decoder {
        return when (brandType) {
            Brands.SAMSUNG -> SamsungDecoder()
            Brands.LG -> LGDecoder()
        }
    }
}