package com.example.macropay.views.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.macropay.R
import com.example.macropay.controllers.MoviesViewModel
import com.example.macropay.databinding.FragmentHomeBinding
import com.example.macropay.services.networking.responses.MoviesList
import com.example.macropay.services.networking.responses.MoviesResponse
import com.example.macropay.utils.Resource
import com.example.macropay.utils.Utils
import com.example.macropay.views.activities.MainActivity
import com.example.macropay.views.adapters.MovieAdapter
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport
import com.google.gson.Gson

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val moviesViewModel: MoviesViewModel by activityViewModels()
    val datosGenerales: ArrayList<MoviesList> = arrayListOf()
    /**Variables que nos sirven de guía para saber en que página estamos y requerimos hacer una nueva petición **/
    var feedPage: Int = 1
    var lastFeedPage: Int = 1
    var isLoading: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initRecyclerMovies()
        moviesViewModel.getMovies(feedPage)

        binding.swipeRefresh.setOnRefreshListener {

            feedPage = 1
            moviesViewModel.getMovies(feedPage)

        }



    }

    private fun initRecyclerMovies() {

        val adapter = MovieAdapter(datosGenerales)

        binding.rvMovies.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

        adapter.mostrarMovieClickListener = {view, movie, index ->
            var bundle: Bundle = Bundle()
            bundle.putInt("idMovie",movie.id)
            findNavController().navigate(R.id.action_detail_fragment, bundle)
        }
        binding.rvMovies.adapter = adapter


        /** Evento que solicita más datos al servidor enviandole un número de página **/
        binding.rvMovies.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = binding.rvMovies.layoutManager as LinearLayoutManager?

                if(!isLoading){

                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == datosGenerales.size - 1) {

                        feedPage += 1
                        if(feedPage <= lastFeedPage){

                            moviesViewModel.getMovies(feedPage)

                        }
                    }

                }


            }
        })
    }

    private fun initObservers() {
        /*** Inicialicación de Observables ***/
        moviesViewModel.MoviesListResponse.observe(viewLifecycleOwner){ response ->

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

                    val feed = response.data as MoviesResponse

                    feedPage = feed.page

                    lastFeedPage = feed.total_pages
                    val gson = Gson()
                    android.util.Log.d("Datos","${gson.toJson(response)}")
                    mostrarMovies(feed.results)
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

    private fun mostrarMovies(results: ArrayList<MoviesList>) {

        if(feedPage==1){
            datosGenerales.clear()
        }

        datosGenerales.addAll(results)

        val gson = Gson()
        android.util.Log.d("Datosresults","${gson.toJson(results)}")

        (binding.rvMovies.adapter as MovieAdapter).notifyDataSetChanged()

        if(binding.swipeRefresh.isRefreshing){
            binding.swipeRefresh.isRefreshing = false

        }
    }
}