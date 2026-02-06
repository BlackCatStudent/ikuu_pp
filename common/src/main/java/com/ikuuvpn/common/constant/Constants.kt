package com.ikuuvpn.common.constant

object Constants {
    const val PREF_NAME = "ikuuvpn_prefs"
    const val KEY_TOKEN = "auth_token"
    const val KEY_USER_EMAIL = "user_email"
    const val KEY_USER_ID = "user_id"
    const val KEY_SUBSCRIPTION_URL = "subscription_url"
    const val KEY_SELECTED_PROXY = "selected_proxy"
    
    const val CLASH_CONFIG_PATH = "/data/local/tmp/clash/config.yaml"
    const val CLASH_CORE_PATH = "/data/local/tmp/clash/clash-meta"
    
    const val NOTIFICATION_CHANNEL_ID = "vpn_service_channel"
    const val NOTIFICATION_ID = 1001
    
    const val TRAFFIC_UPDATE_INTERVAL = 1000L
    const val SUBSCRIPTION_UPDATE_INTERVAL = 3600000L
}

object ProxyType {
    const val SS = "ss"
    const val SSR = "ssr"
    const val VMess = "vmess"
    const val Vless = "vless"
    const val Trojan = "trojan"
    const val Snell = "snell"
}

object NetworkType {
    const val TCP = "tcp"
    const val WS = "ws"
    const val GRPC = "grpc"
    const val H2 = "h2"
}

object SecurityType {
    const val NONE = "none"
    const val AUTO = "auto"
    const val AES_128_GCM = "aes-128-gcm"
    const val CHACHA20_POLY1305 = "chacha20-poly1305"
}