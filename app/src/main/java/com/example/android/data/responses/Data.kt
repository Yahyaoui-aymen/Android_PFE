package com.example.android.data.responses

data class Data(
    val accountNonExpired: Boolean,
    val accountNonLocked: Boolean,
    val authorities: List<Authority>,
    val city: Any,
    val credentialsNonExpired: Boolean,
    val email: String,
    val enabled: Boolean,
    val firstName: String,
    val government: Any,
    val id: Long,
    val imageUrl: Any,
    val category: Category,
    val lastName: String,
    val phoneNumber: String,
    val username: String
)