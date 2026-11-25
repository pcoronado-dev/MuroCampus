package com.coronado.murocampus.model

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(val id: Int, val name: String, val email: String)

// PASO 2
@Serializable
data class Post(val userId: Int, val id: Int, val title: String, val body: String)

@Serializable
data class Comentario(
    val postId: Int,
    val id: Int,
    val name: String,
    val email: String,
    val body: String
)

@Serializable
data class NuevoPost(val userId: Int, val title: String, val body: String)
