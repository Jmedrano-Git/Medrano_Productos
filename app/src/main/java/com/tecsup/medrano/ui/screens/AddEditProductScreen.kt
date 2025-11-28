package com.tecsup.medrano.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.medrano.data.model.Product
import com.tecsup.medrano.ui.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(
    productViewModel: ProductViewModel,
    productId: String?,
    onSaveSuccess: () -> Unit,
    onCancel: () -> Unit
) {
    val isEditMode = productId != null

    var codigo by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("Disponible") }
    var categoria by remember { mutableStateOf("") }

    var localError by remember { mutableStateOf<String?>(null) }

    val viewModelError = productViewModel.errorMessage
    val isLoading = productViewModel.isLoading

    val scope = rememberCoroutineScope()

    // Cargar datos en modo edición
    LaunchedEffect(productId) {
        if (isEditMode && productId != null) {
            val product = productViewModel.getProduct(productId)
            product?.let {
                codigo = it.codigo
                nombre = it.nombre
                descripcion = it.descripcion
                precio = it.precio.toString()
                cantidad = it.cantidad.toString()
                estado = it.estado
                categoria = it.categoria
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Editar producto" else "Nuevo producto") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = codigo,
                onValueChange = { codigo = it },
                label = { Text("Código") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio (S/.)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = cantidad,
                onValueChange = { cantidad = it },
                label = { Text("Cantidad") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoría") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                TextButton(onClick = { estado = "Disponible" }) {
                    Text("Disponible")
                }
                TextButton(onClick = { estado = "Agotado" }) {
                    Text("Agotado")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            (localError ?: viewModelError)?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onCancel) {
                    Text("Cancelar")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        when {
                            codigo.isBlank() -> localError = "El código es obligatorio"
                            nombre.isBlank() -> localError = "El nombre es obligatorio"
                            descripcion.isBlank() -> localError = "La descripción es obligatoria"
                            else -> {
                                localError = null
                                val product = Product(
                                    codigo = codigo,
                                    nombre = nombre,
                                    descripcion = descripcion,
                                    precio = precio.toDoubleOrNull() ?: 0.0,
                                    cantidad = cantidad.toIntOrNull() ?: 0,
                                    estado = estado,
                                    categoria = categoria
                                )

                                scope.launch {
                                    productViewModel.saveProduct(productId, product, onSaveSuccess)
                                }
                            }
                        }
                    }
                ) {
                    Text(if (isLoading) "Guardando..." else "Guardar")
                }
            }
        }
    }
}