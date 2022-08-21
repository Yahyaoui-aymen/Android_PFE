package com.example.android.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android.R
import com.example.android.data.network.AuthApi
import com.example.android.data.network.Resource
import com.example.android.data.repository.AuthRepository
import com.example.android.data.requests.LoginRequest
import com.example.android.databinding.FragmentLoginBinding
import com.example.android.models.AuthViewModel
import com.example.android.models.SharedAuthViewModel
import com.example.android.ui.*
import com.example.android.ui.base.BaseFragment
import com.example.android.ui.client_home.ClientHomeActivity
import com.example.android.ui.provider_home.ProviderHomeActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment<AuthViewModel, FragmentLoginBinding, AuthRepository>() {
    lateinit var token : String

    private val sharedAuthViewModel: SharedAuthViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            token = task.result
        })

        binding?.loginFragment = this

        binding.progressbar.visible(false)
        binding.connexionUserButton.enable(false)

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            binding.progressbar.visible(it is Resource.Loading)
            when (it) {
                is Resource.Success -> {
                    if (it.value.error == null) {
                        lifecycleScope.launch {
                            viewModel.saveAuthToken(
                                it.value.data!!.accessToken,
                                it.value.data!!.id,
                                it.value.data!!.roles.toString()
                            )
                            if (sharedAuthViewModel.role.value == "client") {
                                requireActivity().startNewActivity(ClientHomeActivity::class.java)
                            }
                            if (sharedAuthViewModel.role.value == "provider") {
                                requireActivity().startNewActivity(ProviderHomeActivity::class.java)
                            }
                        }
                    } else {

                        showNegativeAlert(requireActivity(), getString(errorHandler[it.value.error.toString()]!!))

                    }
                }
                is Resource.Failure -> handleApiError(it)
            }
        })

        binding.password.addTextChangedListener {
            val email = binding.pseudoUserEditTxt.text.toString().trim()
            binding.connexionUserButton.enable(email.isNotEmpty() && it.toString().isNotEmpty())
        }
    }


    override fun getViewModel() = AuthViewModel::class.java
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        AuthRepository(remoteDataSource.buildApi(AuthApi::class.java), userPreferences)

    fun goToBonjourScreen() {
        findNavController().navigate(R.id.action_loginFragment_to_bonjourFragment)
    }

    fun login() {
        val pseudo = binding.pseudoUserEditTxt.text.toString().trim()
        val passwrd = binding.password.text.toString().trim()
        val request = LoginRequest(pseudo, passwrd,token)
        binding.progressbar.visible(true)
        when (sharedAuthViewModel.role.value) {
            "provider" -> {
                viewModel.loginProvider(request)
            }
            "client" -> {
                viewModel.loginClient(request)
            }
        }
    }


    fun goToForgetPw() {
        findNavController().navigate(R.id.action_loginFragment_to_forgetPwFragment)
    }

}