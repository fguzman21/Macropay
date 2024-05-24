package com.example.macropay.services.networking.responses

import com.google.gson.annotations.SerializedName


/*** Clase para que se ocupará para la lectura del detalle de una película ***/
data class MovieDetailResponse(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("original_title")
    var nombre:String = "",
    @SerializedName("poster_path")
    var imagen: String = "",
    @SerializedName("overview")
    var descripcion: String = "",
    @SerializedName("vote_average")
    var calificacion: String = "",
    @SerializedName("runtime")
    var duracion: String = "",
    @SerializedName("release_date")
    var fecha_estreno: String = "",
    @SerializedName("imdb_id")
    var clasificacion: String = "",
    @SerializedName("genres")
    var genres: ArrayList<GenresList> = arrayListOf()
)
