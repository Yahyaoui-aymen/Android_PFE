package com.example.android.data.network

import com.example.android.data.requests.AppointmentRequest
import com.example.android.data.requests.LoginRequest
import com.example.android.data.requests.NotificationRequest
import com.example.android.data.responses.GetListAppointmentsResponse
import com.example.android.data.responses.LoginResponse
import com.example.android.data.responses.TimeSlotResponse
import retrofit2.http.*

interface ReservationApi {

    @GET("appointment/confirmedappointment/{providerid}")
    suspend fun confirmedAppointment(
        @Path("providerid") providerId: Long
    ) : GetListAppointmentsResponse


    @GET("timeslot/byprovider/{providerid}")
    suspend fun getTimeSlots(
        @Path("providerid") providerId: Long
    ) : TimeSlotResponse

    @FormUrlEncoded
    @POST("notification/send")
    suspend fun sendNotification(
        @Field("phoneTokens") phoneTokens: List<String>?,
        @Field("title") title: String,
        @Field("message") message: String
    )

    @POST("appointment/addrdv")
    suspend fun addAppointment(
        @Body request: AppointmentRequest
    )

    @POST("notification/add")
    suspend fun saveNotification(
        @Body request: NotificationRequest
    )

}