package com.example.android.data.network

import android.app.DownloadManager
import com.example.android.data.requests.NotificationRequest
import com.example.android.data.requests.PushNotificationRequest
import com.example.android.data.requests.SignupRequest
import com.example.android.data.responses.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File

interface ProviderApi {

    @GET("auth/profile")
    suspend fun getProfile() : SignupResponse

    @GET("ads/all")
    suspend fun getAllAds() : GetAdsResponse

    @GET("offer/getbyuser/{id}")
    suspend fun getOffersByUser(
        @Path("id") id: Long
    ): GetOffersResponse

    @GET("appointment/byprovider")
    suspend fun getAppointmentByProvider() : GetListAppointmentsResponse

    @GET("appointment/confirmedappointment")
    suspend fun getConfirmedAppointment() : GetListAppointmentsResponse

    @GET("timeslot/byprovider")
    suspend fun getProviderTimeSlot(): TimeSlotResponse


    @POST("offer/add")
    suspend fun addOffer(
        @Body request: RequestBody
    ): OfferResponse


    @POST("appointment/confirm/{rdv_id}")
    suspend fun confirmAppointment(
        @Path("rdv_id") rdvId: Long
    )

    @POST("appointment/reject/{rdv_id}")
    suspend fun rejectAppointment(
        @Path("rdv_id") rdvId: Long
    )

    @POST("appointment/delete/{rdv_id}")
    suspend fun removeAppointment(
        @Path("rdv_id") rdvId: Long
    )


    @FormUrlEncoded
    @POST("notification/send")
    suspend fun sendNotification(
        @Field("phoneTokens") phoneTokens: List<String>?,
        @Field("title") title: String,
        @Field("message") message: String
    )

    @PUT("auth/updateprofile")
    suspend fun updateProfile(
        @Body request: RequestBody
    ) : SignupResponse


    @POST("notification/add")
    suspend fun saveNotification(
        @Body request: NotificationRequest
    )


}