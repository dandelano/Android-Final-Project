package com.digitalsolutions.finalproject;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.config.TmdbConfiguration;


public class MainActivity extends ActionBarActivity {
    ImageView movieCoverImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnViewLibrary = (Button) findViewById(R.id.btnViewLibrary);
        Button btnAddMovie = (Button) findViewById(R.id.btnAddMovie);
        movieCoverImg = (ImageView)findViewById(R.id.movieImg);
        btnViewLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MovieListActivity.class));
            }
        });

        btnAddMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    new GetMovieTask().execute("");
                } else {
                    Toast.makeText(MainActivity.this, "No Network connection available", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private class GetMovieTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                TmdbApi tmdbApi = new TmdbApi("17e38ce29cdddeefdaf66d281a0f087f");
                TmdbConfiguration config = tmdbApi.getConfiguration();


                /*TmdbSearch tmdbSearch = tmdbApi.getSearch();
                MovieResultsPage movieResultsPage = tmdbSearch.searchMovie("Big Hero 6", 0, "en", false, 0);
                MovieDb movie;
                if(movieResultsPage.getTotalResults() > 0)
                {
                    List<MovieDb> movieDbsList = movieResultsPage.getResults();
                    movie = movieDbsList.get(0);
                    Log.d("MovieTask",movie.getOverview());
                }*/
                TmdbMovies movies = tmdbApi.getMovies();
                MovieDb movie = movies.getMovie(177572, "en");
                String imgUrl = config.getBaseUrl() + "w154" + movie.getPosterPath();

                Log.d("MovieTask",movie.getTitle());
                Log.d("MovieTask",movie.getOverview());
                return imgUrl;
            } catch (Exception e) {
                Log.d("MovieTask",e.toString());
                return "Unable to get movies";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Picasso.with(getApplicationContext()).load(result).into(movieCoverImg);
            Log.d("MovieTask",result);
        }
    }

}
