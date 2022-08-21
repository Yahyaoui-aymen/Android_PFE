package com.example.android.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.data.SingleLiveEvent
import com.example.android.data.network.Resource
import com.example.android.data.repository.ProviderRepository
import com.example.android.data.requests.NotificationRequest
import com.example.android.data.requests.PushNotificationRequest
import com.example.android.data.responses.*
import com.example.android.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class ProviderViewModel(
    private val repository: ProviderRepository
) : BaseViewModel(repository) {


    private val _provider: MutableLiveData<Resource<SignupResponse>> = MutableLiveData()
    val provider: LiveData<Resource<SignupResponse>>
        get() = _provider

    private val _ads: MutableLiveData<Resource<GetAdsResponse>> = MutableLiveData()
    val ads: LiveData<Resource<GetAdsResponse>>
        get() = _ads

    private val _getAppointmentByProviderResponse: MutableLiveData<Resource<GetListAppointmentsResponse>> =
        MutableLiveData()
    val getAppointmentByProviderResponse: LiveData<Resource<GetListAppointmentsResponse>>
        get() = _getAppointmentByProviderResponse

    private val _confirmedAppointment : MutableLiveData<Resource<GetListAppointmentsResponse>> = MutableLiveData()
    val confirmedAppointment: LiveData<Resource<GetListAppointmentsResponse>>
        get() = _confirmedAppointment

    private val _timeSlots : MutableLiveData<Resource<TimeSlotResponse>> = MutableLiveData()
    val timeSlots: LiveData<Resource<TimeSlotResponse>>
        get() = _timeSlots

    private val _addOfferResponse: MutableLiveData<Resource<OfferResponse>> = MutableLiveData()
    val addOfferResponse: LiveData<Resource<OfferResponse>>
        get() = _addOfferResponse

    private val _getOfferResponse: MutableLiveData<Resource<GetOffersResponse>> = MutableLiveData()
    val getOffersResponse: LiveData<Resource<GetOffersResponse>>
        get() = _getOfferResponse

    private val _updateProfileResponse: SingleLiveEvent<Resource<SignupResponse>> = SingleLiveEvent()
    val updateProfileResponse: LiveData<Resource<SignupResponse>>
        get() = _updateProfileResponse

    init {
        getAllAds()
        getProfile()
    }

    fun getProfile() = viewModelScope.launch {
        _provider.value = Resource.Loading
        _provider.value = repository.getProfile()
    }

    fun getAllAds() = viewModelScope.launch {
        _ads.value = Resource.Loading
        _ads.value = repository.getAllAds()
    }

    fun getOfferByUserId(
        id: Long
    ) = viewModelScope.launch {
        _getOfferResponse.value = Resource.Loading
        _getOfferResponse.value = repository.getOffersByUserId(id)
    }

    fun getAppointmentByProvider() = viewModelScope.launch {
        _getAppointmentByProviderResponse.value = Resource.Loading
        _getAppointmentByProviderResponse.value = repository.getAppointmentByProvider()
    }

    fun getConfirmedAppointment() = viewModelScope.launch {
        _confirmedAppointment.value = Resource.Loading
        _confirmedAppointment.value = repository.getConfirmedAppointment()
    }

    fun getProviderTimeSlot() = viewModelScope.launch {
        _timeSlots.value = Resource.Loading
        _timeSlots.value = repository.getProviderTimeSlot()
    }
    fun addOffer(
        requestBody: RequestBody
    ) = viewModelScope.launch {
        repository.addOffer(requestBody)
    }

    fun confirmAppointment(
        request : NotificationRequest,
        rdvId : Long,
        phoneTokens : List<String>? ,
        title : String,
        message : String
    ) = viewModelScope.launch {
        repository.confirmAppointment(rdvId)
        repository.sendNotification(phoneTokens,title,message)
        repository.saveNotification(request)
        getAppointmentByProvider()
    }

    fun rejectAppointment(
        request : NotificationRequest,
        rdvId : Long,
        phoneTokens : List<String>? ,
        title : String,
        message : String
    ) = viewModelScope.launch {
        repository.rejectAppointment(rdvId)
        repository.sendNotification(phoneTokens,title,message)
        repository.saveNotification(request)
        getAppointmentByProvider()
    }

    fun removeAppointment(
        rdvId: Long,
    ) = viewModelScope.launch {
        repository.removeAppointment(rdvId)
        getAppointmentByProvider()
    }

    fun updateProfile(
        requestBody: RequestBody
    ) = viewModelScope.launch {
        _updateProfileResponse.value = Resource.Loading
        _updateProfileResponse.value = repository.updateProfile(requestBody)
    }





}