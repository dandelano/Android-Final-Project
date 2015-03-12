package com.digitalsolutions.finalproject.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by demont-imac on 3/12/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static DatabaseHandler singleton;

    public static DatabaseHandler getInstance(final Context context) {
        if (singleton == null) {
            singleton = new DatabaseHandler(context);
        }
        return singleton;
    }

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "providerExample";

    private final Context context;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // Good idea to use process context here
        this.context = context.getApplicationContext();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Movie.CREATE_TABLE);
        String imgPath = "/23ljwfe.jpg";
        String movieDescription = "Candy bonbon pastry jujubes lollipop wafer biscuit biscuit. Topping brownie sesame snaps sweet roll pie. Croissant danish biscuit soufflé caramels jujubes jelly. Dragée danish caramels lemon drops dragée. Gummi bears cupcake biscuit tiramisu sugar plum pastry. Dragée gummies applicake pudding liquorice. Donut jujubes oat cake jelly-o. Dessert bear claw chocolate cake gummies lollipop sugar plum ice cream gummies cheesecake.";
        String movieWebUrl = "http://www.google.com";
        String[] movieTitles = {"Mr. Peabody and Sherman",
                "Big Hero 6",
                "Frozen",
                "Toy Story",
                "Toy Story 2",
                "Ice Age",
                "Megamind",
                "Cars",
                "Hotel Transylvania",
                "Hop",
                "Shrek",
                "Cars 2",
                "Finding Nemo",
                "Sharks Tale"};

        Movie movie = new Movie();

        movie.description = movieDescription;
        movie.imagepath = imgPath;
        movie.weburl = movieWebUrl;

        for(int i = 0; i < movieTitles.length; i++) {
            movie.title = movieTitles[i];
            db.insert(Movie.TABLE_NAME,null,movie.getContent());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public synchronized Movie getMovie(final long id) {
        final SQLiteDatabase db = this.getReadableDatabase();
        final Cursor cursor = db.query(Movie.TABLE_NAME, Movie.FIELDS,
                Movie.COL_ID + " IS ?", new String[] { String.valueOf(id) },
                null, null, null, null);
        if (cursor == null || cursor.isAfterLast()) {
            return null;
        }

        Movie item = null;
        if (cursor.moveToFirst()) {
            item = new Movie(cursor);
        }
        cursor.close();
        return item;
    }

    public synchronized boolean putMovie(final Movie movie) {
        boolean success = false;
        int result = 0;
        final SQLiteDatabase db = this.getWritableDatabase();

        if (movie.id > -1) {
            result += db.update(Movie.TABLE_NAME, movie.getContent(),
                    Movie.COL_ID + " IS ?",
                    new String[] { String.valueOf(movie.id) });
        }

        if (result > 0) {
            success = true;
        } else {
            // Update failed or wasn't possible, insert instead
            final long id = db.insert(Movie.TABLE_NAME, null,
                    movie.getContent());

            if (id > -1) {
                movie.id = id;
                success = true;
            }
        }

        if (success) {
            notifyProviderOnMovieChange();
        }

        return success;
    }

    public synchronized int removeMovie(final Movie movie) {
        final SQLiteDatabase db = this.getWritableDatabase();
        final int result = db.delete(Movie.TABLE_NAME,
                Movie.COL_ID + " IS ?",
                new String[] { Long.toString(movie.id) });

        if (result > 0) {
            notifyProviderOnMovieChange();
        }
        return result;
    }

    private void notifyProviderOnMovieChange() {
        context.getContentResolver().notifyChange(
                MovieProvider.URI_MOVIES, null, false);
    }
}
