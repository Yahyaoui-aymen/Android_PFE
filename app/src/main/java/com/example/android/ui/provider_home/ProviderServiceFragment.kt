package com.example.android.ui.provider_home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.android.R
import com.example.android.data.network.ProviderApi
import com.example.android.data.repository.ProviderRepository
import com.example.android.databinding.FragmentProviderServiceBinding
import com.example.android.models.ProviderViewModel
import com.example.android.ui.base.BaseFragment
import com.example.android.ui.reservation.ReservationActivity
import com.example.android.ui.showNegativeAlert
import com.example.android.ui.showPositiveAlert
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.get
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class ProviderServiceFragment :
    BaseFragment<ProviderViewModel, FragmentProviderServiceBinding, ProviderRepository>() {


    private val SELECT_IMAGE_CODE = 1

    private val REQUEST_IMAGE_GALLERY = 132

    private val REQUEST_IMAGE_CAMERA = 14

    private lateinit var photo: ImageView
    var file: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding?.providerServiceFragment = this



        photo = binding.displaypic

        photo.setOnClickListener {
            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
            //Image Uri will not be null for RESULT_OK
            val fileUri = data.data
            binding.displaypic.setImageURI(fileUri)
            binding.displaypic.setPadding(0,0,0,0)
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

    fun uploadOffer() {

        val title = binding.titleTxt.text.toString()
        val subtitle = binding.subtitleTxt.text.toString()
        val price = binding.priceTxt.text.toString()
        val description = binding.descriptionTxt.text.toString()
        if (title.isEmpty() || subtitle.isEmpty() || price.isEmpty() || description.isEmpty())
        {
            showNegativeAlert(requireActivity(), getString(R.string.fill_fields))
        }

        else {
            if (file?.exists() != true) {
                    showNegativeAlert(requireActivity() , getString(R.string.missing_photo))
            } else {
                val requestBody: RequestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "imageFile", file!!.name.toString(),
                        file!!.asRequestBody("file".toMediaTypeOrNull())
                    )
                    .addFormDataPart("title", title)
                    .addFormDataPart("subtitle", subtitle)
                    .addFormDataPart("description", description)
                    .addFormDataPart("price", price)
                    .build()
                runBlocking { viewModel.addOffer(requestBody) }
                showPositiveAlert(requireActivity(), getString(R.string.added_successfully))

                val intent = Intent(context, ProviderHomeActivity::class.java)
                startActivity(intent)
            }
        }


    }


    override fun getViewModel() = ProviderViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentProviderServiceBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): ProviderRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(ProviderApi::class.java, token)
        return ProviderRepository(api)
    }

}