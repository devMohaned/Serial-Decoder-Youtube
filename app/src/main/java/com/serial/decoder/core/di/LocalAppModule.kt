package com.serial.decoder.core.di

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.serial.decoder.core.util.dataStore
import com.serial.decoder.feature_decoding.data.local.factory.DecoderFactory
import com.serial.decoder.feature_decoding.data.local.datastore.DataStoreManager
import com.serial.decoder.feature_onboarding.data.local.SharedPreferenceManagement
import com.serial.decoder.feature_onboarding.data.local.SharedPreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalAppModule {
    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            SharedPreferenceManager.SHARED_PREF_NAME,
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun provideSharedPreferenceManager(sharedPreferences: SharedPreferences): SharedPreferenceManagement {
        return SharedPreferenceManager(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideDataStorePreference(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideDataStoreManager(dataStore: DataStore<Preferences>): DataStoreManager {
        return DataStoreManager(dataStore)
    }

    @Provides
    @Singleton
    fun providesDecoderFactory(): DecoderFactory {
        return DecoderFactory()
    }
}