package com.ikuuvpn.ikuu.auth

import com.ikuuvpn.ikuu.api.IkuuApiService
import com.ikuuvpn.ikuu.model.LoginRequest
import com.ikuuvpn.ikuu.model.LoginResponse
import com.ikuuvpn.ikuu.model.UserInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IkuuAuthManager @Inject constructor(
    private val apiService: IkuuApiService
) {
    private var currentToken: String? = null
    private var currentUserInfo: UserInfo? = null

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val request = LoginRequest(email, password)
            val response = apiService.login(request)
            
            if (response.isSuccessful && response.body()?.success == true) {
                val loginData = response.body()?.data
                currentToken = loginData?.token
                currentUserInfo = loginData?.user
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "登录失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserInfo(): Result<UserInfo> {
        return try {
            val token = currentToken ?: return Result.failure(Exception("未登录"))
            val response = apiService.getUserInfo("Bearer $token")
            
            if (response.isSuccessful) {
                currentUserInfo = response.body()
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("获取用户信息失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getToken(): String? = currentToken

    fun getUserInfo(): UserInfo? = currentUserInfo

    fun isLoggedIn(): Boolean = currentToken != null

    fun logout() {
        currentToken = null
        currentUserInfo = null
    }
}