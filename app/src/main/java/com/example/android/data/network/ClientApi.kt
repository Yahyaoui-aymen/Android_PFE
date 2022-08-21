package com.example.android.data.network

import com.example.android.data.responses.*
import okhttp3.RequestBody
import retrofit2.http.*

interface ClientApi {

    @GET("auth/profile")
    suspend fun getProfile() : SignupResponse

    @GET("ads/all")
    suspend fun getAllAds() : GetAdsResponse

    @GET("offer/all")
    suspend fun getAllOffers(): GetOffersResponse



    @FormUrlEncoded
    @POST("prestataire/getbycategory")
    suspend fun getByCategoryGovernmentCity(
        @Field("categoryid") id : Int ,
        @Field("government") government : String? ,
        @Field("city") city : String?
    ) : FilterResponse


    @PUT("auth/updateprofile")
    suspend fun updateProfile(
        @Body request: RequestBody
    ) : SignupResponse

}