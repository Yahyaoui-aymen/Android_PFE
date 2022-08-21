package com.example.android.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.data.repository.*
import com.example.android.models.*
import java.lang.IllegalArgumentException

class ViewModelFactory(
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository as AuthRepository) as T
            modelClass.isAssignableFrom(ClientViewModel::class.java) -> ClientViewModel(repository as ClientRepository) as T
            modelClass.isAssignableFrom(ProviderViewModel::class.java) -> ProviderViewModel(repository as ProviderRepository) as T
            modelClass.isAssignableFrom(ReservationViewModel::class.java) -> ReservationViewModel(repository as ReservationRepository) as T
            modelClass.isAssignableFrom(NotificationViewModel::class.java) -> NotificationViewModel(repository as NotificationRepository) as T


            else -> throw IllegalArgumentException("ViewModelClass Not Found")
        }
    }
}
