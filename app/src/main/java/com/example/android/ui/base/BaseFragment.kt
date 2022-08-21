package com.example.android.ui.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.android.R
import com.example.android.data.UserPreferences
import com.example.android.data.network.AuthApi
import com.example.android.data.network.RemoteDataSource
import com.example.android.data.repository.BaseRepository
import com.example.android.data.requests.LogoutRequest
import com.example.android.ui.auth.AuthActivity
import com.example.android.ui.startNewActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BaseFragment<VM : BaseViewModel, B : ViewBinding, R : BaseRepository> : Fragment() {

    protected lateinit var userPreferences: UserPreferences
    protected lateinit var binding: B
    protected lateinit var viewModel: VM
    protected val remoteDataSource = RemoteDataSource()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userPreferences = UserPreferences(requireContext())
        binding = getFragmentBinding(inflater, container)
        val factory = ViewModelFactory(getFragmentRepository())
        viewModel = ViewModelProvider(this, factory)[getViewModel()]

        lifecycleScope.launch { userPreferences.authToken.first() }
        return binding.root
    }


    fun logout() = lifecycleScope.launch {
        val authToken = userPreferences.authToken.first()
        val id = userPreferences.id.first()
        val api = remoteDataSource.buildApi(AuthApi::class.java)
        var phoneToken: String? = null
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            // Get new FCM registration token
            phoneToken = task.result
        })
        if (id != null) {
            val request = LogoutRequest(id)
            viewModel.logout(api, request)
            userPreferences.clear()
            if (phoneToken != null) {
                viewModel.deletePhoneToken(api, phoneToken!!)
            }
            requireActivity().startNewActivity(AuthActivity::class.java)
        }
    }

    abstract fun getViewModel(): Class<VM>

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B

    abstract fun getFragmentRepository(): R
}