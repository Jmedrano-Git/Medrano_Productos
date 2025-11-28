package com.tecsup.medrano.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.tecsup.medrano.AppContainer
import com.tecsup.medrano.data.model.Product
import com.tecsup.medrano.data.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: ProductRepository = AppContainer.productRepository
) : ViewModel() {

    var products by mutableStateOf<List<Product>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private var listenerRegistration: ListenerRegistration? = null

    init {
        startListeningProducts()
    }

    private fun startListeningProducts() {
        listenerRegistration?.remove()
        listenerRegistration = repository.listenToUserProducts(
            onChange = { products = it },
            onError = { errorMessage = it.message }
        )
    }

    fun refresh() {
        startListeningProducts()
    }

    fun getCurrentUserEmail(): String {
        return FirebaseAuth.getInstance().currentUser?.email ?: "Usuario"
    }

    fun saveProduct(
        productId: String?,
        product: Product,
        onSuccess: () -> Unit
    ) {
        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            val result = if (productId == null) {
                repository.addProduct(product)
            } else {
                repository.updateProduct(productId, product)
            }

            isLoading = false
            if (result.isSuccess) {
                onSuccess()
            } else {
                errorMessage = result.exceptionOrNull()?.message
                    ?: "Error al guardar el producto"
            }
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            repository.deleteProduct(productId)
        }
    }

    suspend fun getProduct(productId: String): Product? {
        return repository.getProduct(productId)
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}