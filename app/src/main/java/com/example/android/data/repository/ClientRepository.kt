package com.example.android.data.repository

import com.example.android.data.UserPreferences
import com.example.android.data.network.AuthApi
import com.example.android.data.network.ClientApi
import com.example.android.data.requests.LoginRequest
import com.example.android.data.requests.SignupRequest
import okhttp3.RequestBody

class ClientRepository(
    private val api: ClientApi
) : BaseRepository() {

    suspend fun getProfile() = safeApiCall {
        api.getProfile()
    }

    suspend fun getAllAds() = safeApiCall {
        api.getAllAds()
    }

    suspend fun getAllOffers() = safeApiCall {
        api.getAllOffers()
    }



    suspend fun getByCategoryGovernmentCity (
        categoryId : Int ,
        government : String?,
        city : String?
    ) = safeApiCall {
        api.getByCategoryGovernmentCity(categoryId , government, city)
    }

    suspend fun updateProfile(
        requestBody: RequestBody
    ) = safeApiCall {
        api.updateProfile(requestBody)
    }

}