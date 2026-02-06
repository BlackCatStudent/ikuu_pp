package com.ikuuvpn.ikuu.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val data: LoginData?
)

data class LoginData(
    val token: String,
    val user: UserInfo
)

data class UserInfo(
    val id: Int,
    val email: String,
    val username: String,
    val balance: Double,
    val transferEnable: Long,
    val u: Long,
    val d: Long,
    val planId: Int,
    val expiredAt: Long,
    val createdAt: Long
)

data class SubscriptionInfo(
    val id: Int,
    val userId: Int,
    val planId: Int,
    val token: String,
    val link: String,
    val resetDay: Int,
    val trafficUsed: Long,
    val trafficTotal: Long,
    val expiredAt: Long
)

data class ClashProxy(
    val name: String,
    val type: String,
    val server: String,
    val port: Int,
    val uuid: String? = null,
    val alterId: Int? = null,
    val cipher: String? = null,
    val password: String? = null,
    val network: String? = null,
    val security: String? = null,
    val sni: String? = null,
    val host: String? = null,
    val path: String? = null,
    val tls: Boolean? = null
)

data class ProxyGroup(
    val name: String,
    val type: String,
    val proxies: List<String>,
    val url: String? = null,
    val interval: Int? = null
)