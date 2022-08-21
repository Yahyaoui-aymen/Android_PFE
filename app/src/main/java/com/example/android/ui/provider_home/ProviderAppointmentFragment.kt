package com.example.android.ui.provider_home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import com.example.android.R
import com.example.android.data.DataSource
import com.example.android.data.network.ProviderApi
import com.example.android.data.network.Resource
import com.example.android.data.repository.ProviderRepository
import com.example.android.data.requests.NotificationRequest
import com.example.android.data.responses.Appointment
import com.example.android.databinding.FragmentProviderAppointmentBinding
import com.example.android.models.ProviderViewModel
import com.example.android.ui.adapters.AppointmentAdapter
import com.example.android.ui.base.BaseFragment
import com.example.android.ui.showNegativeAlert
import com.example.android.ui.showPositiveAlert
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

//implement ItemClickListener to enable click on child item
class ProviderAppointmentFragment :
    AppointmentAdapter.ItemClickListener,
    BaseFragment<ProviderViewModel, FragmentProviderAppointmentBinding, ProviderRepository>() {

    var status = 0
    var appointments: List<Appointment>? = listOf()

    override fun onResume() {
        super.onResume()
        status = 0
        viewModel.getAppointmentByProvider()
        //creating dropdown menu for FILTER
        binding.filterTxt.setText(getString(R.string.tout))
        val filterAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.AppointmentFilter,
            R.layout.item_dropdown_list
        )

        filterAdapter.setDropDownViewResource(R.layout.item_dropdown_list)
        binding.filterTxt.setAdapter(filterAdapter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.providerAppointmentFragment = this


        //appointment Adapter
        viewModel.getAppointmentByProviderResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    appointments = it.value.data
                    var appointmentAdapter = AppointmentAdapter(
                        appointments!!,
                        requireContext(),
                        status,
                        viewModel,
                        this@ProviderAppointmentFragment
                    )
                    binding.appointmentRecyclerView.adapter = appointmentAdapter
                }
            }
        })

        binding.filterTxt.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            status = position
            var appointmentAdapter = AppointmentAdapter(
                appointments!!,
                requireContext(),
                position,
                viewModel,
                this@ProviderAppointmentFragment
            )
            binding.appointmentRecyclerView.adapter = appointmentAdapter

        }

    }

    override fun getViewModel() = ProviderViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentProviderAppointmentBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): ProviderRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(ProviderApi::class.java, token)
        return ProviderRepository(api)
    }

    override fun confirm(item: Appointment, phoneToken: List<String>?, userId: Long) {



        val title = "Prise des RDV"
        val message =
            item.timeSlot.provider.firstName + " " + item.timeSlot.provider.lastName + " " +
                    getString(R.string.accept_notif) + " " + item.date + " à " + DataSource.timeConverter[item.timeSlot.time] + "H"
        val request = NotificationRequest(title, message, userId)

        viewModel.confirmAppointment(request, item.id, phoneToken, title, message)

        showPositiveAlert(requireActivity() , getString(R.string.reservation_confirmed))
    }

    override fun reject(item: Appointment, phoneToken: List<String>?, userId: Long) {
        val title = "Prise des RDV"
        val message =
            item.timeSlot.provider.firstName + " " + item.timeSlot.provider.lastName + " " + getString(R.string.reject_notif)

        val request = NotificationRequest(title, message, userId)
        viewModel.rejectAppointment(request, item.id, phoneToken, title, message)
        showNegativeAlert(requireActivity(), getString(R.string.reservation_rejected))
    }

    override fun remove(itemId: Long) {
        viewModel.removeAppointment(itemId)
        showPositiveAlert(requireActivity(), getString(R.string.reservation_deleted))
    }

    fun getDateOnly(date: Date):String{
        val dateOnly = SimpleDateFormat("yyyy-MM-dd")
        return dateOnly.format(date)
    }
    fun getTimeOnly(date: Date) : String{
        val timeOnly = SimpleDateFormat("HH:mm")
        return timeOnly.format(date)
    }

}