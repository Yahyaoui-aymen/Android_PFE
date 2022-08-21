package com.example.android.ui.auth

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.android.databinding.FragmentRecoverPwdBinding
import com.example.android.models.AuthViewModel
import com.example.android.models.SharedAuthViewModel
import com.example.android.ui.base.BaseFragment
import com.example.android.ui.showPositiveAlert
import kotlinx.coroutines.launch


class RecoverPwdFragment :
    BaseFragment<AuthViewModel, FragmentRecoverPwdBinding, AuthRepository>() {

    private val sharedAuthViewModel: SharedAuthViewModel by activityViewModels()


    override fun onResume() {
        super.onResume()
        binding.pwUserEditTxt.text = null
        binding.error.visibility = TextView.INVISIBLE
        binding.password.text = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.recoverPwdFragment = this

        viewModel.recoverPwdResponse.observe(viewLifecycleOwner, Observer {
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
                            showPositiveAlert(requireActivity() , getString(R.string.pwd_changed))
                            findNavController().navigate(R.id.action_recoverPwdFragment_to_bonjourFragment)
                        }
                    }
                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "check ur entries", Toast.LENGTH_SHORT).show()
                }
            }
        })

        //confirm password
        binding.pwUserEditTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val strPass1: String = binding.password.text.toString()
                val strPass2: String = binding.pwUserEditTxt.text.toString()
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
        //end confirm password

    }

    fun updatePwd() {
        val pwd = binding.password.text.toString()
        val id = sharedAuthViewModel.recoverPwdResponse.value!!.id
        if (pwd == binding.pwUserEditTxt.text.toString()) {
            lifecycleScope.launch {
                viewModel.updatePwd(id, pwd)
            }

        }
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRecoverPwdBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        AuthRepository(remoteDataSource.buildApi(AuthApi::class.java), userPreferences)

}