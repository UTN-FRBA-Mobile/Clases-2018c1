package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.apifwk;

import android.support.annotation.Nullable;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class CustomAuthenticator implements Authenticator {
    private static final String AUTHORIZATION = "AUTHORIZATION_HEADER";

    @Nullable
    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        // Refresh your access_token using a synchronous api request
        String newAccessToken = getRefreshToken();

        // Add new header to rejected request and retry it
        return response.request().newBuilder()
                .header(AUTHORIZATION, newAccessToken)
                .build();
    }

    private String getRefreshToken() {
        return null;
    }
}
