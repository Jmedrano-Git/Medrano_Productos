package com.tecsup.medrano

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tecsup.medrano.data.repository.ProductRepository

object AppContainer {

    // Instancias únicas de Firebase
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    // Repository único para toda la app
    val productRepository: ProductRepository by lazy {
        ProductRepository(auth, db)
    }
}