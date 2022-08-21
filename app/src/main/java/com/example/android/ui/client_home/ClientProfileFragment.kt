package com.example.android.ui.client_home

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.android.MainActivity
import com.example.android.R
import com.example.android.data.DataSource
import com.example.android.data.network.ClientApi
import com.example.android.data.network.Resource
import com.example.android.data.repository.ClientRepository
import com.example.android.data.responses.Data
import com.example.android.databinding.FragmentClientProfileBinding
import com.example.android.models.ClientViewModel
import com.example.android.ui.base.BaseFragment
import com.example.android.ui.errorHandler
import com.example.android.ui.showNegativeAlert
import com.example.android.ui.showPositiveAlert
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.*

class ClientProfileFragment :
    BaseFragment<ClientViewModel, FragmentClientProfileBinding, ClientRepository>() {

    var file: File? = null
    var logoutRequiredAfterUpdate = false
    var user : Data? = null

    override fun onResume() {
        super.onResume()
        binding.confirmpasswordTxt.text = null
        binding.error.visibility = TextView.INVISIBLE
        binding.passwordTxt.text = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.clientProfileFragment = this
        binding.profileImg.isEnabled = false


        viewModel.client.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    user = it.value.data
                    binding.lastName.text = user!!.lastName
                    binding.firstName.text = user!!.firstName
                    binding.pseudoTxt.setText(user!!.username)
                    if (it.value.data?.imageUrl != null) {
                        val url = DataSource.PHOTO_URL + user!!.imageUrl
                        Glide.with(this)
                            .load(url)
                            .into(binding.profileImg)
                    }
                }
            }
        })

        viewModel.updateProfileResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    if (it.value.error == null) {
                       showPositiveAlert(requireActivity(), getString(R.string.update_success))
                        if (logoutRequiredAfterUpdate) {logout()}
                        else {
                            disableModifying()
                            viewModel.getProfile()
                        }
                    } else {
                        if (errorHandler.containsKey(it.value.error.toString())) {
                            showNegativeAlert(
                                requireActivity(),
                                getString(errorHandler[it.value.error.toString()]!!)
                            )
                        }
                    }
                }
                is Resource.Failure -> {

                }

            }
        })


        //confirm password
        binding.confirmpasswordTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val strPass1: String = binding.passwordTxt.text.toString()
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
        //end confirm password


        //pick image
        binding.profileImg.setOnClickListener {
            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        // set up language
        val prefs = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        language = prefs.getString("MY_LANG", "")

        if (language == "ar") {
            binding.arabicRadioBtn.isChecked = true
        } else {
            binding.frenchRadioBtn.isChecked = true
        }

        binding.languageRadioGrp.setOnCheckedChangeListener { radioGroup, i ->
            val prefs = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE)
            language = prefs.getString("MY_LANG", "")

            if (language == "ar" && binding.frenchRadioBtn.isChecked) {
                showAlert()
            } else if(language == "fr" && binding.arabicRadioBtn.isChecked) {
                showAlert()
            }
        }


        //end set up language

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
            //Image Uri will not be null for RESULT_OK
            val fileUri = data.data
            binding.profileImg.setImageURI(fileUri)
            binding.profileImg.setPadding(0, 0, 0, 0)
            //You can get File object from intent
            file = ImagePicker.getFile(data)
            //You can also get File Path from intent
            val filePath: String? = ImagePicker.getFilePath(data!!)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            showPositiveAlert(requireActivity() , getString(R.string.task_cancelled))
        }
    }


    fun enableModifying() {
        binding.editIcon.isVisible = false
        binding.confirmpassword.isVisible = true
        binding.client3PwEye.isVisible = true
        binding.confirmButton.visibility = View.VISIBLE
        binding.pseudoTxt.isEnabled = true
        binding.passwordTxt.isEnabled = true
        binding.profileImg.isEnabled = true
    }

    fun disableModifying() {
        binding.editIcon.isVisible = true
        binding.confirmpassword.isVisible = false
        binding.client3PwEye.isVisible = false
        binding.confirmButton.visibility = View.GONE
        binding.pseudoTxt.isEnabled = false
        binding.passwordTxt.isEnabled = false
        binding.profileImg.isEnabled = false
    }


    fun updateProfile() {

        val pseudo = binding.pseudoTxt.text.toString()
        val password = binding.passwordTxt.text.toString()
        val confirmPwd: String = binding.confirmpasswordTxt.text.toString()
        logoutRequiredAfterUpdate = password.isNotEmpty() || (pseudo.isNotEmpty() && !pseudo.equals(user?.username))
        val requestBody: RequestBody
        if (file != null) {
            requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    "imageFile", file!!.name.toString(),
                    file!!.asRequestBody("file".toMediaTypeOrNull())
                )
                .addFormDataPart("pseudo", pseudo)
                .addFormDataPart("password", password)
                .build()
        } else {
            requestBody =
                MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("pseudo", pseudo)
                    .addFormDataPart("password", password)
                    .build()
        }
        if (password.equals(confirmPwd)) {
            runBlocking { viewModel.updateProfile(requestBody) }
            binding.passwordTxt.text = null
            binding.confirmpasswordTxt.text = null
            binding.error.visibility = View.GONE
        }
    }

    override fun getViewModel() = ClientViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentClientProfileBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): ClientRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(ClientApi::class.java, token)
        return ClientRepository(api)
    }


    // language settings
    private var language: String? = null
    private var numAttempt = 0
    private val mHandler = Handler()
    private val restartApp: Runnable = object : Runnable {
        override fun run() {
            startActivity(activity!!.intent)
            val intent = Intent(activity, MainActivity::class.java)
            activity!!.startActivity(intent)
            activity!!.finishAffinity()
        }
    }

    fun showAlert() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Alliance").setMessage(getString(R.string.refresh_language))

        builder.setCancelable(true)
        builder.setPositiveButton(
            requireActivity().resources.getString(R.string.oui),
            DialogInterface.OnClickListener { dialog, id ->
                switchLang(language!!)
                dialog.dismiss()
            })
        builder.setNegativeButton(
            requireActivity().resources.getString(R.string.non),
            DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()
                val prefs = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE)
                language = prefs.getString("MY_LANG", "")

                if (language == "ar") {
                    binding.arabicRadioBtn.isChecked = true
                } else {
                    binding.frenchRadioBtn.isChecked = true
                }
            })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    // set language
    // save language to local sharedPreferences
    private fun setLocal(lang: String) {
        val editor = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("MY_LANG", lang)
        editor.apply()
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        requireActivity().baseContext.resources.updateConfiguration(config, requireActivity().baseContext.resources.displayMetrics)
    }

    // check actual lang and switch it
    private fun switchLang(language: String) {
        if (language == "fr" || language == "") {
            setLocal("ar")
        } else if (language == "ar") {
            setLocal("fr")
        }
        // TODO : Uncomment when you add alert
        /*Alerter.create(requireActivity())
            .setTitle(R.string.change_language)
            .setText(R.string.change_language_restart)
            .setBackgroundColorRes(R.color.alert)
            .show()*/
        numAttempt = 0
        mHandler.postDelayed(restartApp, 3000)
    }

}