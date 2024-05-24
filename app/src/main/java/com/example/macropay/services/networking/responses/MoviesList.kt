package com.example.macropay.services.networking.responses

import com.google.gson.annotations.SerializedName

/*** Clase para que se ocupará para la lectura de la lista de películas***/
data class MoviesList(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("original_title")
    var nombre:String = "",
    @SerializedName("poster_path")
    var imagen: String = "",
    @SerializedName("vote_average")
    var calificacion: String = ""
)
