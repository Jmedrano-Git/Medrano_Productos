package com.tecsup.medrano.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.tecsup.medrano.data.model.Product
import kotlinx.coroutines.tasks.await

class ProductRepository(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {
    private val productsCollection = db.collection("products")
    fun getCurrentUserId(): String? = auth.currentUser?.uid

    // ---------- AUTH ----------
    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email.trim(), password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String): Result<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email.trim(), password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    // ---------- PRODUCTS ----------

    fun listenToUserProducts(
        onChange: (List<Product>) -> Unit,
        onError: (Throwable) -> Unit
    ): ListenerRegistration? {
        val userId = getCurrentUserId() ?: run {
            onChange(emptyList())
            return null
        }

        return productsCollection
            .whereEqualTo("userId", userId)
            .orderBy("codigo")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                val products = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Product::class.java)?.copy(id = doc.id)
                } ?: emptyList()

                onChange(products)
            }
    }

    suspend fun getProduct(productId: String): Product? {
        return try {
            val doc = productsCollection.document(productId).get().await()
            doc.toObject(Product::class.java)?.copy(id = doc.id)
        } catch (_: Exception) {
            null
        }
    }

    suspend fun addProduct(product: Product): Result<Unit> {
        return try {
            val userId = getCurrentUserId() ?: return Result.failure(Exception("Usuario no autenticado"))
            val productWithUser = product.copy(userId = userId)
            productsCollection.add(productWithUser.toMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProduct(productId: String, product: Product): Result<Unit> {
        return try {
            val userId = getCurrentUserId() ?: return Result.failure(Exception("Usuario no autenticado"))
            val productWithUser = product.copy(userId = userId)
            productsCollection.document(productId).update(productWithUser.toMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteProduct(productId: String): Result<Unit> {
        return try {
            productsCollection.document(productId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
