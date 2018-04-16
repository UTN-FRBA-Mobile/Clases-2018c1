package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.api;

/**
 * Created by emanuel on 10/4/17.
 */

public interface Callback<T> {

    void onSuccess(T response);
    void onError(Exception e);
}
