package com.ikuuvpn.ikuu.model

data class SubscriptionLink(
    val url: String,
    val name: String = "",
    val isDefault: Boolean = false
)

data class SubscriptionConfig(
    val proxies: List<ClashProxy>,
    val proxyGroups: List<ProxyGroup>,
    val rules: List<String>,
    val metadata: SubscriptionMetadata
)

data class SubscriptionMetadata(
    val name: String,
    val updateTime: Long,
    val totalTraffic: Long,
    val usedTraffic: Long,
    val expireTime: Long
)