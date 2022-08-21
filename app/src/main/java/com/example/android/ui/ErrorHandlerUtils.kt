package com.example.android.ui

import android.view.View
import androidx.fragment.app.Fragment
import com.example.android.R
import com.example.android.data.network.Resource
import com.example.android.ui.auth.LoginFragment
import com.google.android.material.snackbar.Snackbar

fun View.snackbar(message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    snackbar.show()
}

fun Fragment.handleApiError(
    failure: Resource.Failure,
    retry: (() -> Unit)? = null
) {

    when{
        failure.isNetworkError -> showNegativeAlert(requireActivity(),getString(R.string.check_cnx))
        failure.errorCode == 401 -> {
            if(this is LoginFragment) {
                requireView().snackbar("You've entered incorrect email or password")
            } else {
                //todo perform logout operation here
            }
        }
        else -> {
            val error = failure.errorBody?.string().toString()
            requireView().snackbar(error)
        }
    }
}



val errorHandler = mapOf(
    "Invalid password" to R.string.invalid_password,
    "Please use another username" to R.string.invalid_username,
    "Authentication error :Please check your username" to R.string.check_username,
    "Authentication error: Please check your password" to R.string.check_password
    )


