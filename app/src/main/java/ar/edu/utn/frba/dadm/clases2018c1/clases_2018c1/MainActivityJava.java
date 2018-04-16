package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.api.responses.Movie;
import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.apifwk.Api;
import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.apifwk.Api2;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by guille on 4/16/18.
 */

public class MainActivityJava extends AppCompatActivity {

    @BindView(R.id.titleInput) EditText titleInput;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.findButton) View findButton;
    @BindView(R.id.favButton) View favButton;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    List<Movie> movies;
    MovieAdapter adapter;

    private Api api;
    private Api2 api2;

    private static final String MOVIES = "MOVIES";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_java);
        ButterKnife.bind(this);
        api = createNetworkClient(Api.class, "http://www.omdbapi.com");
        api2 = createNetworkClient(Api2.class, "https://utnmobi.firebaseio.com/");
        movies = new ArrayList<>();

        setupViews();
    }

    void setupViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MovieAdapter(this, movies);
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.findButton)
    void findMovie() {
        setLoading(true);
        String  apiKey = "7a9f6b43";
        String title = titleInput.getText().toString();
        api.findMovie(apiKey, title).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                setLoading(false);
                if (response.isSuccessful()) {
                    addMovie(response.body());
                }
                else {
                    showError("Error...");
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                showError(t);
            }
        });

    }

    @OnClick(R.id.favButton)
    void favMovie() {
        setLoading(true);
        String  apiKey = "7a9f6b43";
        String title = titleInput.getText().toString();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        api2.findMovie(uid).enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                setLoading(false);
                if (response.isSuccessful()) {
                    for(Movie m : response.body()) {
                        addMovie(m);
                    }
                }
                else {
                    showError("Error...");
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                setLoading(false);
                showError(t);
            }
        });

    }

    void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        findButton.setEnabled(!isLoading);
        favButton.setEnabled(!isLoading);

    }

    void addMovie(Movie movie) {
        movies.add(0, movie);
        adapter.notifyItemInserted(0);
        recyclerView.scrollToPosition(0);
    }

    void showError(Throwable error) {
        showError(error.toString());
    }

    void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public <T> T createNetworkClient(Class<T> apiClass, String baseUrl){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(interceptor);
//        OkHttpClient client = enableTls12OnPreLollipop(builder).build();
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(
                        GsonConverterFactory.create(
                                new GsonBuilder()
                                        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                                        .create()
                        ))
                .build();

        return retrofit.create(apiClass);
    }

//    private OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client) {
//        if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 22) {
//            try {
//                SSLContext sc = SSLContext.getInstance("TLSv1.2");
//                sc.init(null, null, null);
//                client.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()));
//
//                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
//                        .tlsVersions(TlsVersion.TLS_1_2)
//                        .build();
//
//                List<ConnectionSpec> specs = new ArrayList<>();
//                specs.add(cs);
//                specs.add(ConnectionSpec.COMPATIBLE_TLS);
//                specs.add(ConnectionSpec.CLEARTEXT);
//
//                client.connectionSpecs(specs);
//            } catch (Exception exc) {
//                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc);
//            }
//        }
//
//        return client;
//    }
}
