package com.example.android.ui.provider_home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.android.R
import com.example.android.data.DataSource
import com.example.android.data.network.ProviderApi
import com.example.android.data.network.Resource
import com.example.android.data.repository.ProviderRepository
import com.example.android.data.responses.Data
import com.example.android.databinding.FragmentProviderHomeBinding
import com.example.android.models.ProviderViewModel
import com.example.android.ui.adapters.AdsHorizontalAdapter
import com.example.android.ui.adapters.OfferAdapter
import com.example.android.ui.base.BaseFragment
import com.example.android.ui.client_home.ClientProfileFragment
import com.example.android.ui.notification.NotificationActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class ProviderHomeFragment :
    BaseFragment<ProviderViewModel, FragmentProviderHomeBinding, ProviderRepository>() {


    var id: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()

        viewModel.getProfile()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.providerHomeFragment = this

        //ads Adapter
        val adsRecyclerView = binding.adsRecyclerView
        viewModel.ads.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    var layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    var adsAdapter = AdsHorizontalAdapter(it.value.data, requireContext())
                    adsRecyclerView.adapter = adsAdapter
                    adsRecyclerView.layoutManager = layoutManager
                }
            }
        })

        //getting the user name
        viewModel.provider.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    val user = it.value.data
                    getUsername(it.value.data!!)
                    if (it.value.data.imageUrl != null) {
                        val url = DataSource.PHOTO_URL + user!!.imageUrl
                        Glide.with(this)
                            .load(url)
                            .into(binding.img)
                    }
                    id = it.value.data.id
                    viewModel.getOfferByUserId(id!!)
                }
                is Resource.Failure -> {
                    val alertDialogBuilder = AlertDialog.Builder(requireContext())
                        .setMessage("session ended please reconnect")
                        .setNegativeButton("close app") { dialog, which ->
                            // Respond to negative button press
                            System.exit(-1)
                        }
                        .setPositiveButton("reconnect") { dialog, which ->
                            // Respond to positive button press
                            logout()
                        }
                        .show()
                }
            }
        })

        //offer Adapter

        viewModel.getOffersResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    if (it.value.data != null) {
                        var offerAdapter = OfferAdapter(it.value.data, requireContext(), null)
                        binding.offerRecyclerView.adapter = offerAdapter
                    }
                }
            }
        })

    }


    private fun getUsername(provider: Data) {
        binding.user.text = provider.username
    }

    fun goToProfile() {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_view).selectedItemId = R.id.profile
        loadFragment(ProviderProfileFragment())
    }

    fun goToNotification(){
        val intent = Intent(context, NotificationActivity::class.java)
        startActivity(intent)
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.provider_nav_host_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun getViewModel() = ProviderViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentProviderHomeBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): ProviderRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(ProviderApi::class.java, token)
        return ProviderRepository(api)
    }

}