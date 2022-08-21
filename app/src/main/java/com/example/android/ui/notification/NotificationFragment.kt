package com.example.android.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.android.data.network.NotificationApi
import com.example.android.data.network.Resource
import com.example.android.data.repository.NotificationRepository
import com.example.android.databinding.FragmentNotificaBinding
import com.example.android.models.NotificationViewModel
import com.example.android.ui.adapters.NotificationAdapter
import com.example.android.ui.base.BaseFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class NotificationFragment :
    BaseFragment<NotificationViewModel, FragmentNotificaBinding, NotificationRepository>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        viewModel.getNotificationByUser()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.notification.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    if (it.value.error != null) {
                        binding.errorMessage.text = it.value.error.toString()
                        binding.errorMessage.visibility = View.VISIBLE
                    } else if (it.value.data != null) {
                        if (binding.errorMessage.visibility == View.VISIBLE) binding.errorMessage.visibility = View.GONE
                        val notifications = it.value.data
                        var notificationsAdapter = NotificationAdapter(
                            notifications!!.sortedByDescending { it.date },
                            requireContext()
                        )
                        binding.notificationRecyclerView.adapter = notificationsAdapter
                    }
                }
            }
        })
    }

    override fun getViewModel() = NotificationViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentNotificaBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): NotificationRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(NotificationApi::class.java, token)
        return NotificationRepository(api)
    }


}