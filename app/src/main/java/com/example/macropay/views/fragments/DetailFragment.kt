package com.example.macropay.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.macropay.R
import com.example.macropay.controllers.MoviesViewModel
import com.example.macropay.databinding.FragmentDetailBinding
import com.example.macropay.services.networking.responses.MovieDetailResponse
import com.example.macropay.utils.Constants
import com.example.macropay.utils.Resource
import com.example.macropay.utils.Utils
import com.example.macropay.views.activities.MainActivity
import com.google.gson.Gson


class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private var idMovie: Int = 0
    private val moviesViewModel: MoviesViewModel by activityViewModels()
    var isLoading: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /***Obtenemos el parametro de la ventana anterior**/
        idMovie = arguments?.getInt("idMovie") ?: 0
        initObservers()
        moviesViewModel.obtenerMovie(idMovie)
        (requireActivity() as MainActivity).actionBar(false)

        binding.ivBack.setOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
            (requireActivity() as MainActivity).actionBar(true)
        }


    }

    fun initObservers() {
        /*** Inicialicación de Observables ***/
        moviesViewModel.DetalleResponse.observe(viewLifecycleOwner){ response ->

            when (response) {

                is Resource.Loading -> {

                    isLoading = true
                    Utils.loader(
                        requireActivity(),
                        (requireActivity() as MainActivity).loader,
                        true
                    )

                }

                is Resource.Success -> {

                    val result = response.data as MovieDetailResponse


                    val gson = Gson()
                    android.util.Log.d("Datos","${gson.toJson(response)}")
                    mostrarMovieDetail(result)
                    isLoading = false

                    Utils.loader(
                        requireActivity(),
                        (requireActivity() as MainActivity).loader,
                        false
                    )

                }

                is Resource.Error -> {

                    val msj = response.message ?: ""
                    Toast.makeText(requireContext(), msj, Toast.LENGTH_SHORT).show()
                    android.util.Log.d("Datos Error","${msj}")

                    Utils.loader(
                        requireActivity(),
                        (requireActivity() as MainActivity).loader,
                        false
                    )
                    isLoading = false

                }

            }

        }

    }

    private fun mostrarMovieDetail(result: MovieDetailResponse) {

        try{
            /*** Librería que nos ayudará a mostrar la imagen de la Película  ***/
            Glide
                .with(binding.root.context)
                .load(Constants.URL_IMAGES +result.imagen)
                .placeholder(R.drawable.image_loading)
                .into(binding.ivImageMovie)

        }catch (ex: Exception){
            Log.e("movieAdapter","Error: ${ex.message}")
        }

        /*** Asignación y muestra de valores  ***/
        binding.tvTittle.text = result.nombre
        binding.tvDescripcion.text = result.descripcion
        binding.tvDuracion.text = "${result.duracion} minutos"
        binding.tvEstreno.text = result.fecha_estreno
        binding.tvClasificacion.text = result.clasificacion
        var generos: String = ""


        /*** Recorrido de Array de Genéros para mostrar en un sólo campo ***/
        result.genres.forEach {it
            generos+= "${it.nombre} /"
        }

        binding.tvGeneros.text = generos

    }

}