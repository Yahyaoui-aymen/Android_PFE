package com.example.android.ui.client_home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.android.R
import com.example.android.data.CategoryData
import com.example.android.data.DataSource
import com.example.android.data.DataSource.categories
import com.example.android.data.network.ClientApi
import com.example.android.data.network.Resource
import com.example.android.data.repository.ClientRepository
import com.example.android.data.responses.Data
import com.example.android.databinding.FragmentClientHomeBinding
import com.example.android.models.ClientViewModel
import com.example.android.ui.adapters.*
import com.example.android.ui.base.BaseFragment
import com.example.android.ui.category.RsltSearchCategoryActivity
import com.example.android.ui.notification.NotificationActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class ClientHomeFragment : BaseFragment<ClientViewModel, FragmentClientHomeBinding, ClientRepository>() {

    override fun onResume() {
        super.onResume()
        viewModel.getAllOffers()
        viewModel.getProfile()

        val governmentAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.Government,
            R.layout.item_dropdown_list
        )
        governmentAdapter.setDropDownViewResource(R.layout.item_dropdown_list)
        binding!!.govermentTxt.setAdapter(governmentAdapter)
        binding.govermentTxt.text = null

        val cityAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.cities,
            R.layout.item_dropdown_list
        )
        cityAdapter.setDropDownViewResource(R.layout.item_dropdown_list)
        binding!!.cityTxt.setAdapter(cityAdapter)
        binding.cityTxt.text = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding?.clientHomeFragment = this

        val recyclerView = binding.adsRecyclerView


        viewModel.ads.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success -> {
                    //ads adapter
                    var layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    var adapter = AdsHorizontalAdapter(it.value.data, requireContext())
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = layoutManager

                }
            }
        })

        viewModel.client.observe( viewLifecycleOwner , Observer {
            when(it){
                is Resource.Success -> {
                    val user = it.value.data
                    getUsername(it.value.data!!)
                    if (it.value.data.imageUrl != null) {
                        val url = DataSource.PHOTO_URL + user!!.imageUrl
                        Glide.with(this)
                            .load(url)
                            .into(binding.img)
                    }
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

        //observer on offers
        viewModel.offers.observe( viewLifecycleOwner , Observer {
            when(it){
                is Resource.Success -> {
                    val offers = it.value.data.sortedByDescending { it.id }
                    //Offer adapter
                    var offerAdapter = OfferAdapter(offers, requireContext(), 3)
                    binding.offerVerticalRecyclerView.adapter = offerAdapter
                }
            }
        })

        //category adapter
        var categoryLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        var categoryAdapter = CategoryButtonAdapter(CategoryData(requireContext()).categories, requireContext())
        binding.categoryButtonRecyclerView.adapter = categoryAdapter
        binding.categoryButtonRecyclerView.layoutManager = categoryLayoutManager


        categoryAdapter.setOnClickListener(object : CategoryButtonAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {

                val government = binding.govermentTxt.text.toString()
                val city = binding.cityTxt.text.toString()
                val categoryId = categoryAdapter.getItemIdByPosition(position)

                val intent = Intent(context, RsltSearchCategoryActivity::class.java)

                intent.putExtra("government", government)
                intent.putExtra("city", city)
                intent.putExtra("category_id", categoryId)
                startActivity(intent)

            }
        })


        //Category horizontal adapter
      /*  var categoryHorizontalLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        var categoryHorizontalAdapter = CategoryHorizontalAdapter(CategoryData(requireContext()).categories, requireContext())
        binding.categoryHorizontalRecyclerView.adapter = categoryHorizontalAdapter
        binding.categoryHorizontalRecyclerView.layoutManager= categoryHorizontalLayoutManager*/


        //DropDown button for government
        val governmentAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.Government,
            R.layout.item_dropdown_list
        )

        governmentAdapter.setDropDownViewResource(R.layout.item_dropdown_list)
        binding!!.govermentTxt.setAdapter(governmentAdapter)

        binding.govermentTxt.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                //DropDown button for City
                val cityAdapter = ArrayAdapter.createFromResource(
                    requireContext(),
                    DataSource.city[parent.getItemAtPosition(position)]!!,
                    R.layout.item_dropdown_list
                )
                cityAdapter.setDropDownViewResource(R.layout.item_dropdown_list)
                binding!!.cityTxt.setAdapter(cityAdapter)
                //end DropDown button for City
            }
        //end DropDown botton for government

    }

    private fun getUsername(client: Data) {
        binding.user.text= client.username
    }

    fun goToProfile(){
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_view).selectedItemId  = R.id.profile
        loadFragment(ClientProfileFragment())
    }

    fun goToNotification(){
        val intent = Intent(context, NotificationActivity::class.java)
        startActivity(intent)
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.client_nav_host_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    override fun getViewModel() = ClientViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentClientHomeBinding.inflate(inflater, container, false)
    override fun getFragmentRepository(): ClientRepository {
        val token =  runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(ClientApi::class.java, token)
        return ClientRepository(api)
    }

}