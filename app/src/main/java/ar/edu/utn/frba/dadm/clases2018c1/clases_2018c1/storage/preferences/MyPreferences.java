package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.storage.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

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

        String updatedSearchedMovies = addMovieSearched(title, searchedMovies);

        editor.putString(preference_searched_movies, updatedSearchedMovies);

        editor.apply();
    }

    public static ArrayList<String> getMoviesSearched(Context context) {
        SharedPreferences sharedPref = getPreferences(context);

        String searchedMovies = sharedPref.getString(preference_searched_movies, "");

        ArrayList<String> response = new ArrayList<>();
        response.addAll(Arrays.asList(searchedMovies.split(";")));

        return response;
    }

    @Nullable
    private static String addMovieSearched(String title, ArrayList<String> searchedMovies) {
        if(searchedMovies.size() > 5){
            searchedMovies.remove(1);
        }

        for (String searchedMovie: searchedMovies){
            if(searchedMovie.equals(title)){
                return moviesListToString(searchedMovies);
            }
        }

        searchedMovies.add(title);

        return moviesListToString(searchedMovies);
    }

    @NonNull
    private static String moviesListToString(ArrayList<String> searchedMovies) {
        StringBuilder sb = new StringBuilder();
        for (String searchedMovie : searchedMovies)
        {
            if(!searchedMovie.equals(searchedMovies.get(0))){
                sb.append(";");
            }
            sb.append(searchedMovie);
        }

        return sb.toString();
    }

}
