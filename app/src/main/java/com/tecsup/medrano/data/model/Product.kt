package com.tecsup.medrano.data.model

data class Product(
    val id: String = "",
    val codigo: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val cantidad: Int = 0,
    val estado: String = "Disponible",
    val categoria: String = "",
    val userId: String = ""
) {
    constructor() : this("", "", "", "", 0.0, 0, "Disponible", "", "")

    fun toMap(): Map<String, Any> = mapOf(
        "codigo" to codigo,
        "nombre" to nombre,
        "descripcion" to descripcion,
        "precio" to precio,
        "cantidad" to cantidad,
        "estado" to estado,
        "categoria" to categoria,
        "userId" to userId
    )
}