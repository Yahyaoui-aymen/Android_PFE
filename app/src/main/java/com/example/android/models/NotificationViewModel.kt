package com.example.android.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.data.network.Resource
import com.example.android.data.repository.ClientRepository
import com.example.android.data.repository.NotificationRepository
import com.example.android.data.responses.GetNotificationResponse
import com.example.android.data.responses.SignupResponse
import com.example.android.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val repository: NotificationRepository,
) : BaseViewModel(repository) {

    private val _notifications: MutableLiveData<Resource<GetNotificationResponse>> = MutableLiveData()
    val notification: LiveData<Resource<GetNotificationResponse>>
        get() = _notifications

    init {
        getNotificationByUser()
    }


    fun getNotificationByUser() = viewModelScope.launch {
        _notifications.value = Resource.Loading
        _notifications.value = repository.getNotificationByUser()
    }

}