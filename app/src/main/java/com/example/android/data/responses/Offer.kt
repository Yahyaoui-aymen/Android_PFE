package com.example.android.data.responses

data class Offer(
    val description: String,
    val id: Int,
    val image: String,
    val price: String,
    val subtitle: String,
    val title: String,
    val user: User
)