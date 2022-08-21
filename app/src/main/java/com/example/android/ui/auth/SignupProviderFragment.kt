package com.example.android.ui.auth

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android.R
import com.example.android.data.CategoryData
import com.example.android.data.DataSource
import com.example.android.data.network.AuthApi
import com.example.android.data.network.Resource
import com.example.android.data.repository.AuthRepository
import com.example.android.data.requests.SignupRequest
import com.example.android.databinding.FragmentSignupProviderBinding
import com.example.android.models.AuthViewModel
import com.example.android.models.SharedAuthViewModel
import com.example.android.ui.base.BaseFragment
import com.example.android.ui.showNegativeAlert
import com.example.android.ui.showPositiveAlert
import com.tapadoo.alerter.Alerter
import kotlinx.coroutines.launch


class SignupProviderFragment :
    BaseFragment<AuthViewModel, FragmentSignupProviderBinding, AuthRepository>() {

    private val sharedAuthViewModel: SharedAuthViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        //government Adapter
        val governmentAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.Government,
            R.layout.item_dropdown_list
        )
        governmentAdapter.setDropDownViewResource(R.layout.item_dropdown_list)
        binding!!.providerGovernmentTxt.setAdapter(governmentAdapter)
        binding.providerGovernmentTxt.text = null


        //cityAdapter
        val cityAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.cities,
            R.layout.item_dropdown_list
        )
        cityAdapter.setDropDownViewResource(R.layout.item_dropdown_list)
        binding!!.providerCityTxt.setAdapter(cityAdapter)
        binding.providerCityTxt.text = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.signupProviderFragment = this


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
                            findNavController().navigate(R.id.action_signupProviderFragment_to_otpFragment)
                        }
                    }
                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "Signup Failure", Toast.LENGTH_SHORT).show()
                }
            }
        })

        //creating dropdown menu for government
        val governmentAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.Government,
            R.layout.item_dropdown_list
        )


        governmentAdapter.setDropDownViewResource(R.layout.item_dropdown_list)
        binding!!.providerGovernmentTxt.setAdapter(governmentAdapter)

        binding.providerGovernmentTxt.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                //DropDown button for City
                val cityAdapter = ArrayAdapter.createFromResource(
                    requireContext(),
                    DataSource.city[parent.getItemAtPosition(position)]!!,
                    R.layout.item_dropdown_list
                )
                cityAdapter.setDropDownViewResource(R.layout.item_dropdown_list)
                binding!!.providerCityTxt.setAdapter(cityAdapter)
                //end DropDown button for City


            }
        //creating dropdown menu for category
        val categoryAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.Categories,
            R.layout.item_dropdown_list
        )

        categoryAdapter.setDropDownViewResource(R.layout.item_dropdown_list)
        binding!!.providerCategoryTxt.setAdapter(categoryAdapter)
        //end DropDown button for category


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

    fun goToVerificationScreen() {
        val firstname = binding.providerFirstNameEditText.text.toString().trim()
        val lastName = binding.providerNameEditText.text.toString().trim()
        val phoneNumber = binding.providerPhoneNumberEditText.text.toString().trim()

        val government =
            DataSource.governmentConverter[binding.providerGovernmentTxt.text.toString().trim()]

        val city = DataSource.cityConverter[binding.providerCityTxt.text.toString().trim()]

        val email = binding.providerEmailEditText.text.toString().trim()
        val pseudo = binding.providerPseudoEditText.text.toString().trim()
        val password = binding.password.text.toString()
        val confirmPassword = binding.confirmpasswordTxt.text.toString()
        if (password.equals(confirmPassword)) {
            val category =
                CategoryData(requireContext()).categoryConverter[binding.providerCategoryTxt.text.toString()
                    .trim()]
            val request = SignupRequest(
                pseudo,
                email,
                null,
                category,
                password,
                firstname,
                lastName,
                government,
                city,
                phoneNumber,
                null,
                null
            )
            viewModel.presignupProvider(request)
        } else {
            showNegativeAlert(requireActivity(), getString(R.string.password_dont_match))
        }
    }

    fun goToLoginScreen() {
        findNavController().navigate(R.id.action_signupProviderFragment_to_loginFragment)
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSignupProviderBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        AuthRepository(remoteDataSource.buildApi(AuthApi::class.java), userPreferences)

}

