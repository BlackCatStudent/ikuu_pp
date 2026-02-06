package com.ikuuvpn.app.presentation.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikuuvpn.clashcore.ClashCoreManager
import com.ikuuvpn.clashcore.ClashProcessManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val coreManager: ClashCoreManager,
    private val processManager: ClashProcessManager
) : ViewModel() {

    private val _coreInstalled = MutableStateFlow(false)
    val coreInstalled: StateFlow<Boolean> = _coreInstalled.asStateFlow()

    private val _isDownloading = MutableStateFlow(false)
    val isDownloading: StateFlow<Boolean> = _isDownloading.asStateFlow()

    private val _downloadProgress = MutableStateFlow(0f)
    val downloadProgress: StateFlow<Float> = _downloadProgress.asStateFlow()

    private val _clashRunning = MutableStateFlow(false)
    val clashRunning: StateFlow<Boolean> = _clashRunning.asStateFlow()

    private val _coreVersion = MutableStateFlow("")
    val coreVersion: StateFlow<String> = _coreVersion.asStateFlow()

    init {
        checkCoreStatus()
        checkClashStatus()
    }

    private fun checkCoreStatus() {
        _coreInstalled.value = coreManager.isCoreInstalled()
        if (_coreInstalled.value) {
            _coreVersion.value = ClashNative.getVersion()
        }
    }

    private fun checkClashStatus() {
        _clashRunning.value = processManager.isRunning.value
    }

    fun downloadCore() {
        viewModelScope.launch {
            _isDownloading.value = true
            _downloadProgress.value = 0f

            val coreUrl = "https://github.com/MetaCubeX/mihomo/releases/download/v1.18.0/mihomo-android-arm64-v8a"

            try {
                val result = coreManager.downloadCore(coreUrl)
                result.fold(
                    onSuccess = {
                        _downloadProgress.value = 100f
                        _coreInstalled.value = true
                        _coreVersion.value = ClashNative.getVersion()
                    },
                    onFailure = { error ->
                        
                    }
                )
            } finally {
                _isDownloading.value = false
            }
        }
    }

    fun deleteCore() {
        viewModelScope.launch {
            try {
                coreManager.getCoreFile().delete()
                _coreInstalled.value = false
                _coreVersion.value = ""
            } catch (e: Exception) {

            }
        }
    }

    fun startClash() {
        viewModelScope.launch {
            val result = processManager.start()
            result.fold(
                onSuccess = {
                    _clashRunning.value = true
                },
                onFailure = { error ->

                }
            )
        }
    }

    fun stopClash() {
        viewModelScope.launch {
            val result = processManager.stop()
            result.fold(
                onSuccess = {
                    _clashRunning.value = false
                },
                onFailure = { error ->

                }
            )
        }
    }

    fun downloadCountryDb() {
        viewModelScope.launch {
            val dbUrl = "https://github.com/MetaCubeX/meta-rules-dat/releases/download/latest/country.mmdb"
            coreManager.downloadCountryDb(dbUrl)
        }
    }

    fun clearCache() {
        viewModelScope.launch {
            coreManager.clearCache()
        }
    }
}