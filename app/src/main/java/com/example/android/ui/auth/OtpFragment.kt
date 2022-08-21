package com.example.android.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android.R
import com.example.android.data.network.AuthApi
import com.example.android.data.network.Resource
import com.example.android.data.repository.AuthRepository
import com.example.android.databinding.FragmentOtpBinding
import com.example.android.models.AuthViewModel
import com.example.android.models.SharedAuthViewModel
import com.example.android.ui.base.BaseFragment
import com.example.android.ui.client_home.ClientHomeActivity
import com.example.android.ui.handleApiError
import com.example.android.ui.provider_home.ProviderHomeActivity
import com.example.android.ui.showPositiveAlert
import com.example.android.ui.startNewActivity
import com.example.android.ui.visible
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class OtpFragment : BaseFragment<AuthViewModel, FragmentOtpBinding, AuthRepository>() {

    private val sharedAuthViewModel: SharedAuthViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.otpFragment = this


        when (sharedAuthViewModel.source.value) {
            "SignUp" -> {
                binding.clientPhoneNumberTxt.text =
                    sharedAuthViewModel.userSignupRequest.value!!.phoneNumber
            }
            "forgetPwd" -> {
                binding.clientPhoneNumberTxt.text = sharedAuthViewModel.recoverPwdResponse.value!!.phoneNumber
            }
        }

        viewModel.resendOtpResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    when (sharedAuthViewModel.source.value) {
                        "SignUp" -> {
                            sharedAuthViewModel.setOtpCodeUserSignupRequest(it.value.data.toString())
                        }
                        "forgetPwd" -> {
                            sharedAuthViewModel.setOtpCodeRecoverPwdResponse(it.value.data.toString())

                        }
                    }
                }
                is Resource.Failure -> handleApiError(it)
            }
        })

        val otp1 = binding.num1
        val otp2 = binding.num2
        val otp3 = binding.num3
        val otp4 = binding.num4
        val otp5 = binding.num5
        val otp6 = binding.num6


        val otpTextViews = arrayOf<TextView>(otp1, otp2, otp3, otp4, otp5, otp6)

        for (currTextView in otpTextViews) {
            currTextView.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    nextTextView().requestFocus()
                }

                override fun afterTextChanged(s: Editable) {}
                fun nextTextView(): TextView {
                    var i: Int
                    i = 0
                    while (i < otpTextViews.size - 1) {
                        if (otpTextViews[i] === currTextView) return otpTextViews[i + 1]
                        i++
                    }
                    return otpTextViews[i]
                }
            })
        }

    }



    fun checkOTP() {
        val otp = "${binding.num1.text.toString()}" +
                "${binding.num2.text.toString()}" +
                "${binding.num3.text.toString()}" +
                "${binding.num4.text.toString()}" +
                "${binding.num5.text.toString()}" +
                "${binding.num6.text.toString()}"

        when (sharedAuthViewModel.source.value) {
            "SignUp" -> {
                if (otp == sharedAuthViewModel.userSignupRequest.value!!.otpCode) {
                    lifecycleScope.launch {
                        if(sharedAuthViewModel.role.value == "client") {
                            viewModel.signupClient(sharedAuthViewModel.userSignupRequest.value!!)
                        }
                        if(sharedAuthViewModel.role.value == "provider"){
                            viewModel.signupProvider(sharedAuthViewModel.userSignupRequest.value!!)
                        }
                       showPositiveAlert(requireActivity() , getString(R.string.registred_success))
                        findNavController().navigate(R.id.action_otpFragment_to_loginFragment)
                    }
                } else {
                    Toast.makeText(requireContext(), "Code is incorrect!!", Toast.LENGTH_LONG)
                        .show()
                }
            }
            "forgetPwd" -> {
                if (otp == sharedAuthViewModel.recoverPwdResponse.value!!.otpCode) {
                    Toast.makeText(requireContext(), "forgetPwd", Toast.LENGTH_SHORT)
                    findNavController().navigate(R.id.action_otpFragment_to_recoverPwdFragment)
                } else {
                    Toast.makeText(requireContext(), "Code is incorrect!!", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }


    }

    fun resendOTP() {
        viewModel.resentOTP(binding.clientPhoneNumberTxt.text.toString())
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOtpBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        AuthRepository(remoteDataSource.buildApi(AuthApi::class.java), userPreferences)

}


