package com.overshoot.data.datasource.remote

import com.overshoot.data.datasource.remote.model.employees.Employees
import com.overshoot.data.datasource.remote.model.goal.PostGoalRequestBody
import com.overshoot.data.datasource.remote.model.goal.PostGoalResponse
import com.overshoot.data.datasource.remote.model.something.RequestBody
import com.overshoot.data.datasource.remote.model.something.Something
import com.overshoot.data.datasource.remote.model.transaction.PostTransactionRequestBody
import com.overshoot.data.datasource.remote.model.transaction.PostTransactionResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MoneyGoalApiService {

    @POST("/")
    suspend fun callSomething(
        @Query("data1") data1: String,
        @Query("data2") data2: String,
        @Body body: RequestBody
    ): Something

    @POST("/widgets")
    suspend fun callWidget(): String

    @GET("/info")
    suspend fun getInfo(): Response<Employees>

    @GET("/unauthorizedTest")
    fun getUnauthorizedTest(): Call<PostGoalResponse>

    @POST("/goals")
    suspend fun postGoal(@Body body: PostGoalRequestBody): Response<PostGoalResponse>

    @POST("/transaction")
    suspend fun postTransaction(@Body body: PostTransactionRequestBody): Response<PostTransactionResponse>

}