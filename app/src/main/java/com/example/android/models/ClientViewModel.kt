package com.example.android.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.data.SingleLiveEvent
import com.example.android.data.network.Resource
import com.example.android.data.repository.ClientRepository
import com.example.android.data.responses.*
import com.example.android.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class ClientViewModel(
    private val repository: ClientRepository,
) : BaseViewModel(repository) {

    private val _client: MutableLiveData<Resource<SignupResponse>> = MutableLiveData()
    val client: LiveData<Resource<SignupResponse>>
        get() = _client


    private val _ads: MutableLiveData<Resource<GetAdsResponse>> = MutableLiveData()
    val ads: LiveData<Resource<GetAdsResponse>>
        get() = _ads

    private val _offers: MutableLiveData<Resource<GetOffersResponse>> = MutableLiveData()
    val offers: LiveData<Resource<GetOffersResponse>>
        get() = _offers



    private val _filterResponse: MutableLiveData<Resource<FilterResponse>> = MutableLiveData()
    val filterResponse: LiveData<Resource<FilterResponse>>
        get() = _filterResponse


    private val _updateProfileResponse: SingleLiveEvent<Resource<SignupResponse>> = SingleLiveEvent()
    val updateProfileResponse: LiveData<Resource<SignupResponse>>
        get() = _updateProfileResponse

    init {
        getAllAds()
        getProfile()
        getAllOffers()
    }

    fun getProfile() = viewModelScope.launch {
        _client.value = Resource.Loading
        _client.value = repository.getProfile()
    }

    fun getAllAds() = viewModelScope.launch {
        _ads.value = Resource.Loading
        _ads.value = repository.getAllAds()
    }

    fun getAllOffers() = viewModelScope.launch {
        _offers.value = Resource.Loading
        _offers.value = repository.getAllOffers()
    }



    fun getByCategoryGovernmentCity( categoryId : Int , government : String? , city :String?)
    = viewModelScope.launch {
        _filterResponse.value = Resource.Loading
        _filterResponse.value = repository.getByCategoryGovernmentCity(categoryId , government , city   )
    }

    fun updateProfile(
        requestBody: RequestBody
    ) = viewModelScope.launch {
        _updateProfileResponse.value = Resource.Loading
        _updateProfileResponse.value = repository.updateProfile(requestBody)
    }
}