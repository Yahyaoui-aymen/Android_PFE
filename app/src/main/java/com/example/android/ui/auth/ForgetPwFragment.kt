package com.example.android.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android.R
import com.example.android.data.repository.AuthRepository
import com.example.android.databinding.FragmentForgetPwBinding
import com.example.android.models.AuthViewModel
import com.example.android.data.network.AuthApi
import com.example.android.data.network.Resource
import com.example.android.data.requests.RecoverpwdRequest
import com.example.android.models.SharedAuthViewModel
import com.example.android.ui.base.BaseFragment
import com.example.android.ui.handleApiError
import com.example.android.ui.showNegativeAlert
import kotlinx.coroutines.launch


class ForgetPwFragment : BaseFragment<AuthViewModel, FragmentForgetPwBinding, AuthRepository>() {

    private val sharedAuthViewModel: SharedAuthViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.forgetPwFragment = this

        viewModel.recoverPwdResponse.observe(viewLifecycleOwner, Observer{
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        if (it.value.error != null )
                        {
                            showNegativeAlert(requireActivity(),it.value.error.toString())
                        }
                        else {
                            sharedAuthViewModel.setRecoverPwdResponse(it.value.data!!)
                            sharedAuthViewModel.setSource("forgetPwd")
                            findNavController().navigate(R.id.action_forgetPwFragment_to_otpFragment)
                        }
                    }
                }
                is Resource.Failure -> handleApiError(it)
            }
        })
    }

    fun checkAccount() {
        val request = RecoverpwdRequest(binding.pseudoUserEditTxt.text.toString(),
            binding.phoneEditTxt.text.toString())
        viewModel.recoverPwd(request)
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )  = FragmentForgetPwBinding.inflate(inflater , container ,false)

    override fun getFragmentRepository()= AuthRepository(remoteDataSource.buildApi(AuthApi::class.java),userPreferences)

}
