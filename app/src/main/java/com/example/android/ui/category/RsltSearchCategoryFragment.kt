package com.example.android.ui.category

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.android.data.network.ClientApi
import com.example.android.data.network.Resource
import com.example.android.data.repository.ClientRepository
import com.example.android.databinding.FragmentRsltSearchCategoryBinding
import com.example.android.models.ClientViewModel
import com.example.android.ui.adapters.SearchCategoryAdapter
import com.example.android.ui.base.BaseFragment
import com.example.android.ui.client_home.ClientHomeActivity
import com.example.android.ui.reservation.ReservationActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class RsltSearchCategoryFragment :
    BaseFragment<ClientViewModel, FragmentRsltSearchCategoryBinding, ClientRepository>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rsltSearchCategoryFragment = this

        var government = arguments?.getString("government")
        var city = arguments?.getString("city")
        var categoryId = arguments?.getInt("category_id")!!


        viewModel.getByCategoryGovernmentCity(categoryId, government, city)

        viewModel.filterResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {

                    var searchCategoryAdapter =
                        SearchCategoryAdapter(it.value.data!!, requireContext())
                    binding.rsltSearchCategoryRecyclerView.adapter = searchCategoryAdapter

                    searchCategoryAdapter.setOnClickListener(object :
                        SearchCategoryAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {

                            val intent = Intent(context, ReservationActivity::class.java)
                            intent.putExtra("provider_id" , it.value.data[position].id)
                            intent.putExtra("first_name" , it.value.data[position].firstName)
                            intent.putExtra("last_name" , it.value.data[position].lastName)
                            intent.putExtra("government" , it.value.data[position].government)
                            intent.putExtra("city" , it.value.data[position].city)
                            intent.putExtra("phoneNumber" , it.value.data[position].phoneNumber)
                            intent.putExtra("imageUrl" , it.value.data[position].imageUrl)
                            intent.putExtra("category" , it.value.data[position].category.name)
                            intent.putExtra("isOffer", false)
                            startActivity(intent)
                        }

                    })

                }
            }
        })
    }


    override fun getViewModel() = ClientViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRsltSearchCategoryBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): ClientRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(ClientApi::class.java, token)
        return ClientRepository(api)
    }

    fun goBack() {
        val intent = Intent(context, ClientHomeActivity::class.java)
        startActivity(intent)
    }


}