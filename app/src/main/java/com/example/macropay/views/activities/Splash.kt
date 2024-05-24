package com.example.macropay.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.macropay.R
import com.example.macropay.utils.Utils

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            /*** Validación para saber si el usuario ya tiene un inicio de sesión con Firese o Facebook y/o necesita
             * loguearse  ***/
            if(Utils.getOnlyToken(applicationContext).isNullOrEmpty()){
                val i = Intent(this, AuthActivity::class.java)
                startActivity(i)
                finish()
            }else{
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                finish()
            }
        }, 3000)
    }
}