package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class SearchedMoviesAdapter(val context: Context, val searchResults: List<String>) : RecyclerView.Adapter<SearchedMoviesAdapter.ViewHolder>() {

    val inflater = LayoutInflater.from(context)
    val mListener: SearchedMoviesAdapter.IListener = context as SearchedMoviesAdapter.IListener

    interface IListener {
        fun itemClicked(title: String)
    }

    override fun getItemCount(): Int {
        return searchResults.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.searched_movie_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searchResults[position])
        holder.itemView.setOnClickListener(View.OnClickListener {view ->
            mListener?.itemClicked(view.findViewById<TextView>(R.id.movieTitle).text.toString())
        })
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.movieTitle)

        fun bind(searchedMovie: String) {
            title.text = searchedMovie
        }
    }
}