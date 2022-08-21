package com.example.android.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.data.network.Resource
import com.example.android.data.repository.AuthRepository
import com.example.android.data.requests.RecoverpwdRequest
import com.example.android.data.requests.SignupRequest
import com.example.android.data.responses.PresignupResponse
import com.example.android.data.responses.RecoverPwdResponse
import com.example.android.data.responses.ResendOtpResponse
import com.example.android.data.responses.User
import kotlinx.coroutines.launch

class SharedAuthViewModel : ViewModel() {


    private val _role = MutableLiveData<String>()
    val role: LiveData<String>
        get() =_role

    private val _source = MutableLiveData<String>()
    val source: LiveData<String>
        get() = _source

    private val _userSignupRequest = MutableLiveData<SignupRequest>()
    val userSignupRequest: LiveData<SignupRequest>
        get() =  _userSignupRequest


    private val _recoverPwdResponse : MutableLiveData<User> = MutableLiveData()
    val recoverPwdResponse: LiveData<User>
        get() = _recoverPwdResponse

    fun setRole(type: String) {
        _role.value = type
    }

    fun setSource(srcPage : String) {
        _source.value = srcPage
    }


    fun setUserSignupRequest(request: SignupRequest){
        _userSignupRequest.value = request
    }

    fun setOtpCodeUserSignupRequest(otpCode: String){
        _userSignupRequest.value!!.otpCode = otpCode
    }

    fun setOtpCodeRecoverPwdResponse(otpCode: String){
        _recoverPwdResponse.value!!.otpCode = otpCode
    }



    fun setRecoverPwdResponse(request: User){
        _recoverPwdResponse.value = request
    }


}