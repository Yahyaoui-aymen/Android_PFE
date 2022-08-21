package com.example.android.data.responses

data class LoginResponse(
    val error: Any?,
    val message: String?,
    val data: LoggingUser?
)

    data class LoggingUser( val accessToken: String,
                 val email: String,
                 val id: Long,
                 val refreshToken: String,
                 val roles: List<String>,
                 val type: String,
                 val username: String)