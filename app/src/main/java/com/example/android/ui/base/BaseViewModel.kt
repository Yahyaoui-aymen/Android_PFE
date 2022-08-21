package com.example.android.ui.base

import androidx.lifecycle.ViewModel
import com.example.android.data.network.AuthApi
import com.example.android.data.repository.BaseRepository
import com.example.android.data.requests.LogoutRequest

abstract class BaseViewModel(
    private val repository: BaseRepository
): ViewModel() {

    suspend fun logout(api: AuthApi,request : LogoutRequest) = repository.logout(api, request)

    suspend fun deletePhoneToken(api: AuthApi,phoneToken : String) = repository.deletePhoneToken(api,phoneToken)
}