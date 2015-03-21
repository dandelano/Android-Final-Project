package com.digitalsolutions.finalproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.config.TmdbConfiguration;


public class GetMovieActivity extends Activity {
    private final static String API_KEY = "17e38ce29cdddeefdaf66d281a0f087f";
    private static final String HTTPS = "https://";
    private static final String HTTP = "http://";

    // COL_TITLE, COL_DESCRIPTION,COL_IMAGEPATH, COL_WEBURL
    protected ImageView imgCover;
    protected TextView title;
    protected TextView description;
    protected TextView url;
    protected Button btnSave;
    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Black);
        setContentView(R.layout.activity_get_movie);

        // get the result containers
        imgCover = (ImageView)findViewById(R.id.imgNewCover);
        title = (TextView)findViewById(R.id.txtNewTitle);
        description = (TextView)findViewById(R.id.txtNewDescription);
        url = (TextView)findViewById(R.id.txtNewUrl);

        // Get the buttons
        Button btnSearch = (Button)findViewById(R.id.btnSearch);
        btnSave = (Button)findViewById(R.id.btnSaveMovie);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Searching");
        progressDialog.setMessage("Wait while I find the movie...");

        // Search for the title of movie entered into the text field
        btnSearch.setOnClickListener(new View.OnClickListener() {
            EditText searchString = (EditText)findViewById(R.id.txtSearchMovie);
            @Override
            public void onClick(View v) {
                String searchTitle = searchString.getText().toString();
                if(searchTitle.length() > 0)
                {
                    hideKeyboard();
                    progressDialog.show();
                    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        new GetMovieTask().execute("");
                    } else {
                        Toast.makeText(GetMovieActivity.this, "No Network connection available", Toast.LENGTH_LONG).show();
                    }
                }else
                {
                    Toast.makeText(GetMovieActivity.this, "Must enter a title to search", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Save the movie if one was found.
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                // TODO Add extras or a data URI to this intent as appropriate.
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlText = url.getText().toString();
                if(urlText.length() > 0)
                {
                    if (!urlText.startsWith(HTTP) && !urlText.startsWith(HTTPS)) {
                        urlText = HTTP + urlText;
                    }

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlText));
                    startActivity(Intent.createChooser(intent, "Chose browser"));
                }
            }
        });
    }


    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    protected class MovieContainer
    {
        public String mImgUrl = "";
        public String mTitle = "";
        public String mDescription = "";
        public String mWebUrl = "";
        public MovieContainer(String img, String title, String desc, String url)
        {
            mImgUrl = img;
            mTitle = title;
            mDescription = desc;
            mWebUrl = url;
        }
    }

    private class GetMovieTask extends AsyncTask<String, Void, MovieContainer> {
        @Override
        protected MovieContainer doInBackground(String... urls) {
            try {
                TmdbApi tmdbApi = new TmdbApi(API_KEY);
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

                // Put the returned movie details in a container to pass
                return new MovieContainer(imgUrl,movie.getTitle(),movie.getOverview(),movie.getHomepage());
            } catch (Exception e) {
                Log.d("MovieTask",e.toString());
                return new MovieContainer("","","","");
            }
        }

        @Override
        protected void onPostExecute(MovieContainer result) {
            if(result != null)
            {
                Boolean isCompleteMovie = true;
                // Check if image is set
                if(result.mImgUrl.length() > 0) {
                    Picasso.with(getApplicationContext()).load(result.mImgUrl).into(imgCover);
                    imgCover.setVisibility(View.VISIBLE);
                }else
                {
                    isCompleteMovie = false;
                }

                // Check if title is set
                if(result.mTitle.length() > 0)
                {
                    title.setText(result.mTitle);
                    title.setVisibility(View.VISIBLE);
                }else
                {
                    isCompleteMovie = false;
                }
                // Check if description is set
                if(result.mDescription.length() > 0)
                {
                    description.setText(result.mDescription);
                    description.setVisibility(View.VISIBLE);
                }else
                {
                    isCompleteMovie = false;
                }
                // Check if url is set
                if(result.mWebUrl.length() > 0)
                {
                    url.setText(result.mWebUrl);
                    url.setVisibility(View.VISIBLE);
                }else
                {
                    isCompleteMovie = false;
                }

                if(isCompleteMovie)
                    btnSave.setVisibility(View.VISIBLE);
            }
            // Dismiss spinner dialog
            progressDialog.dismiss();
        }
    }
}
