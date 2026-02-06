package com.ikuuvpn.app.presentation.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikuuvpn.ikuu.auth.IkuuAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authManager: IkuuAuthManager
) : ViewModel() {

    fun login(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = authManager.login(email, password)
            result.fold(
                onSuccess = { callback(true, null) },
                onFailure = { error -> callback(false, error.message) }
            )
        }
    }
}