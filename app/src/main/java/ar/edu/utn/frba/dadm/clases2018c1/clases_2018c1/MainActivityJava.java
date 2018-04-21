package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.api.responses.Movie;
import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.apifwk.Api;
import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.apifwk.Api2;
import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.persistence.MyPreferences;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by guille on 4/16/18.
 */

public class MainActivityJava extends AppCompatActivity implements SearchedMoviesAdapter.IListener {

    @BindView(R.id.titleInput) EditText titleInput;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.findButton) View findButton;
    @BindView(R.id.favButton) View favButton;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.searchRecyclerView) RecyclerView searchRecyclerView;
    List<Movie> movies;
    MovieAdapter adapter;
    SearchedMoviesAdapter searchedMoviesAdapter;

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

        titleInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0){
                    showHistory(s.toString());
                } else {
                    hideHistory();
                }
            }
        });

        setupViews();
    }

    void setupViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MovieAdapter(this, movies);
        recyclerView.setAdapter(adapter);
//        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
//            @Override
//            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//                View child = rv.findChildViewUnder(e.getX(), e.getY());
//                if (child != null) {
//                    //findMovie(((TextView)child).getText().toString());
//                    Toast.makeText(MainActivityJava.this, "HOLA", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @OnClick(R.id.findButton)
    void findMovie() {
        final String title = titleInput.getText().toString();

        findMovie(title);
    }

    void findMovie(final String title) {
        setLoading(true);
        String  apiKey = "7a9f6b43";

        api.findMovie(apiKey, title).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                setLoading(false);
                if (response.isSuccessful()) {
                    MyPreferences.addMovieSearched(MainActivityJava.this, title);

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
                    if(response.body() != null){
                        for(Movie m : response.body()) {
                            addMovie(m);
                        }
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

    void showHistory(String termSearched) {
        List<String> searchedMovies = MyPreferences.getMoviesSearched(MainActivityJava.this);

        List<String> matchedResults = new ArrayList<>();
        if(termSearched == null){
            matchedResults = searchedMovies;
        } else {
            if(searchedMovies == null){
                return;
            }

            for (String searchedMovie : searchedMovies){
                if(searchedMovie.contains(termSearched)){
                    matchedResults.add(searchedMovie);
                }
            }
        }

        if(matchedResults.size() == 0){
            hideHistory();
            return;
        }

        searchedMoviesAdapter = new SearchedMoviesAdapter(this, matchedResults);
        searchRecyclerView.setAdapter(searchedMoviesAdapter);
        searchRecyclerView.setVisibility(View.VISIBLE);
    }

    void hideHistory() {
        searchRecyclerView.setAdapter(null);
        searchRecyclerView.setVisibility(View.GONE);
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

    @Override
    public void itemClicked(@NotNull String title) {
        hideHistory();
        findMovie(title);
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
