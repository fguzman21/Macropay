package com.example.macropay.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import com.github.ybq.android.spinkit.SpinKitView
import java.text.SimpleDateFormat
import java.time.Period
import java.time.ZoneId
import java.util.*
import java.util.regex.Pattern

object Utils {
    /*** Validación para saber si el dispositivo tiene conexión a internet ***/
    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }


    /*** Validación para saber si un email es válido ***/
    fun isEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }


    /*** Función para obtener el toke único una vez que se inicia sesión ***/
    fun getOnlyToken(context: Context): String{

        val preferences: SharedPreferences =
            context.getSharedPreferences("macro_pay_prefs", Context.MODE_PRIVATE)
        val token = preferences.getString("token","") ?: ""

        Log.d("Utils","Token -> $token")

        return token

    }

    /*** Función papra realizar animación de cargando.. en lo que se procesa la solicitud con el servidor ***/
    fun loader(activity: Activity, loader: SpinKitView?, show: Boolean){

        if (!activity.isDestroyed) {
            if (show) {
                if (loader != null) {
                    if (!loader.isVisible)
                        loader.visibility = View.VISIBLE
                }
            } else {
                if (loader != null) {
                    if (loader.isVisible)
                        loader.visibility = View.GONE
                }
            }
        }

    }

    /*** Función para guadar en nuestro dispositivo nuestro token una vez de hacer login ***/
    fun saveToken(context: Context, token: String){

        val preferences: SharedPreferences =
            context.getSharedPreferences("macro_pay_prefs", Context.MODE_PRIVATE)

        val editor = preferences.edit()
        editor.putString("token", token)
        editor.apply()

    }

}
