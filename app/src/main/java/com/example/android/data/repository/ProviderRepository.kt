package com.example.android.data.repository

import com.example.android.data.DataSource.city
import com.example.android.data.network.ProviderApi
import com.example.android.data.requests.NotificationRequest
import com.example.android.data.requests.PushNotificationRequest
import okhttp3.RequestBody

class ProviderRepository(
    private val api: ProviderApi
) : BaseRepository() {

    suspend fun getProfile() = safeApiCall {
        api.getProfile()
    }

    suspend fun getAllAds() = safeApiCall {
        api.getAllAds()
    }

    suspend fun getOffersByUserId(
        id: Long
    ) = safeApiCall {
        api.getOffersByUser(id)
    }

    suspend fun getAppointmentByProvider() = safeApiCall {
        api.getAppointmentByProvider()
    }

    suspend fun getConfirmedAppointment() = safeApiCall {
        api.getConfirmedAppointment()
    }

    suspend fun getProviderTimeSlot() = safeApiCall {
        api.getProviderTimeSlot()
    }


    suspend fun addOffer(
        requestBody: RequestBody,
    ) = safeApiCall {
        api.addOffer(requestBody)
    }

    suspend fun confirmAppointment(
        rdvId : Long
    ) = safeApiCall {
        api.confirmAppointment(rdvId)
    }

    suspend fun rejectAppointment(
        rdvId : Long
    ) = safeApiCall {
        api.rejectAppointment(rdvId)
    }

    suspend fun removeAppointment(
        rdvId : Long
    ) = safeApiCall {
        api.removeAppointment(rdvId)
    }


    suspend fun sendNotification (
        phoneTokens : List<String>? ,
        title : String,
        message : String
    ) = safeApiCall {
        api.sendNotification(phoneTokens , title, message)
    }

    suspend fun updateProfile(
        requestBody: RequestBody
    ) = safeApiCall {
        api.updateProfile(requestBody)
    }


    suspend fun saveNotification (
        request: NotificationRequest,
    ) = safeApiCall {
        api.saveNotification(request)
    }


}