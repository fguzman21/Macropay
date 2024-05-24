package com.example.macropay.views.adapters

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.macropay.R
import com.example.macropay.databinding.ItemMovieBinding
import com.example.macropay.services.networking.responses.MoviesList
import com.example.macropay.utils.Constants.URL_IMAGES
import com.example.macropay.views.fragments.HomeFragment

/*** Adapdador que nos sirve para poder hacer la lectura y la muestra del listado de Películas ***/
class MovieAdapter(val movies: ArrayList<MoviesList>):
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    var mostrarMovieClickListener: ((view: View, movie: MoviesList, index: Int) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.mostrarMovieClickListener = mostrarMovieClickListener
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun flex(index: Int, flex: Boolean) {


        notifyItemChanged(index)

    }

    class MovieViewHolder(val binding: ItemMovieBinding): RecyclerView.ViewHolder(binding.root){

        var mostrarMovieClickListener: ((view: View, movies: MoviesList, index: Int) -> Unit)? = null


        fun bind(movies: MoviesList){


            var Tittle = movies.nombre
            var Raiting = movies.calificacion

            binding.tvTittle.text = Tittle.toString()
            binding.tvRaiting.text = Raiting.toString()
            Log.d("movies",movies.toString())

            binding.apply {



                    try{

                         Glide
                            .with(binding.root.context)
                            .load(URL_IMAGES+movies.imagen)
                            .placeholder(R.drawable.image_loading)
                            .into(binding.ivImageMovie)

                    }catch (ex: Exception){
                        Log.e("movieAdapter","Error: ${ex.message}")
                    }

                binding.ivImageMovie.setOnClickListener {

                    mostrarMovieClickListener?.invoke(it,movies,adapterPosition)

                }



            }

        }

    }

}