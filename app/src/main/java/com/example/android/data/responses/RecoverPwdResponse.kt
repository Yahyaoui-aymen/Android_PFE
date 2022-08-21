package com.example.android.data.responses

data class RecoverPwdResponse(
    val `data`: User?,
    val error: Any?,
    val message: String?
)