package com.ikuuvpn.clashcore

import android.content.Context
import android.os.Environment
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClashCoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val clashDir: File by lazy {
        File(context.filesDir, "clash").apply { mkdirs() }
    }

    private val configDir: File by lazy {
        File(clashDir, "config").apply { mkdirs() }
    }

    private val coreFile: File by lazy {
        File(clashDir, "clash-meta")
    }

    private val configFile: File by lazy {
        File(configDir, "config.yaml")
    }

    private val mmdbFile: File by lazy {
        File(clashDir, "Country.mmdb")
    }

    suspend fun downloadCore(url: String): Result<File> = withContext(Dispatchers.IO) {
        try {
            val request = okhttp3.Request.Builder()
                .url(url)
                .build()

            val response = okhttp3.OkHttpClient().newCall(request).execute()

            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("下载失败: ${response.code}"))
            }

            val inputStream = response.body?.byteStream()
                ?: return@withContext Result.failure(Exception("响应体为空"))

            FileOutputStream(coreFile).use { output ->
                inputStream.copyTo(output)
            }

            coreFile.setExecutable(true)
            Result.success(coreFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun downloadCountryDb(url: String): Result<File> = withContext(Dispatchers.IO) {
        try {
            val request = okhttp3.Request.Builder()
                .url(url)
                .build()

            val response = okhttp3.OkHttpClient().newCall(request).execute()

            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("下载失败: ${response.code}"))
            }

            val inputStream = response.body?.byteStream()
                ?: return@withContext Result.failure(Exception("响应体为空"))

            FileOutputStream(mmdbFile).use { output ->
                inputStream.copyTo(output)
            }

            Result.success(mmdbFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun saveConfig(yamlContent: String): Result<File> {
        return try {
            configFile.writeText(yamlContent)
            Result.success(configFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getConfigFile(): File = configFile

    fun getCoreFile(): File = coreFile

    fun isCoreInstalled(): Boolean = coreFile.exists()

    fun isConfigExists(): Boolean = configFile.exists()

    fun getClashDir(): File = clashDir

    fun clearCache() {
        try {
            clashDir.listFiles()?.forEach { file ->
                if (file.name != "clash-meta" && file.name != "Country.mmdb") {
                    file.deleteRecursively()
                }
            }
        } catch (e: Exception) {

        }
    }
}