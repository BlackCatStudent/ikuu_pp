package com.ikuuvpn.clashcore

object ClashNative {
    init {
        System.loadLibrary("clashcore")
    }

    external fun startClash(configPath: String): String
    external fun stopClash()
    external fun isRunning(): Boolean
    external fun getVersion(): String
}

class ClashCoreManager {
    
    fun start(configPath: String): Result<String> {
        return try {
            val result = ClashNative.startClash(configPath)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun stop() {
        ClashNative.stopClash()
    }
    
    fun isRunning(): Boolean {
        return ClashNative.isRunning()
    }
    
    fun getVersion(): String {
        return ClashNative.getVersion()
    }
}