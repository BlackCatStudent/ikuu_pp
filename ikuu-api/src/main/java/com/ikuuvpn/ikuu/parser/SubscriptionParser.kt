package com.ikuuvpn.ikuu.parser

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.ikuuvpn.ikuu.model.SubscriptionInfo
import com.ikuuvpn.ikuu.model.ClashProxy
import com.ikuuvpn.ikuu.model.ProxyGroup

data class ClashConfig(
    val proxies: List<ClashProxy>,
    val proxyGroups: List<ProxyGroup>,
    val rules: List<String>
)

class SubscriptionParser(private val gson: Gson) {

    fun parseSubscription(content: String): ClashConfig {
        val decodedContent = if (isBase64(content)) {
            String(android.util.Base64.decode(content, android.util.Base64.DEFAULT))
        } else {
            content
        }

        return if (decodedContent.trim().startsWith("{")) {
            parseJsonConfig(decodedContent)
        } else {
            parseYamlConfig(decodedContent)
        }
    }

    private fun isBase64(str: String): Boolean {
        return try {
            android.util.Base64.decode(str, android.util.Base64.DEFAULT)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun parseJsonConfig(content: String): ClashConfig {
        val json = JsonParser.parseString(content).asJsonObject
        val proxiesArray = json.getAsJsonArray("proxies")
        val proxies = proxiesArray.map { gson.fromJson(it, ClashProxy::class.java) }

        val proxyGroupsArray = json.getAsJsonArray("proxy-groups")
        val proxyGroups = proxyGroupsArray.map { gson.fromJson(it, ProxyGroup::class.java) }

        val rulesArray = json.getAsJsonArray("rules")
        val rules = rulesArray.map { it.asString }

        return ClashConfig(proxies, proxyGroups, rules)
    }

    private fun parseYamlConfig(content: String): ClashConfig {
        val lines = content.lines()
        val proxies = mutableListOf<ClashProxy>()
        val proxyGroups = mutableListOf<ProxyGroup>()
        val rules = mutableListOf<String>()

        var currentSection = ""
        var currentProxy: MutableMap<String, Any>? = null
        var currentGroup: MutableMap<String, Any>? = null

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

        return ClashConfig(proxies, proxyGroups, rules)
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

    fun convertToClashConfig(subscriptionInfo: SubscriptionInfo, subscriptionContent: String): ClashConfig {
        return parseSubscription(subscriptionContent)
    }
}