package com.example.android.data.requests

data class SignupRequest (
    val username: String,

    val email: String,

    val role: Set<String>?,

    val category: String?,

    val password: String,

    val firstName: String,

    val lastName: String,

    val government: String?,

    val city: String?,

    val phoneNumber: String,

    val imageUrl: String?,

    var otpCode: String?,
)
