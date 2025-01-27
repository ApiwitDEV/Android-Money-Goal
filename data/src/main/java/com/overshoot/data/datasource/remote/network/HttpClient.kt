package com.overshoot.data.datasource.remote.network

import android.util.Log
import com.moczul.ok2curl.CurlInterceptor
import com.moczul.ok2curl.logger.Logger
import com.overshoot.data.datasource.local.user.UserInfoDao
import com.overshoot.data.datasource.local.user.UserInfoEntity
import com.overshoot.data.datasource.remote.model.authentication.AuthenticationService
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object HttpClient {

    private var client: OkHttpClient? = null

    fun getClient(userInfoDao: UserInfoDao, authenticationService: AuthenticationService): OkHttpClient {
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
                .callTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor { chain: Interceptor.Chain ->
                    requestHandler(chain, userInfoDao, authenticationService)
                }
                .addInterceptor(httpLogger)
                .addInterceptor(curlGenerator)
                .build()
        }
        return client!!
    }

    private fun requestHandler(
        chain: Interceptor.Chain,
        userInfoDao: UserInfoDao,
        authenticationService: AuthenticationService
    ): Response {
        return runBlocking {
            val userInfo = userInfoDao.getUserInfo()
            val response = if (userInfo.isNotEmpty()) {
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer "+userInfo.last().accessToken)
                    .build()
                chain.proceed(request)
            }
            else {
                chain.proceed(chain.request())
            }
            if (response.code == 401) {
                var isRefreshTokenSuccess = false
                try {
                    val accessToken = authenticationService.getAccessToken()
                    accessToken?.also {
                        userInfoDao.saveUserInfo(
                            UserInfoEntity(
                                accessToken = it,
                                refreshToken = "",
                                userName = ""
                            )
                        )
                        isRefreshTokenSuccess = true
                    }
                }
                catch (error: Exception) {
                    Log.e("okHttp", "refresh accessToken fail: $error")
                }
                if (isRefreshTokenSuccess) {
                    val newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer "+userInfoDao.getUserInfo().last().accessToken)
                        .build()
                    chain.proceed(newRequest)
                }
                else {
                    chain.proceed(chain.request()).newBuilder()
                        .code(401)
                        .message("refresh accessToken fail")
                        .build()
                }
            }
            else {
                response
            }
        }
    }

}