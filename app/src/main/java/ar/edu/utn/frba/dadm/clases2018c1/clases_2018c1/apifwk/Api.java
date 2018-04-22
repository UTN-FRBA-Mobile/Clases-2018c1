package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.apifwk;


import java.util.List;

import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.api.responses.Movie;
import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.api.responses.MovieSearch;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by guille on 4/16/18.
 */

public interface Api {

    @GET("/")
    Call<MovieSearch> findMovie(
            @Query("apiKey") String apiKey,
            @Query("s") String title);
}
