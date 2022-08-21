package com.example.android.data.repository

import com.example.android.data.network.AuthApi
import com.example.android.data.network.Resource
import com.example.android.data.requests.LogoutRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRepository {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ) :Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            }catch (throwable: Throwable) {
                when(throwable){
                    is HttpException -> {
                        Resource.Failure(false,throwable.code(), throwable.response()?.errorBody())
                    }
                    else -> {
                        Resource.Failure(true, null, null)
                    }
                }
            }
        }
    }

    suspend fun logout(api : AuthApi , request: LogoutRequest
    ) = safeApiCall {
        api.logout(request)
    }

    suspend fun deletePhoneToken(api : AuthApi , phoneToken: String) = safeApiCall {
        api.deletePhoneToken(phoneToken)
    }
}