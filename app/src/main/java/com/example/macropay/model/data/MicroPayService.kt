package mx.easycode.coachapp.model.data

import com.example.macropay.services.networking.request.MoviesPageRequest
import com.example.macropay.services.networking.responses.MovieDetailResponse
import com.example.macropay.services.networking.responses.MoviesResponse
import com.example.macropay.utils.Constants
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface MicroPayService {

    companion object{

        var service: MicroPayService? = null
        /*** Generación de Instancia con RetroFit ***/
        fun getInstance(): MicroPayService{
            if(service == null){

                val okhttpClient = OkHttpClient.Builder()

                okhttpClient.addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })

                /*** Definición de tiempo para esperar conexión ***/
                okhttpClient.connectTimeout(120,TimeUnit.SECONDS)
                okhttpClient.readTimeout(120,TimeUnit.SECONDS)

                val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okhttpClient.build())
                    .build()

                service = retrofit.create(MicroPayService::class.java)
            }
            return service!!
        }

    }


    /*** Peticiones al Servidor ***/

    @GET("now_playing")/*** Petición Listado de Películas ***/
   suspend fun getMovies(@Header("Authorization") authToken: String, @Query("page") page : Int): Response<MoviesResponse>

    @GET("{idMovie}")/*** Petición Detalle de Película ***/
    suspend fun getInfoMovie(@Header("Authorization") authToken: String, @Path("idMovie") idMovie : Int): Response<MovieDetailResponse>



}