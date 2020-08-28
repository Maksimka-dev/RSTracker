package com.rshack.rstracker.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.unsplash.com/search/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface PhotoApiService {
    @GET("photos?client_id=WHKarwg_kx4SVgXyp9FTV5nIAMT1Bs2V664nVwK-5XE&query=running")
    suspend fun getPhotos(@Query("page") page: Int): Results
}

object PhotoApi {
    val retrofitService: PhotoApiService by lazy {
        retrofit.create(PhotoApiService::class.java)
    }
}