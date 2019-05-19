package com.penguodev.smartmd.repository

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object MDRepository {

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
}