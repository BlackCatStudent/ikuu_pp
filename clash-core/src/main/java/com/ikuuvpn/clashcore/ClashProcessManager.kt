package com.ikuuvpn.clashcore

import android.content.Context
import android.os.Process
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClashProcessManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val coreManager: ClashCoreManager
) {
    private var process: Process? = null

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    private val _logs = MutableStateFlow<List<String>>(emptyList())
    val logs: StateFlow<List<String>> = _logs.asStateFlow()

    suspend fun start(configFile: File? = null): Result<String> = withContext(Dispatchers.IO) {
        try {
            if (_isRunning.value) {
                return@withContext Result.failure(Exception("Clash 已在运行"))
            }

            val coreFile = coreManager.getCoreFile()
            if (!coreFile.exists()) {
                return@withContext Result.failure(Exception("Clash 核心未安装"))
            }

            val config = configFile ?: coreManager.getConfigFile()
            if (!config.exists()) {
                return@withContext Result.failure(Exception("配置文件不存在"))
            }

            val command = listOf(
                coreFile.absolutePath,
                "-d", coreManager.getClashDir().absolutePath,
                "-f", config.absolutePath
            )

            val processBuilder = ProcessBuilder(command)
            processBuilder.redirectErrorStream(true)

            process = processBuilder.start()

            Thread {
                try {
                    val reader = BufferedReader(InputStreamReader(process?.inputStream))
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        addLog(line ?: "")
                    }
                } catch (e: Exception) {
                    addLog("日志读取错误: ${e.message}")
                }
            }.start()

            _isRunning.value = true
            Result.success("Clash 启动成功")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun stop(): Result<String> = withContext(Dispatchers.IO) {
        try {
            process?.destroy()
            process?.waitFor(5, java.util.concurrent.TimeUnit.SECONDS)

            if (process?.isAlive == true) {
                Process.killProcess(process?.pid() ?: 0)
            }

            process = null
            _isRunning.value = false
            Result.success("Clash 已停止")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun restart(configFile: File? = null): Result<String> {
        stop()
        return start(configFile)
    }

    fun getPid(): Int? = process?.pid()

    private fun addLog(message: String) {
        val currentLogs = _logs.value.toMutableList()
        currentLogs.add(message)
        if (currentLogs.size > 100) {
            currentLogs.removeAt(0)
        }
        _logs.value = currentLogs
    }

    fun clearLogs() {
        _logs.value = emptyList()
    }
}