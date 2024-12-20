package com.example.mobile_team11

import android.app.Application
import java.util.prefs.Preferences

class MyApplication:Application() {
    companion object{
        lateinit var prefs:PreferenceUtil
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)

        super.onCreate()
    }
}