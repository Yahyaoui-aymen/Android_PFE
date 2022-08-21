package com.example.android.ui.client_home

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.android.R
import com.example.android.data.DataSource
import com.example.android.data.network.ClientApi
import com.example.android.data.network.Resource
import com.example.android.data.repository.ClientRepository
import com.example.android.databinding.FragmentClientBestPlanBinding
import com.example.android.models.ClientViewModel
import com.example.android.ui.adapters.BestPlanAdapter
import com.example.android.ui.base.BaseFragment
import com.example.android.ui.reservation.ReservationActivity
import kotlinx.coroutines.NonCancellable.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class ClientBestPlanFragment :
    BaseFragment<ClientViewModel, FragmentClientBestPlanBinding, ClientRepository>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.clientBestPlanFragment = this

        //observer on offers
        viewModel.offers.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    val offers = it.value.data.sortedByDescending { it.id }

                    //Offer adapter
                    var bestPlanAdapter = BestPlanAdapter(offers, requireContext())
                    binding.bestPlanRecyclerView.adapter = bestPlanAdapter

                    //on Click listener : Show alert dialog
                    bestPlanAdapter.setOnClickListener(object :
                        BestPlanAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            //get actual item
                            val offer = bestPlanAdapter.getItemByPosition(position)
                            //afficher l'offre dans une alerte:

                            // build alert dialog
                            val dialogBuilder = AlertDialog.Builder(requireContext())

                            val view: View = layoutInflater.inflate(
                                com.example.android.R.layout.alert_offer_details,
                                null
                            )

                            //offer Image
                            val offerImage =
                                view.findViewById<ImageView>(com.example.android.R.id.alert_offer_img)
                            val offerUrl = DataSource.PHOTO_URL + offer.image
                            Glide.with(this@ClientBestPlanFragment)
                                .load(offerUrl)
                                .into(offerImage)

                            //title
                            val offerTxt =
                                view.findViewById<TextView>(com.example.android.R.id.alert_offer_title)
                            offerTxt.text = offer.title

                            //providerName
                            val providerName =
                                view.findViewById<TextView>(com.example.android.R.id.alert_provider_name)
                            providerName.text = offer.user.firstName + " " + offer.user.lastName

                            //provider Image
                            val providerImage =
                                view.findViewById<ImageView>(com.example.android.R.id.alert_provider_img)
                            val url = DataSource.PHOTO_URL + offer.user.imageUrl
                            Glide.with(this@ClientBestPlanFragment)
                                .load(url)
                                .into(providerImage)

                            //offer description
                            val descriptionTxt =
                                view.findViewById<TextView>(com.example.android.R.id.alert_offer_description)
                            descriptionTxt.text = offer.description
                            descriptionTxt.movementMethod = ScrollingMovementMethod()

                            dialogBuilder.setView(view)

                            dialogBuilder.setPositiveButton(
                                getString(R.string.proceed),
                                DialogInterface.OnClickListener { dialog, id ->

                                    val intent = Intent(context, ReservationActivity::class.java)
                                    intent.putExtra("provider_id" , offers[position].user.id)
                                    intent.putExtra("first_name" , offers[position].user.firstName)
                                    intent.putExtra("last_name" , offers[position].user.lastName)
                                    intent.putExtra("government" , offers[position].user.government)
                                    intent.putExtra("city" , offers[position].user.city)
                                    intent.putExtra("phoneNumber" , offers[position].user.phoneNumber)
                                    intent.putExtra("imageUrl" , offers[position].user.imageUrl)
                                    intent.putExtra("category" , offers[position].user.category.name)
                                    intent.putExtra("isOffer", true)
                                    startActivity(intent)
                                })
                            // negative button text and action
                            dialogBuilder.setNegativeButton(
                                getString(R.string.cancel),
                                DialogInterface.OnClickListener { dialog, id ->
                                    dialog.cancel()
                                })
                            dialogBuilder.show()

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
    ) = FragmentClientBestPlanBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): ClientRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(ClientApi::class.java, token)
        return ClientRepository(api)
    }


}