package com.example.macropay.views.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.macropay.R
import com.example.macropay.databinding.ActivityAuthBinding
import com.example.macropay.utils.Utils
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*


class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFacebookLogin()
        binding.btnLogin.setOnClickListener {
            iniciar_sesion_firebase()
        }

        binding.btnAccederFacebook.setOnClickListener {

            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"))

        }
    }

    private fun iniciar_sesion_firebase() {
        var textoCorreo = binding.etEmail.text.toString()
        var textoContrasena = binding.etPassword.text.toString()


        /*** Validaciones de campos ***/
        if(textoCorreo.isNullOrEmpty()){

            Toast.makeText(applicationContext,resources.getString(R.string.error_correo_vacio), Toast.LENGTH_SHORT).show()
            return

        }

        if(textoContrasena.isNullOrEmpty()){

            Toast.makeText(applicationContext,resources.getString(R.string.error_contrasena_vacia), Toast.LENGTH_SHORT).show()
            return

        }

        if(Utils.isEmail(textoCorreo)){
            /** Login con FireBase **/
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(textoCorreo,textoContrasena).addOnCompleteListener {

                    /*** Ingreso Correcto **/
                    if (it.isSuccessful){

                        var token = it.result.credential.toString()
                        Log.d("token",token)
                        Utils.saveToken(applicationContext, token)
                        val i = Intent(this, MainActivity::class.java)
                        startActivity(i)
                        finish()
                    }else{
                        showAlert()
                    }
                }

        }else{

            Toast.makeText(applicationContext,resources.getString(R.string.error_formato_correo), Toast.LENGTH_SHORT).show()
            return

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data)
    }



    fun initFacebookLogin(){

        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager,object :
            FacebookCallback<LoginResult> {

            override fun onSuccess(loginResult: LoginResult) {
                val userId = loginResult?.accessToken?.userId
                Log.d("LoginFragment", "onSuccess: userId $userId")
                loginFacebook(loginResult!!.accessToken)
            }

            override fun onCancel() {
                Log.d("LoginFragment", "onCancel: called")
            }

            override fun onError(error: FacebookException) {
                Log.d("LoginFragment", "onError: called -> ${error?.localizedMessage}")
                Log.d("LoginFragment", "onError: called -> ${error?.message}")
            }
        })

    }

    fun loginFacebook(token: AccessToken){

        val credentialsFacebook = FacebookAuthProvider.getCredential(token.token)

        var firebaseAuth = Firebase.auth

        firebaseAuth.signInWithCredential(credentialsFacebook).addOnCompleteListener { task ->
            /*** Validación para inicio correcto o incorrecto de Facebook ***/
            if(task.isSuccessful){

                val user = firebaseAuth.currentUser
                var idUser = user!!.uid
                var token = idUser.toString()

                Log.d("LoginFragment", "onSuccess: userId $token")
                Log.d("token",token)
                Utils.saveToken(applicationContext, token)
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                finish()

            }else{

                Log.d("LoginFragment","Error Al iniciar sesión con Facebook ${task.exception}")
                Toast.makeText(applicationContext,"Error al iniciar sesión con Facebook!",Toast.LENGTH_SHORT).show()

            }

        }

    }



    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Usuario o password inválidos")
        builder.setPositiveButton("Aceptar",null)
        val dialog:AlertDialog = builder.create()
        dialog.show()
    }
}