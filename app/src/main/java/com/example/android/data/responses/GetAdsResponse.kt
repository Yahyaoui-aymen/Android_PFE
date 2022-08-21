package com.example.android.data.responses

data class GetAdsResponse(
    val `data`: List<Ads>,
    val error: String,
    val message: String
)