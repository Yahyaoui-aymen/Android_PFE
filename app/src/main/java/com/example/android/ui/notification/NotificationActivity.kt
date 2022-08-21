package com.example.android.ui.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.android.R

class NotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)



        supportFragmentManager!!.beginTransaction()
            .add(R.id.notification, NotificationFragment(), "notification").commit()
    }
}