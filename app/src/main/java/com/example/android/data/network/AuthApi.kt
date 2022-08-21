package com.example.android.data.network

import com.example.android.data.requests.LoginRequest
import com.example.android.data.requests.LogoutRequest
import com.example.android.data.requests.RecoverpwdRequest
import com.example.android.data.requests.SignupRequest
import com.example.android.data.responses.*
import okhttp3.ResponseBody
import retrofit2.http.*

interface AuthApi {

    @POST("client/signin")
    suspend fun loginClient(
        @Body request: LoginRequest
    ) : LoginResponse

    @POST("client/presignup")
    suspend fun presignupClient(
        @Body request: SignupRequest
    ) : PresignupResponse

    @POST("client/signup")
    suspend fun signupClient(
        @Body request: SignupRequest
    ): SignupResponse

    @POST("prestataire/signin")
    suspend fun loginProvider(
        @Body request: LoginRequest
    ) : LoginResponse

    @POST("prestataire/presignup")
    suspend fun presignupProvider(
        @Body request: SignupRequest
    ) : PresignupResponse

    @POST("prestataire/signup")
    suspend fun signupProvider(
        @Body request: SignupRequest
    ) : SignupResponse

    @POST("auth/logout")
    suspend fun logout(
        @Body request: LogoutRequest
    ) : ResponseBody

    @POST("auth/recoverpwd")
    suspend fun recoverpwd(
        @Body request: RecoverpwdRequest
    ) : RecoverPwdResponse

    @FormUrlEncoded
    @POST("auth/updatepassword")
    suspend fun updatePwd(
        @Field("id") id : Long,
        @Field("password") password: String
    ) : RecoverPwdResponse

    @FormUrlEncoded
    @POST("auth/resentotp")
    suspend fun resendOtp(
        @Field("phoneNumber") phoneNumber: String
    ) : ResendOtpResponse

    @FormUrlEncoded
    @HTTP(method = "DELETE", path ="phonetoken/delete" , hasBody = true)
    suspend fun deletePhoneToken(
        @Field("phoneToken") phoneToken: String
    ) : Void


}