package com.example.mobile_team11

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("prefs_name",Context.MODE_PRIVATE)

    fun getdata(key:String,Value:String): String {
        return prefs.getString(key,Value).toString()
    }

    fun getint(key:String,Value:Int):Int{
        return prefs.getInt(key,Value)
    }

    fun setdata(key:String,Value: String){
        prefs.edit().putString(key,Value).apply()
    }

    fun setint(key:String,Value: Int){
        prefs.edit().putInt(key,Value).apply()
    }
}
