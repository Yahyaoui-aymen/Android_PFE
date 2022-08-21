package com.example.android.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.data.network.Resource
import com.example.android.data.repository.ReservationRepository
import com.example.android.data.requests.AppointmentRequest
import com.example.android.data.requests.NotificationRequest
import com.example.android.data.responses.GetListAppointmentsResponse
import com.example.android.data.responses.PhoneToken
import com.example.android.data.responses.TimeSlotResponse
import com.example.android.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class ReservationViewModel ( private val repository: ReservationRepository
) : BaseViewModel(repository) {

    private val _confirmedAppointments: MutableLiveData<Resource<GetListAppointmentsResponse>> = MutableLiveData()
    val confirmedAppointments: LiveData<Resource<GetListAppointmentsResponse>>
        get() = _confirmedAppointments


    private val _timeSlots: MutableLiveData<Resource<TimeSlotResponse>> = MutableLiveData()
    val timeSlots: LiveData<Resource<TimeSlotResponse>>
        get() = _timeSlots

    fun getConfirmedAppointment(
        providerId: Long,
    ) = viewModelScope.launch {
        _confirmedAppointments.value = repository.getConfirmedAppointments(providerId)
    }

    fun getTimeSlots(
        providerId: Long,
    ) = viewModelScope.launch {
        _timeSlots.value = repository.getTimeSlots(providerId)
    }


    fun addAppointment (
        phoneTokens : List<String>?,
        title : String ,
        message : String,
        apointmentRequest: AppointmentRequest,
        saveNotificationRequest: NotificationRequest
    ) = viewModelScope.launch {
        repository.addAppointment(apointmentRequest)
        repository.sendNotification(phoneTokens , title , message)
        repository.saveNotification(saveNotificationRequest)
    }

}