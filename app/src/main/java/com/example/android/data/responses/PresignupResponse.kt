package com.example.android.data.responses

import com.example.android.data.requests.SignupRequest

data class PresignupResponse(
    val `data`: SignupRequest?,
    val error: Any?,
    val message: String?
)
