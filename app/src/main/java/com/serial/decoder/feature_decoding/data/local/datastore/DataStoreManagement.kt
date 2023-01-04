package com.serial.decoder.feature_decoding.data.local.datastore

import kotlinx.coroutines.flow.Flow

interface DataStoreManagement {
    suspend fun updateSerial(serial: String)
    fun getSerial(): Flow<String>
    suspend fun updateBrand(brand: String)
    fun getBrand(): Flow<String>
}