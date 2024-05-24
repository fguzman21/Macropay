package com.example.macropay.views.activities

import android.R
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.macropay.databinding.ActivityMainBinding
import com.example.macropay.utils.Utils
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.github.ybq.android.spinkit.SpinKitView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    var loader: SpinKitView? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loader = binding.loader
        loader?.visibility = View.GONE

        binding.btnLogout.setOnClickListener {
            /*** Validación para saber si el usuario realmente quiere cerrar sesión  ***/
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Cerrar Sesión")
            builder.setMessage("¿Está seguro de cerrar sesión?")
            builder.setPositiveButton("Aceptar", { dialog, which ->
                validarSesionFacebook()
                validarSesionFirebase()
                Utils.saveToken(applicationContext,"")
                var mainIntent = Intent(this, AuthActivity::class.java)
                startActivity(mainIntent)
                finish()
            })
            builder.setNegativeButton("No", null)


            val dialog: AlertDialog = builder.create()
            dialog.show()


        }

    }

    /*** Función para mostrar u ocultar la barra de navegación en fragmentos  ***/
    fun actionBar(activa : Boolean){
        if(activa){
            binding.clToolbar.visibility = View.VISIBLE
        }else{
            binding.clToolbar.visibility = View.GONE
        }
    }

    /*** Validación se sesión de Facebook y cierre de la misma  ***/
    fun validarSesionFacebook(){

        var fbAccessToken = AccessToken.getCurrentAccessToken()

        if(fbAccessToken!=null){

            Log.d("LoginFragment","Existe una sesión de Facebook")

            if(!fbAccessToken.isExpired){

                Log.d("LoginFragment","Existe una sesión de Facebook -> La sesión no ha expirado")

                LoginManager.getInstance().logOut()

                Log.d("LoginFragment","Existe una sesión de Facebook -> La sesión no ha expirado -> Se cerró sesión")

            }

        }

    }
    /*** Validación se sesión de FireBase y cierre de la misma  ***/
    fun validarSesionFirebase(){

        var firebaseUser = Firebase.auth.currentUser

        if(firebaseUser!=null){

            Log.d("PerfilFragment","Usuario Firebase -> ${firebaseUser?.email} ")
            Firebase.auth.signOut()

        } else {
            Log.d("PerfilFragment"," No hay usuario de Firebase ")

        }

    }
}