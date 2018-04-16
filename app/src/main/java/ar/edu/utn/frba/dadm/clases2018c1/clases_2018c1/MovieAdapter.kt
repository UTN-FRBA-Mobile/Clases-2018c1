package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.api.responses.Movie

class MovieAdapter(val context: Context, val movies: List<Movie>) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    val inflater = LayoutInflater.from(context)

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.movie_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val poster: ImageView = itemView.findViewById(R.id.moviePoster)
        val title: TextView = itemView.findViewById(R.id.movieTitle)
        val year: TextView = itemView.findViewById(R.id.movieYear)

        fun bind(movie: Movie) {
            title.text = movie.title
            year.text = movie.year
            ImageLoader.instance.loadImage(movie.poster, poster)
        }
    }
}