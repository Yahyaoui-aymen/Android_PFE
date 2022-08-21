package com.example.android.ui.category

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.android.R

class RsltSearchCategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rslt_search_category)

        var i = intent.extras



        var government = i?.getString("government")
        var city = i?.getString("city")
        var categoryId = i?.getInt("category_id")!!


        val mBundle = Bundle()
        mBundle.putString("government", government)
        mBundle.putString("city" , city)
        mBundle.putInt("category_id" , categoryId)

        val mFragment = RsltSearchCategoryFragment()

        mFragment.arguments = mBundle

        supportFragmentManager!!.
        beginTransaction().
        add(R.id.rslt_search_category , mFragment,"ResultSearchCategoryFragment").commit()





    }


}