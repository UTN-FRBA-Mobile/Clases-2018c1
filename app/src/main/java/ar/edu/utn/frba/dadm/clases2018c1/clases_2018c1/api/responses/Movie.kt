package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.api.responses

import java.io.Serializable

class Movie: Serializable {

    var response: Boolean = false
    var title: String? = null
    var year: String? = null
    var poster: String? = null
    var metascore: String? = null
}