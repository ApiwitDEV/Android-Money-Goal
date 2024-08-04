package com.overshoot.data.datasource.remote.network

import android.util.Log
import com.moczul.ok2curl.CurlInterceptor
import com.moczul.ok2curl.logger.Logger
import com.overshoot.data.datasource.local.user.UserInfoDao
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object HttpClient {

    private var client: OkHttpClient? = null

    fun getClient(userInfoDao: UserInfoDao): OkHttpClient {
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
                .addInterceptor { chain: Interceptor.Chain ->
                    val userInfo = userInfoDao.getUserInfo()
                    if (userInfo.isNotEmpty()) {
                        val request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer "+userInfo[0].accessToken)
                            .build()
                        chain.proceed(request)
                    }
                    else {
                        chain.proceed(chain.request())
                    }
                }
                .addInterceptor(httpLogger)
                .addInterceptor(curlGenerator)
                .build()
        }
        return client!!
    }

}