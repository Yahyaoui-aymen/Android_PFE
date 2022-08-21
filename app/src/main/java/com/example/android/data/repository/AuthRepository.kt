package com.example.android.data.repository

import com.example.android.data.UserPreferences
import com.example.android.data.network.AuthApi
import com.example.android.data.requests.LoginRequest
import com.example.android.data.requests.RecoverpwdRequest
import com.example.android.data.requests.SignupRequest

class AuthRepository(
    private val api: AuthApi,
    private val preferences: UserPreferences
) : BaseRepository() {

    suspend fun loginClient( request: LoginRequest
    ) = safeApiCall {
        api.loginClient(request)
    }

    suspend fun presignupClient( request: SignupRequest
    ) = safeApiCall {
        api.presignupClient(request)
    }

    suspend fun signupClient( request: SignupRequest
    )= safeApiCall {
        api.signupClient(request)
    }

    suspend fun loginProvider( request: LoginRequest
    ) = safeApiCall {
        api.loginProvider(request)
    }

    suspend fun presignupProvider ( request: SignupRequest
    ) = safeApiCall {
        api.presignupProvider(request)
    }

    suspend fun signupProvider(request: SignupRequest
    ) = safeApiCall {
        api.signupProvider(request)
    }


    suspend fun recoverPwd ( request: RecoverpwdRequest
    )= safeApiCall {
        api.recoverpwd(request)
    }

    suspend fun updatePwd ( id: Long,
    password: String) = safeApiCall {
        api.updatePwd(id,password)
    }

    suspend fun saveAuthToken(token: String, id: Long, role: String) {
        preferences.saveAuthToken(token,id,role)
    }

    suspend fun resendOtp(phoneNumber : String
    ) = safeApiCall {
        api.resendOtp(phoneNumber)
    }

}