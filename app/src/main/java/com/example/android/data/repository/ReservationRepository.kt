package com.example.android.data.repository

import com.example.android.data.network.ReservationApi
import com.example.android.data.requests.AppointmentRequest
import com.example.android.data.requests.NotificationRequest

class ReservationRepository (
    private val api: ReservationApi
) : BaseRepository() {


    suspend fun getConfirmedAppointments (
        providerId : Long
    ) = safeApiCall {
        api.confirmedAppointment(providerId)
    }


    suspend fun getTimeSlots (
        providerId : Long
    ) = safeApiCall {
        api.getTimeSlots(providerId)
    }

    suspend fun  addAppointment (
        request: AppointmentRequest,
    ) = safeApiCall {
        api.addAppointment(request)
    }

    suspend fun sendNotification (
        phoneTokens : List<String>? ,
        title : String,
        message : String
    ) = safeApiCall {
        api.sendNotification(phoneTokens , title, message)
    }

    suspend fun saveNotification (
        request: NotificationRequest,
    ) = safeApiCall {
        api.saveNotification(request)
    }

}