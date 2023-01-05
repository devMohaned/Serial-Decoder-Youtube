package com.serial.decoder.feature_decoding.data.local.entity

import androidx.annotation.Keep

/*
When we create an enum class, the compiler will create instances (objects) of each enum constants.
Also, all enum constant is always public static final by default. */

/* @Keep is used to keep this class from being renamed,thus preventing an error
 in production when accessing @Brands properties, this is made due to
 minifyEnabled true in the build.gradle app module */
@Keep
enum class Brands {
    SAMSUNG,
    LG,
}
