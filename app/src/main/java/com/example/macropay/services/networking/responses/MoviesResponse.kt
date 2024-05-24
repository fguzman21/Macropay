package com.example.macropay.services.networking.responses

import com.google.gson.annotations.SerializedName


/*** Clase para que se ocupará para obtener el listado de peliculals por página y saber el número de la misma ***/
data class MoviesResponse(
    @SerializedName("page")
    var page: Int = 0,
    @SerializedName("total_pages")
    var total_pages: Int = 0,
    @SerializedName("results")
    var results: ArrayList<MoviesList> = arrayListOf()
)
