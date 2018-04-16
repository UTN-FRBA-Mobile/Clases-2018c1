package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by emanuel on 10/4/17.
 */

public class ResponseParser {

    public static final ResponseParser instance = new ResponseParser();

    final Gson gson;

    ResponseParser() {
        GsonBuilder builder = new GsonBuilder();
        builder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
        gson = builder.create();
    }

    public <T> T parse(String jsonString, Class<T> clazz) {
        return gson.fromJson(jsonString, clazz);
    }
}

