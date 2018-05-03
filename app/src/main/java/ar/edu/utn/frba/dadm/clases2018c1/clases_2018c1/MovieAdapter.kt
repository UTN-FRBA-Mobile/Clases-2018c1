package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1

import android.content.Context
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.api.responses.Movie
import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.storage.db.AppDatabase
import com.squareup.picasso.Picasso
import android.graphics.drawable.Drawable
import android.graphics.Bitmap
import android.support.v4.content.ContextCompat
import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.storage.fileSystem.ExternalStorage
import java.lang.Exception

class MovieAdapter(private val context: Context, private val movies: List<ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.api.responses.Movie>, private val storePostersInFs: Boolean) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    val inflater = LayoutInflater.from(context)

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.movie_item, parent, false), context, storePostersInFs)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    class ViewHolder(itemView: View, private val context: Context, private val storePostersInFs: Boolean) : RecyclerView.ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.moviePoster)
        val title: TextView = itemView.findViewById(R.id.movieTitle)
        val year: TextView = itemView.findViewById(R.id.movieYear)
        val setStarred: ImageView = itemView.findViewById(R.id.set_starred)
        val unsetStarred: ImageView = itemView.findViewById(R.id.unset_starred)
        val movieDao = AppDatabase.getInstance(context).movieDao()
        var storedMovie: ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.storage.db.entities.Movie? = null

        fun bind(movie: Movie) {
            class BindAsync : AsyncTask<Void, Void, ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.storage.db.entities.Movie>() {
                override fun doInBackground(vararg params: Void): ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.storage.db.entities.Movie? {
                    return movieDao.getByTitle(title.text.toString())
                }

                override fun onPostExecute(storedValue: ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.storage.db.entities.Movie?) {
                    storedMovie = storedValue

                    if(storedMovie == null){
                        setStarred.visibility = View.VISIBLE
                        unsetStarred.visibility = View.GONE
                    } else {
                        setStarred.visibility = View.GONE
                        unsetStarred.visibility = View.VISIBLE
                    }

                    setUpSetStarred(movie)

                    setUpUnsetStarred()
                }
            }

            title.text = movie.title
            year.text = movie.year

            val fsPosterUri = ExternalStorage.getFileUri(movie.title!!)
            //val fsPosterUri = ExternalStorage.getCacheFileUri(context, movie.title!!)
            //val fsPosterUri = InternalStorage.getFileUri(context, movie.title!!)
            //val fsPosterUri = InternalStorage.getCacheFileUri(context, movie.title!!)
            if(fsPosterUri == null){
                if(storePostersInFs){
                    Picasso.get().load(movie.poster).placeholder(android.R.drawable.ic_media_play).into(getTarget(context, poster, movie.title!!))
                } else {
                    Picasso.get().load(movie.poster).placeholder(android.R.drawable.ic_media_play).into(poster)
                }
            } else {
                Picasso.get().load(fsPosterUri).placeholder(android.R.drawable.ic_media_play).into(poster)
            }

            BindAsync().execute()
        }

        private fun setUpSetStarred(movie: Movie) {
            class setUpSetStarredAsync : AsyncTask<Void, Void, Unit>() {
                override fun doInBackground(vararg params: Void): Unit {

                    movieDao.insert(storedMovie)

                    return Unit
                }

                override fun onPostExecute(response: Unit) {
                    setStarred.visibility = View.GONE
                    unsetStarred.visibility = View.VISIBLE
                }
            }

            setStarred.setOnClickListener({
                storedMovie = ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.storage.db.entities.Movie()

                storedMovie!!.title = movie.title!!
                storedMovie!!.year = movie.year
                storedMovie!!.poster = movie.poster

                setUpSetStarredAsync().execute()
            })
        }

        private fun setUpUnsetStarred() {
            class setUpUnsetStarredAsync : AsyncTask<Void, Void, Unit>() {
                override fun doInBackground(vararg params: Void): Unit {
                    if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        ExternalStorage.deleteFile(storedMovie!!.title!!)
                        //ExternalStorage.deleteFileFromCache(context, fileName)
                    }
                    //InternalStorage.deleteFile(context, fileName)
                    //InternalStorage.deleteFileFromCache(context, fileName)
                    movieDao.delete(storedMovie)

                    return Unit
                }

                override fun onPostExecute(response: Unit) {
                    setStarred.visibility = View.VISIBLE
                    unsetStarred.visibility = View.GONE
                }
            }

            unsetStarred.setOnClickListener({
                setUpUnsetStarredAsync().execute()
            })
        }

        private fun getTarget(context: Context, view: ImageView, fileName: String): com.squareup.picasso.Target {
            return object : com.squareup.picasso.Target {
                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                }

                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                    if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        ExternalStorage.saveFile(bitmap, fileName)
                        //ExternalStorage.saveFileInCache(context, bitmap, fileName)
                    }
                    //InternalStorage.saveFile(context, bitmap, fileName)
                    //InternalStorage.saveFileInCache(context, bitmap, fileName)

                    view.setImageBitmap(bitmap)
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable){
                }
            }
        }
    }
}