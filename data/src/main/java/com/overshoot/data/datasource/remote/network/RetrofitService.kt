package com.overshoot.data.datasource.remote.network

import com.overshoot.data.datasource.remote.MoneyGoalApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

fun getMoneyGoalApiService(httpClient: OkHttpClient): MoneyGoalApiService {
    return Retrofit.Builder()
        .client(httpClient)
        .baseUrl("https://money-596910917282.us-central1.run.app")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(MoneyGoalApiService::class.java)
}