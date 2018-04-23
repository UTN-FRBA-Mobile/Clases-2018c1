package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.api.Callback
import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.api.OmdbApi
import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.api.responses.Movie
import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.api.responses.MovieSearch
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    val MOVIES = "MOVIES"

    lateinit var titleInput: EditText
    lateinit var progressBar: ProgressBar
    lateinit var findButton: View
    lateinit var recyclerView: RecyclerView
    lateinit var movies: ArrayList<Movie>
    lateinit var adapter: MovieAdapter

    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        movies = savedInstanceState?.getSerializable(MOVIES) as ArrayList<Movie>? ?: ArrayList()
        setupViews()
    }

    fun setupViews() {
        titleInput = findViewById(R.id.titleInput)
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        findButton = findViewById(R.id.findButton)
        findButton.setOnClickListener {
            findMovie()
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MovieAdapter(this, movies, false)
        recyclerView.adapter = adapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(MOVIES, movies)
    }

    fun findMovie() {
        setLoading(true)
        val apiKey = "7a9f6b43"
        val title = titleInput.text.toString()
        executor.submit(OmdbApi.getMovies(apiKey, title, object : Callback<MovieSearch> {
            override fun onSuccess(movieSearch: MovieSearch) {

                runOnUiThread {
                    setLoading(false)
                    if (movieSearch.response) {
                        for (movie in movieSearch.search!!){
                            addMovie(movie)
                        }
                    }
                    else {
                        showError(null)
                    }
                }
            }
            override fun onError(error: Exception?) {
                runOnUiThread {
                    setLoading(false)
                    showError(error)
                }
            }
        }))
    }

    fun setLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        findButton.isEnabled = !isLoading

    }

    fun addMovie(movie: Movie) {
        movies.add(0, movie)
        adapter.notifyItemInserted(0)
        recyclerView.scrollToPosition(0)
    }

    fun showError(error: Exception?) {
        Toast.makeText(this, error?.toString() ?: "Error", Toast.LENGTH_LONG).show()
    }
}
