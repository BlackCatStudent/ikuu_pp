package com.ikuuvpn.app.presentation.subscription.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikuuvpn.ikuu.model.SubscriptionConfig
import com.ikuuvpn.ikuu.subscription.SubscriptionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val subscriptionManager: SubscriptionManager
) : ViewModel() {

    private val _subscriptions = MutableStateFlow<List<SubscriptionConfig>>(emptyList())
    val subscriptions: StateFlow<List<SubscriptionConfig>> = _subscriptions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun importSubscription(url: String, name: String = "") {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = subscriptionManager.importSubscription(url)
                result.fold(
                    onSuccess = { config ->
                        val updatedConfig = config.copy(
                            metadata = config.metadata.copy(name = name.ifBlank { config.metadata.name })
                        )
                        _subscriptions.value = _subscriptions.value + updatedConfig
                    },
                    onFailure = { error ->
                        
                    }
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateSubscription(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val subscription = _subscriptions.value.find { it.hashCode() == id }
                subscription?.let {
                    val result = subscriptionManager.importSubscription(it.metadata.name)
                    result.fold(
                        onSuccess = { config ->
                            val updatedList = _subscriptions.value.map { sub ->
                                if (sub.hashCode() == id) config else sub
                            }
                            _subscriptions.value = updatedList
                        },
                        onFailure = { error ->

                        }
                    )
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteSubscription(id: Int) {
        _subscriptions.value = _subscriptions.value.filterNot { it.hashCode() == id }
    }

    fun refreshAllSubscriptions() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val updatedList = mutableListOf<SubscriptionConfig>()
                _subscriptions.value.forEach { subscription ->
                    val result = subscriptionManager.importSubscription(subscription.metadata.name)
                    result.fold(
                        onSuccess = { config ->
                            updatedList.add(config)
                        },
                        onFailure = {
                            updatedList.add(subscription)
                        }
                    )
                }
                _subscriptions.value = updatedList
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveSubscriptionToFile(subscription: SubscriptionConfig, file: File) {
        viewModelScope.launch {
            subscriptionManager.saveConfigToFile(subscription, file)
        }
    }
}