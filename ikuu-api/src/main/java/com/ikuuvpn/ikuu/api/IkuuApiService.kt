package com.ikuuvpn.ikuu.api

import com.ikuuvpn.ikuu.model.LoginRequest
import com.ikuuvpn.ikuu.model.LoginResponse
import com.ikuuvpn.ikuu.model.SubscriptionInfo
import com.ikuuvpn.ikuu.model.UserInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface IkuuApiService {

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @GET("api/user/info")
    suspend fun getUserInfo(
        @Header("Authorization") token: String
    ): Response<UserInfo>

    @GET("api/user/subscription")
    suspend fun getSubscription(
        @Header("Authorization") token: String
    ): Response<SubscriptionInfo>

    @POST("api/user/checkin")
    suspend fun checkIn(
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>
}