package com.example.macropay.controllers

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.macropay.R
import com.example.macropay.services.networking.request.MoviesPageRequest
import com.example.macropay.services.networking.responses.MovieDetailResponse
import com.example.macropay.services.networking.responses.MoviesResponse
import com.example.macropay.utils.Constants.TEMP_AUTH_TOKEN
import com.example.macropay.utils.Resource
import com.example.macropay.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.easycode.coachapp.model.data.MicroPayService

/*** Model View Creado para Controlar el Listado de Películas / Detalle de las mismas ***/
class MoviesViewModel(private val app: Application): AndroidViewModel(app) {

    val MoviesListResponse: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()
    val DetalleResponse: MutableLiveData<Resource<MovieDetailResponse>> = MutableLiveData()


    fun getMovies(page: Int) = viewModelScope.launch(Dispatchers.IO){

        MoviesListResponse.postValue(Resource.Loading())

        try{

            if(Utils.isNetworkAvailable(app)){

                val result = MicroPayService.getInstance().getMovies(TEMP_AUTH_TOKEN,page)

                if(result.isSuccessful){

                    MoviesListResponse.postValue(Resource.Success(result.body() as MoviesResponse))

                }else{

                    MoviesListResponse.postValue(Resource.Error(result.message(),null))

                }

            } else {

                MoviesListResponse.postValue(Resource.Error(app.resources.getString(R.string.error_red)))

            }

        }catch (ex: Exception){

            MoviesListResponse.postValue(Resource.Error(ex.localizedMessage?.toString() ?: "Error",null))
            Log.e("HomeList", "Error: -> ${ex.localizedMessage}")

        }

    }


    fun obtenerMovie(idMovie: Int) = viewModelScope.launch(Dispatchers.IO){

        DetalleResponse.postValue(Resource.Loading())

        try{

            if(Utils.isNetworkAvailable(app)){

                val result = MicroPayService.getInstance().getInfoMovie(TEMP_AUTH_TOKEN,idMovie)

                if(result.isSuccessful) {

                    DetalleResponse.postValue(Resource.Success(result.body() as MovieDetailResponse))

                }else{

                    DetalleResponse.postValue(Resource.Error(result.message(),null))

                }

            } else {

                DetalleResponse.postValue(Resource.Error(app.resources.getString(R.string.error_red)))

            }

        }catch (ex: Exception){

            DetalleResponse.postValue(Resource.Error(ex.localizedMessage?.toString() ?: "Error",null))
            Log.e("MovieDetail", "Error: -> ${ex.localizedMessage}")

        }

    }



    fun cleanFlex(){
        MoviesListResponse.value = null
    }

}