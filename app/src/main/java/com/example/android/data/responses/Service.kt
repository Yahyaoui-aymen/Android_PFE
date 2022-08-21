package com.example.android.data.responses

data class Service(
    val availability: String,
    val description: String,
    val id: Int,
    val prestataire: User
)