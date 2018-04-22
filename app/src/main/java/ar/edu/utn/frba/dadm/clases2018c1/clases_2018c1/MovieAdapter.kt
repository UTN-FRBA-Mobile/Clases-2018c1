package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.api.responses.Movie
import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.persistence.db.AppDatabase
import com.squareup.picasso.Picasso

class MovieAdapter(val context: Context, val movies: List<ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.api.responses.Movie>) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    val inflater = LayoutInflater.from(context)

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.movie_item, parent, false), context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    class ViewHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.moviePoster)
        val title: TextView = itemView.findViewById(R.id.movieTitle)
        val year: TextView = itemView.findViewById(R.id.movieYear)
        val setStarred: ImageView = itemView.findViewById(R.id.set_starred)
        val unsetStarred: ImageView = itemView.findViewById(R.id.unset_starred)
        val movieDao = AppDatabase.getInstance(context).movieDao()
        var storedMovie: ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.persistence.db.entities.Movie? = null

        fun bind(movie: Movie) {
            title.text = movie.title
            year.text = movie.year
            Picasso.get().load(movie.poster).placeholder(android.R.drawable.ic_media_play).into(poster)

            object : AsyncTask<Void, Void, ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.persistence.db.entities.Movie>() {
                override fun doInBackground(vararg params: Void): ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.persistence.db.entities.Movie? {
                    return movieDao.getByTitle(title.text.toString())
                }

                override fun onPostExecute(storedValue: ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.persistence.db.entities.Movie?) {
                    storedMovie = storedValue

                    if(storedMovie == null){
                        setStarred.visibility = View.VISIBLE
                        unsetStarred.visibility = View.GONE
                    } else {
                        setStarred.visibility = View.GONE
                        unsetStarred.visibility = View.VISIBLE
                    }

                    setStarred.setOnClickListener({
                        storedMovie = ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.persistence.db.entities.Movie()

                        storedMovie!!.title = movie.title!!
                        storedMovie!!.year = movie.year
                        storedMovie!!.imageUri = ""

                        object : AsyncTask<Void, Void, Unit>() {
                            override fun doInBackground(vararg params: Void): Unit {
                                movieDao.insert(storedMovie)
0
                                return Unit
                            }

                            override fun onPostExecute(response: Unit) {
                                setStarred.visibility = View.GONE
                                unsetStarred.visibility = View.VISIBLE
                            }
                        }.execute()
                    })

                    unsetStarred.setOnClickListener({
                        object : AsyncTask<Void, Void, Unit>() {
                            override fun doInBackground(vararg params: Void): Unit {
                                movieDao.delete(storedMovie)

                                return Unit
                            }

                            override fun onPostExecute(response: Unit) {
                                setStarred.visibility = View.VISIBLE
                                unsetStarred.visibility = View.GONE
                            }
                        }.execute()
                    })
                }
            }.execute()
        }
    }
}