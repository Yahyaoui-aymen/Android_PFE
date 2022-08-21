package com.example.android.ui.reservation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android.R

class ReservationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        var i = intent.extras

        var providerId = i?.getLong("provider_id")
        var firstName = i?.getString("first_name")
        var lastName = i?.getString("last_name")
        var government = i?.getString("government")
        var city = i?.getString("city")
        var phoneNumber = i?.getString("phoneNumber")
        var imageUrl = i?.getString("imageUrl")
        var category = i?.getString("category")
        var isOffer = i?.getBoolean("isOffer")


        val mBundle = Bundle()
        mBundle.putLong("provider_id", providerId!!)
        mBundle.putString("first_name", firstName)
        mBundle.putString("last_name", lastName)
        mBundle.putString("government", government)
        mBundle.putString("city", city)
        mBundle.putString("phoneNumber", phoneNumber)
        mBundle.putString("imageUrl", imageUrl)
        mBundle.putString("category", category)
        mBundle.putBoolean("isOffer", isOffer!!)



        val mFragment = ReservationFragment()
        mFragment.arguments = mBundle


        supportFragmentManager!!.beginTransaction()
            .add(R.id.reservation, mFragment, "reservation").commit()
    }
}