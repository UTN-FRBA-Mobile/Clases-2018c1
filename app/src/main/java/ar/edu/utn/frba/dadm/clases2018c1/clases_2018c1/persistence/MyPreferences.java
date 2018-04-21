package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyPreferences {
    private static String preference_searched_movies = "preference_searched_movies";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getPreferencesEditor(Context context) {
        return getPreferences(context).edit();
    }

    public static void addMovieSearched(Context context, String title) {
        ArrayList<String> searchedMovies = getMoviesSearched(context);

        SharedPreferences.Editor editor = getPreferencesEditor(context);

        if(searchedMovies.size() > 5){
            searchedMovies.remove(1);
        }

        for (String searchedMovie: searchedMovies){
            if(searchedMovie.equals(title)){
                //Won't add repeated movies to the list
                return;
            }
        }

        searchedMovies.add(title);

        StringBuilder sb = new StringBuilder();
        for (String searchedMovie : searchedMovies)
        {
            if(!searchedMovie.equals(searchedMovies.get(0))){
                sb.append(";");
            }
            sb.append(searchedMovie);
        }

        editor.putString(preference_searched_movies, sb.toString());

        editor.apply();
    }

    public static ArrayList<String> getMoviesSearched(Context context) {
        SharedPreferences sharedPref = getPreferences(context);

        String searchedMovies = sharedPref.getString(preference_searched_movies, "");

        ArrayList<String> response = new ArrayList<>();
        response.addAll(Arrays.asList(searchedMovies.split(";")));

        return response;
    }
}
