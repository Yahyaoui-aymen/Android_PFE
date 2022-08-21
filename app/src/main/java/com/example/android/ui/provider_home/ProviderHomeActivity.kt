package com.example.android.ui.provider_home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.android.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProviderHomeActivity : AppCompatActivity(R.layout.activity_provider_home) {
    private lateinit var navController: NavController
    private val providerHomeFragment = ProviderHomeFragment()
    private val providerProfileFragment = ProviderProfileFragment()
    private val providerServiceFragment = ProviderServiceFragment()
    private val providerTimeslotFragment = ProviderTimeslotFragment()
    private val providerAppointmentFragment = ProviderAppointmentFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager!!.
        beginTransaction().
        add(R.id.provider_nav_host_fragment, providerHomeFragment,"HomeFragment").commit()

       // loadFragment(providerHomeFragment)


        findViewById<BottomNavigationView>(R.id.bottom_nav_view).setOnItemSelectedListener{
            when(it.itemId) {
                R.id.home -> loadFragment(providerHomeFragment)
                R.id.profile -> loadFragment(providerProfileFragment)
                R.id.plus -> loadFragment(providerServiceFragment)
                R.id.time -> loadFragment(providerTimeslotFragment)
                R.id.appointment -> loadFragment(providerAppointmentFragment)
            }
            true
        }


    }

    fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.provider_nav_host_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}




