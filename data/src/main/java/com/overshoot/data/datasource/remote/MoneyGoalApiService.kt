package com.overshoot.data.datasource.remote

import com.overshoot.data.datasource.remote.model.categories.GetCategoriesResponse
import com.overshoot.data.datasource.remote.model.employees.Employees
import com.overshoot.data.datasource.remote.model.goal.PostGoalRequestBody
import com.overshoot.data.datasource.remote.model.goal.PostGoalResponse
import com.overshoot.data.datasource.remote.model.receipt.ReceiptInfoResponse
import com.overshoot.data.datasource.remote.model.something.RequestBody
import com.overshoot.data.datasource.remote.model.something.Something
import com.overshoot.data.datasource.remote.model.transaction.GetTransactionsResponse
import com.overshoot.data.datasource.remote.model.transaction.PostTransactionRequestBody
import com.overshoot.data.datasource.remote.model.transaction.PostTransactionResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.net.URL

interface MoneyGoalApiService {

    @POST("/")
    suspend fun callSomething(
        @Query("data1") data1: String,
        @Query("data2") data2: String,
        @Body body: RequestBody
    ): Something

    @POST("/widgets")
    suspend fun callWidget(): String

    @GET("/unauthorizedTest")
    fun getUnauthorizedTest(): Call<PostGoalResponse>

    @GET("/user")
    suspend fun getInfo(): Response<Employees>

    @POST("/goals")
    suspend fun postGoal(@Body body: PostGoalRequestBody): Response<PostGoalResponse>

    @POST("/transactions")
    suspend fun postTransaction(@Body body: PostTransactionRequestBody): Response<PostTransactionResponse>

    @GET("/transactions")
    suspend fun getTransactions(): Response<GetTransactionsResponse>

    @HTTP(method = "DELETE", path = "/transactions", hasBody = true)
    suspend fun deleteTransactions(@Body ids: List<String>): Response<String>

    @GET("/categories")
    suspend fun getCategories(): Response<GetCategoriesResponse>

    @GET("/begin-resumable-upload-session/{file_name}")
    suspend fun getUploadSession(@Path("file_name") filename: String): Response<URL>

    @Multipart
    @POST("/extract-info-from-receipt")
    suspend fun extractInfoFromImage(
        @Part bodyPart: MultipartBody.Part
    ): Response<ReceiptInfoResponse>

    @POST("/extract-info-from-receipt/{file_name}")
    suspend fun extractInfoFromImage2(
        @Path("file_name") filename: String,
        @Query("mime_file_type") type: String
    ): Response<ReceiptInfoResponse>

}