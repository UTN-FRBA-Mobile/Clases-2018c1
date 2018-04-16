package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.api;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.api.responses.Movie;

/**
 * Created by emanuel on 10/4/17.
 */

public class OmdbApi {

    private static final String TAG = OmdbApi.class.getName();

    public static Runnable getMovie(String apiKey, String title, final Callback<Movie> callback) {
        final URL url;
        try {
            url = new URL("http://www.omdbapi.com/?apikey=" + apiKey + "&t=" + URLEncoder.encode(title, "UTF-8"));
        }
        catch (MalformedURLException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return getRequest(url, Movie.class, callback);
    }

    private static <T> Runnable getRequest(final URL url, final Class<T> type, final Callback<T> callback) {
        UrlRequest request = UrlRequest.makeRequest(url, new UrlRequest.Listener() {
            @Override
            public void onReceivedBody(int responseCode, String body) {
                // Evaluamos la respuesta
                if (responseCode == 200) {
                    try {
                        // Respondieron OK, parseamos la respuesta esperada
                        T response = ResponseParser.instance.parse(body, type);
                        callback.onSuccess(response);
                    } catch (Exception e) {
                        Log.d(TAG, "Falló el parseo.");
                        callback.onError(e);
                    }
                }
                else {
                    Log.d(TAG, "Falló la llamada.");
                    callback.onError(null); // TODO: agregar un error
                }
            }

            @Override
            public void onError(Exception e) {
                Log.d(TAG, "Falló la llamada.");
                callback.onError(e);
            }
        });
        return request;
    }
}
