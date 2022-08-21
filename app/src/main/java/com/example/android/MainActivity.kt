package com.example.android

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.example.android.data.UserPreferences
import com.example.android.ui.auth.AuthActivity
import com.example.android.ui.client_home.ClientHomeActivity
import com.example.android.ui.provider_home.ProviderHomeActivity
import com.example.android.ui.startNewActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.intellij.lang.annotations.Language
import java.util.*
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities

class MainActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT:Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userPreferences = UserPreferences(this)

        Handler().postDelayed( {
            val role = runBlocking { userPreferences.role.first()?.toString() }
            userPreferences.authToken.asLiveData().observe(this, androidx.lifecycle.Observer {
                if (it == null) {
                    startNewActivity(AuthActivity::class.java)
                } else {
                    if (role == "[ROLE_CLIENT]") {
                        startNewActivity(ClientHomeActivity::class.java)
                    }
                    if (role == "[ROLE_PRESTATAIRE]") {
                        startNewActivity(ProviderHomeActivity::class.java)
                    }

                }
            })
        } , SPLASH_TIME_OUT)

        val prefs = getSharedPreferences("Settings", MODE_PRIVATE)
        val language = prefs.getString("MY_LANG", "")

        //get device language

        val lang = Locale.getDefault().displayLanguage

        if (language.equals("ar")) {
            setLocal(language!!);
        } else if (language.equals("fr")) {
            setLocal(language!!);
        } else {
            if (lang.equals("العربية")) {
                setLocal("ar");
            } else {
                setLocal("fr");
            }

        }
    }

    private fun setLocal(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        //save language to local shared preferences
        val editor = getSharedPreferences("Settings", MODE_PRIVATE).edit()
        if (lang.equals("ar")) {
            editor.putString("MY_LANG", lang)
        } else {
            editor.putString("MY_LANG", "fr")
        }
        editor.apply()
    }
}
