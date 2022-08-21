package com.example.android.data.responses

data class FilterResponse (
    val `data`: List<User>?,
    val error: String?,
    val message: String?
        )