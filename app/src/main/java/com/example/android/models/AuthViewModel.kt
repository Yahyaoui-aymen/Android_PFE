package com.example.android.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.data.network.Resource
import com.example.android.data.repository.AuthRepository
import com.example.android.data.requests.LoginRequest
import com.example.android.data.requests.RecoverpwdRequest
import com.example.android.data.requests.SignupRequest
import com.example.android.data.responses.*
import com.example.android.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : BaseViewModel(repository) {

    private val _loginResponse: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<LoginResponse>>
        get() = _loginResponse

    private val _presignupResponse: MutableLiveData<Resource<PresignupResponse>> = MutableLiveData()
    val presignupResponse: LiveData<Resource<PresignupResponse>>
        get() = _presignupResponse

    private val _signupResponse: MutableLiveData<Resource<SignupResponse>> = MutableLiveData()
    val signupResponse: LiveData<Resource<SignupResponse>>
        get() = _signupResponse

    private val _recoverPwdResponse: MutableLiveData<Resource<RecoverPwdResponse>> = MutableLiveData()
    val recoverPwdResponse: LiveData<Resource<RecoverPwdResponse>>
        get() = _recoverPwdResponse

    private val _resendOtpResponse: MutableLiveData<Resource<ResendOtpResponse>> = MutableLiveData()
    val resendOtpResponse: LiveData<Resource<ResendOtpResponse>>
        get() = _resendOtpResponse


    fun loginProvider(
        request: LoginRequest
    ) = viewModelScope.launch {
        _loginResponse.value = repository.loginProvider(request)
    }

    fun presignupProvider(
        request: SignupRequest
    ) = viewModelScope.launch {
        _presignupResponse.value = repository.presignupProvider(request)
    }

    fun signupProvider(
        request: SignupRequest
    ) = viewModelScope.launch {
        _signupResponse.value = repository.signupProvider(request)
    }

    fun loginClient(
        request: LoginRequest
    ) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = repository.loginClient(request)
    }

    fun presignupClient(
        request: SignupRequest
    ) = viewModelScope.launch {
        _presignupResponse.value = repository.presignupClient(request)
    }

    fun signupClient(
        request: SignupRequest
    ) = viewModelScope.launch {
        _signupResponse.value = repository.signupClient(request)
    }

    fun recoverPwd(
        request: RecoverpwdRequest
    ) = viewModelScope.launch {
        _recoverPwdResponse.value = repository.recoverPwd(request)
    }

    suspend fun saveAuthToken(token: String, id: Long, role: String) {
        repository.saveAuthToken(token, id, role)
    }

    suspend fun updatePwd(id: Long, password: String) {
        _recoverPwdResponse.value = repository.updatePwd(id, password)
    }


    fun resentOTP(
        phoneNumber: String
    ) {
        viewModelScope.launch {
            _resendOtpResponse.value = repository.resendOtp(phoneNumber)
        }

    }

}