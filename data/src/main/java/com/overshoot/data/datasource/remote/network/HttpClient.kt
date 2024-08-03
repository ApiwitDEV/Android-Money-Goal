package com.overshoot.data.datasource.remote.network

import android.util.Log
import com.moczul.ok2curl.CurlInterceptor
import com.moczul.ok2curl.logger.Logger
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object HttpClient {

    private var client: OkHttpClient? = null

    fun getClient(): OkHttpClient {
        if (client == null) {
            val curlGenerator = CurlInterceptor(object : Logger {
                override fun log(message: String) {
                    Log.v("Ok2Curl", message)
                }
            })
            val httpLogger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            client = OkHttpClient.Builder()
                .callTimeout(25, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(curlGenerator)
                .addInterceptor(httpLogger)
                .addInterceptor { chain: Interceptor.Chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "bearer")
                        .build()
                    chain.proceed(request)
                }
                .build()
        }
        return client!!
    }

}