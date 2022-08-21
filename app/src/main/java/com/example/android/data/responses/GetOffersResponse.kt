package com.example.android.data.responses

data class GetOffersResponse(
    val `data`: List<Offer>,
    val error: Any,
    val message: String
)