package com.example.android.ui.auth

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android.R
import com.example.android.data.network.AuthApi
import com.example.android.data.network.Resource
import com.example.android.data.repository.AuthRepository
import com.example.android.data.requests.SignupRequest
import com.example.android.databinding.FragmentSignupClientBinding
import com.example.android.models.AuthViewModel
import com.example.android.models.SharedAuthViewModel
import com.example.android.ui.base.BaseFragment
import kotlinx.coroutines.launch

class SignupClientFragment :
    BaseFragment<AuthViewModel, FragmentSignupClientBinding, AuthRepository>() {

    private val sharedAuthViewModel: SharedAuthViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.signupClientFragment = this


        viewModel.presignupResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        if (it.value.error != null) {
                            Toast.makeText(
                                requireContext(),
                                it.value.error.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                        } else {
                            sharedAuthViewModel.setUserSignupRequest(it.value.data!!)
                            sharedAuthViewModel.setSource("SignUp")
                            findNavController().navigate(R.id.action_signupClientFragment_to_otpFragment)
                        }
                    }
                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "Signup Failure", Toast.LENGTH_SHORT).show()
                }
            }
        })

        //confirm password
        binding.confirmpasswordTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val strPass1: String = binding.password.text.toString()
                val strPass2: String = binding.confirmpasswordTxt.text.toString()
                if (strPass1 == strPass2 && strPass2 != null) {
                    binding.error.text = getString(R.string.password_match)
                    binding.error.setTextColor(Color.parseColor("#00FF00"))
                    binding.error.visibility = View.VISIBLE
                } else {
                    binding.error.text = getString(R.string.password_dont_match)
                    binding.error.setTextColor(Color.parseColor("#FF0000"))
                    binding.error.visibility = View.VISIBLE

                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

    }


    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSignupClientBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        AuthRepository(remoteDataSource.buildApi(AuthApi::class.java), userPreferences)

    fun goToVerificationScreen() {
        val firstname = binding.prenomClientEditTxt.text.toString().trim()
        val lastName = binding.nameClientEditTxt.text.toString().trim()
        val phoneNumber = binding.phoneClientEditTxt.text.toString().trim()
        val email = binding.emailClientEditTxt.text.toString().trim()
        val pseudo = binding.pseudoClientEditTxt.text.toString().trim()
        val password = binding.password.text.toString()
        val confirmPassword = binding.confirmpasswordTxt.text.toString()
        if (password.equals(confirmPassword)) {
            val request = SignupRequest(
                pseudo,
                email,
                null,
                null,
                password,
                firstname,
                lastName,
                null,
                null,
                phoneNumber,
                null,
                null
            )
            viewModel.presignupClient(request)
        } else {
            Toast.makeText(requireContext(),"mots de passe ne sont pas similaires", Toast.LENGTH_SHORT).show()
        }
    }

    fun goToLoginScreen() {
        findNavController().navigate(R.id.action_signupClientFragment_to_loginFragment)
    }


}