package com.example.macropay.services.networking.responses

import com.google.gson.annotations.SerializedName

/*** Clase para que se ocupará para la lectura de genéros de películas ***/
data class GenresList(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var nombre:String = ""
)
