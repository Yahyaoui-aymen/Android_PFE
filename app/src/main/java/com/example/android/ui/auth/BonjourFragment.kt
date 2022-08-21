package com.example.android.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.android.R
import com.example.android.data.network.AuthApi
import com.example.android.data.repository.AuthRepository
import com.example.android.databinding.FragmentBonjourBinding
import com.example.android.models.AuthViewModel
import com.example.android.models.SharedAuthViewModel
import com.example.android.ui.base.BaseFragment


class BonjourFragment : BaseFragment<AuthViewModel, FragmentBonjourBinding, AuthRepository>() {

    private val sharedAuthViewModel: SharedAuthViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.bonjourFragment = this
    }


    override fun getViewModel() = AuthViewModel::class.java
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentBonjourBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        AuthRepository(remoteDataSource.buildApi(AuthApi::class.java), userPreferences)


    fun goSignUp(user: String) {
        when (user) {
            "provider" -> {
                sharedAuthViewModel.setRole("provider")
                findNavController().navigate(R.id.action_bonjourFragment_to_signupProviderFragment)
            }
            "client" -> {
                sharedAuthViewModel.setRole("client")
                findNavController().navigate(R.id.action_bonjourFragment_to_signupClientFragment)
            }
        }
    }
}

