package com.example.android.data.responses

data class User(
    val category: Category,
    val city: String,
    val email: String,
    val firstName: String,
    val government: String,
    val id: Long,
    val imageUrl: String,
    val lastName: String,
    var otpCode: String,
    val password: String,
    val phoneNumber: String,
    val roles: List<Role>,
    val username: String,
    val phoneToken : List<PhoneToken>
)