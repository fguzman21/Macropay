package com.example.macropay.services.networking.request

import com.google.gson.annotations.SerializedName

/*** Clase para que se ocupará para solicitar más páginas de datos al servidor ***/
data class MoviesPageRequest(
    @SerializedName("page")
    var page: Int = 1
)
