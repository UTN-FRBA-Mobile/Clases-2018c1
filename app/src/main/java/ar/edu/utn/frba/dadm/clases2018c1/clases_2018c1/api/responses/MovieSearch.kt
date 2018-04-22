package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.api.responses

import java.io.Serializable

class MovieSearch : Serializable {
    var response: Boolean = false
    var search: List<Movie>? = null
    var totalResults: String? = null
}