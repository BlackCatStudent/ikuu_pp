package com.ikuuvpn.ikuu.subscription

import android.util.Base64
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.ikuuvpn.ikuu.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubscriptionManager @Inject constructor(
    private val gson: Gson
) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun importSubscription(url: String): Result<SubscriptionConfig> {
        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url(url)
                    .build()

                val response = client.newCall(request).execute()
                
                if (!response.isSuccessful) {
                    return@withContext Result.failure(Exception("下载订阅失败: ${response.code}"))
                }

                val content = response.body?.string()
                    ?: return@withContext Result.failure(Exception("订阅内容为空"))

                val config = parseSubscriptionContent(content, url)
                Result.success(config)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun downloadSubscription(url: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url(url)
                    .build()

                val response = client.newCall(request).execute()
                
                if (!response.isSuccessful) {
                    return@withContext Result.failure(Exception("下载订阅失败: ${response.code}"))
                }

                val content = response.body?.string()
                    ?: return@withContext Result.failure(Exception("订阅内容为空"))

                Result.success(content)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    fun parseSubscriptionContent(content: String, sourceUrl: String): SubscriptionConfig {
        val decodedContent = if (isBase64(content)) {
            String(Base64.decode(content, Base64.DEFAULT))
        } else {
            content
        }

        return if (decodedContent.trim().startsWith("{") || decodedContent.trim().startsWith("[")) {
            parseJsonConfig(decodedContent, sourceUrl)
        } else {
            parseYamlConfig(decodedContent, sourceUrl)
        }
    }

    private fun isBase64(str: String): Boolean {
        return try {
            Base64.decode(str, Base64.DEFAULT)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun parseJsonConfig(content: String, sourceUrl: String): SubscriptionConfig {
        val json = JsonParser.parseString(content).asJsonObject
        
        val proxiesArray = json.getAsJsonArray("proxies")
        val proxies = proxiesArray.map { 
            gson.fromJson(it, ClashProxy::class.java) 
        }

        val proxyGroupsArray = json.getAsJsonArray("proxy-groups")
        val proxyGroups = proxyGroupsArray.map { 
            gson.fromJson(it, ProxyGroup::class.java) 
        }

        val rulesArray = json.getAsJsonArray("rules")
        val rules = rulesArray.map { it.asString }

        val metadata = SubscriptionMetadata(
            name = extractNameFromUrl(sourceUrl),
            updateTime = System.currentTimeMillis(),
            totalTraffic = 0,
            usedTraffic = 0,
            expireTime = 0
        )

        return SubscriptionConfig(proxies, proxyGroups, rules, metadata)
    }

    private fun parseYamlConfig(content: String, sourceUrl: String): SubscriptionConfig {
        val lines = content.lines()
        val proxies = mutableListOf<ClashProxy>()
        val proxyGroups = mutableListOf<ProxyGroup>()
        val rules = mutableListOf<String>()

        var currentSection = ""
        var currentProxyMap: MutableMap<String, Any>? = null
        var currentGroupMap: MutableMap<String, Any>? = null

        for (line in lines) {
            val trimmedLine = line.trim()
            
            when {
                trimmedLine.startsWith("proxies:") -> currentSection = "proxies"
                trimmedLine.startsWith("proxy-groups:") -> currentSection = "proxy-groups"
                trimmedLine.startsWith("rules:") -> currentSection = "rules"
                trimmedLine.startsWith("- ") -> {
                    when (currentSection) {
                        "proxies" -> {
                            val proxyData = parseProxyLine(trimmedLine.substring(2))
                            proxyData?.let { proxies.add(it) }
                        }
                        "proxy-groups" -> {
                            val groupData = parseGroupLine(trimmedLine.substring(2))
                            groupData?.let { proxyGroups.add(it) }
                        }
                        "rules" -> rules.add(trimmedLine.substring(2))
                    }
                }
            }
        }

        val metadata = SubscriptionMetadata(
            name = extractNameFromUrl(sourceUrl),
            updateTime = System.currentTimeMillis(),
            totalTraffic = 0,
            usedTraffic = 0,
            expireTime = 0
        )

        return SubscriptionConfig(proxies, proxyGroups, rules, metadata)
    }

    private fun parseProxyLine(line: String): ClashProxy? {
        val parts = line.split(", ")
        if (parts.size < 3) return null

        return try {
            ClashProxy(
                name = parts[0].trim(),
                type = parts[1].trim(),
                server = parts[2].trim(),
                port = parts.getOrNull(3)?.trim()?.toIntOrNull() ?: 0
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun parseGroupLine(line: String): ProxyGroup? {
        val parts = line.split(", ")
        if (parts.size < 3) return null

        return try {
            ProxyGroup(
                name = parts[0].trim(),
                type = parts[1].trim(),
                proxies = parts.drop(2).map { it.trim() }
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun extractNameFromUrl(url: String): String {
        return try {
            val uri = java.net.URI(url)
            val path = uri.path
            val segments = path.split("/")
            segments.lastOrNull() ?: "Unknown"
        } catch (e: Exception) {
            "Unknown"
        }
    }

    fun saveConfigToFile(config: SubscriptionConfig, file: File): Result<File> {
        return try {
            val yamlContent = generateYamlConfig(config)
            file.writeText(yamlContent)
            Result.success(file)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun generateYamlConfig(config: SubscriptionConfig): String {
        val sb = StringBuilder()
        
        sb.appendLine("proxies:")
        config.proxies.forEach { proxy ->
            sb.appendLine("  - name: ${proxy.name}")
            sb.appendLine("    type: ${proxy.type}")
            sb.appendLine("    server: ${proxy.server}")
            sb.appendLine("    port: ${proxy.port}")
            proxy.uuid?.let { sb.appendLine("    uuid: $it") }
            proxy.cipher?.let { sb.appendLine("    cipher: $it") }
            proxy.password?.let { sb.appendLine("    password: $it") }
            proxy.network?.let { sb.appendLine("    network: $it") }
            proxy.sni?.let { sb.appendLine("    sni: $it") }
            proxy.host?.let { sb.appendLine("    host: $it") }
            proxy.path?.let { sb.appendLine("    path: $it") }
            proxy.tls?.let { sb.appendLine("    tls: $it") }
        }

        sb.appendLine()
        sb.appendLine("proxy-groups:")
        config.proxyGroups.forEach { group ->
            sb.appendLine("  - name: ${group.name}")
            sb.appendLine("    type: ${group.type}")
            sb.appendLine("    proxies:")
            group.proxies.forEach { proxy ->
                sb.appendLine("      - $proxy")
            }
            group.url?.let { sb.appendLine("    url: $it") }
            group.interval?.let { sb.appendLine("    interval: $it") }
        }

        sb.appendLine()
        sb.appendLine("rules:")
        config.rules.forEach { rule ->
            sb.appendLine("  - $rule")
        }

        return sb.toString()
    }
}