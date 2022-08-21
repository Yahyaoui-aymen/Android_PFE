package com.example.android.ui.client_home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.android.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class ClientHomeActivity : AppCompatActivity(R.layout.activity_client_home) {

    private lateinit var navController: NavController
    private val clientCategoryFragment = ClientCategoryFragment()
    private val clientBestPlanFragment = ClientBestPlanFragment()
    private val clientProfileFragment = ClientProfileFragment()
    private val clientHomeFragment = ClientHomeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.client_nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController
       // loadFragment(clientHomeFragment)


        findViewById<BottomNavigationView>(R.id.bottom_nav_view).setOnItemSelectedListener{
            when(it.itemId) {
                R.id.home -> loadFragment(clientHomeFragment)
                R.id.profile -> loadFragment(clientProfileFragment)
                R.id.category -> loadFragment(clientCategoryFragment)
                R.id.best_plan -> loadFragment(clientBestPlanFragment)
            }
            true
        }


    }


    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.client_nav_host_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}