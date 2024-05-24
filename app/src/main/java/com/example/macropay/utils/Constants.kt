package com.example.macropay.utils

import android.os.Environment

object Constants {

    /*** Url del Servidor al cúal se le está solicitando el servicio  ***/
    const val BASE_URL: String = "https://api.themoviedb.org/3/movie/"
    /*** Token de autenticación para que el api nos brinde autorización  ***/
    const val TEMP_AUTH_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhYzFjMGM4MGU3NjA4NGQ2ZDJmM2ZlOGUzMTliMDMzZiIsInN1YiI6IjY2NTAwODAxNTg3NzE3NjY3OTliMzU5ZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.l8qYBnyxWm67LogBVATTq_I4MFWSb4lI06rfZ8pjAys"
    /*** Url del Servidor en el cúal se encuentrán alojadas las imagenes que se mostrarán en la app  ***/
    const val URL_IMAGES = "http://image.tmdb.org/t/p/w500//"
}