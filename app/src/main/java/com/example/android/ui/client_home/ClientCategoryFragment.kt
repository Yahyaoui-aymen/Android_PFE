package com.example.android.ui.client_home


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.get
import com.example.android.R
import com.example.android.data.CategoryData
import com.example.android.data.DataSource
import com.example.android.data.DataSource.categories
import com.example.android.data.network.ClientApi
import com.example.android.data.repository.ClientRepository
import com.example.android.databinding.FragmentClientCategoryBinding
import com.example.android.models.ClientViewModel
import com.example.android.ui.adapters.AppointmentAdapter
import com.example.android.ui.adapters.CategoryGridAdapter
import com.example.android.ui.base.BaseFragment
import com.example.android.ui.category.RsltSearchCategoryActivity
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ClientCategoryFragment :
    BaseFragment<ClientViewModel, FragmentClientCategoryBinding, ClientRepository>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.clientCategoryFragment = this


        //category adapter
        var categoryAdapter = CategoryGridAdapter(CategoryData(requireContext()).categories, requireContext())
        binding.gridRecyclerView.adapter = categoryAdapter

        categoryAdapter.setOnClickListener(object : CategoryGridAdapter.onItemClickListener {
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


        val governmentAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.Government,
            R.layout.item_dropdown_list
        )
        governmentAdapter.setDropDownViewResource(R.layout.item_dropdown_list)
        binding!!.govermentTxt.setAdapter(governmentAdapter)


        binding.govermentTxt.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->


                val cityAdapter = ArrayAdapter.createFromResource(
                    requireContext(),
                    DataSource.city[parent.getItemAtPosition(position)]!!,
                    R.layout.item_dropdown_list
                )
                cityAdapter.setDropDownViewResource(R.layout.item_dropdown_list)
                binding!!.cityTxt.setAdapter(cityAdapter)

            }
    }

    override fun onResume() {
        super.onResume()
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


    override fun getViewModel() = ClientViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentClientCategoryBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): ClientRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(ClientApi::class.java, token)
        return ClientRepository(api)
    }


}