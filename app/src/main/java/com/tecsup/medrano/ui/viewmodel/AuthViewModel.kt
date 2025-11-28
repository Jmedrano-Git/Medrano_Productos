package com.tecsup.medrano.ui.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.medrano.AppContainer
import com.tecsup.medrano.data.repository.ProductRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: ProductRepository = AppContainer.productRepository
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun clearError() {
        errorMessage = null
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        val cleanEmail = email.trim()

        when {
            cleanEmail.isEmpty() || password.isEmpty() -> {
                errorMessage = "Por favor completa todos los campos"
                return
            }
            !Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches() -> {
                errorMessage = "El correo no tiene un formato válido"
                return
            }
        }

        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            val result = repository.login(cleanEmail, password)
            isLoading = false
            if (result.isSuccess) {
                onSuccess()
            } else {
                errorMessage = result.exceptionOrNull()?.message ?: "Error al iniciar sesión"
            }
        }
    }

    fun register(email: String, password: String, confirmPassword: String, onSuccess: () -> Unit) {
        val cleanEmail = email.trim()

        when {
            cleanEmail.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ->
                errorMessage = "Completa todos los campos"
            !Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches() ->
                errorMessage = "El correo no es válido"
            password.length < 6 ->
                errorMessage = "La contraseña debe tener al menos 6 caracteres"
            password != confirmPassword ->
                errorMessage = "Las contraseñas no coinciden"
            else -> {
                isLoading = true
                errorMessage = null

                viewModelScope.launch {
                    val result = repository.register(cleanEmail, password)
                    isLoading = false
                    if (result.isSuccess) {
                        onSuccess()
                    } else {
                        errorMessage = result.exceptionOrNull()?.message
                            ?: "Error al registrar usuario"
                    }
                }
            }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        repository.logout()
        onSuccess()
    }
}