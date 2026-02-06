package com.ikuuvpn.common.util

import android.content.Context
import android.util.Base64
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CryptoUtils {

    fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(input.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }

    fun sha256(input: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(input.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }

    fun encodeBase64(input: String): String {
        return Base64.encodeToString(input.toByteArray(), Base64.NO_WRAP)
    }

    fun decodeBase64(input: String): String {
        return String(Base64.decode(input, Base64.NO_WRAP))
    }
}

object DateUtils {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    fun formatTimestamp(timestamp: Long): String {
        return dateFormat.format(Date(timestamp))
    }

    fun formatDuration(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        
        return when {
            hours > 0 -> "${hours}小时${minutes}分钟"
            minutes > 0 -> "${minutes}分钟${secs}秒"
            else -> "${secs}秒"
        }
    }

    fun isExpired(expiredAt: Long): Boolean {
        return System.currentTimeMillis() > expiredAt
    }

    fun getRemainingDays(expiredAt: Long): Long {
        val remainingMs = expiredAt - System.currentTimeMillis()
        return remainingMs / (24 * 60 * 60 * 1000)
    }
}

object TrafficUtils {

    fun formatTraffic(bytes: Long): String {
        return when {
            bytes >= 1024 * 1024 * 1024 -> String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024))
            bytes >= 1024 * 1024 -> String.format("%.2f MB", bytes / (1024.0 * 1024))
            bytes >= 1024 -> String.format("%.2f KB", bytes / 1024.0)
            else -> "$bytes B"
        }
    }

    fun calculateTrafficUsed(upload: Long, download: Long): Long {
        return upload + download
    }

    fun calculateTrafficRemaining(total: Long, used: Long): Long {
        return (total - used).coerceAtLeast(0)
    }

    fun calculateTrafficPercentage(used: Long, total: Long): Float {
        return if (total > 0) (used.toFloat() / total * 100) else 0f
    }
}