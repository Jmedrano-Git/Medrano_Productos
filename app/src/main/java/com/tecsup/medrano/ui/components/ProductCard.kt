package com.tecsup.medrano.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tecsup.medrano.data.model.Product

@Composable
fun ProductCard(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Código
            Text(
                text = "Código: ${product.codigo}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Nombre
            Text(
                text = product.nombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold, // celeste oscuro
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Estado y categoría
            Text(
                text = "${product.estado} • ${product.categoria}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Descripción
            Text(
                text = product.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Precio y cantidad
            Text(
                text = "Precio: S/ ${String.format("%.2f", product.precio)}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Cantidad: ${product.cantidad} unidades",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

    // Confirmación
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Producto") },
            text = { Text("¿Estás seguro de que deseas eliminar este producto?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDelete()
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}