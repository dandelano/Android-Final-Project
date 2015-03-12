package com.digitalsolutions.finalproject.database;

/**
 * Created by demont-imac on 3/12/15.
 */

import android.content.ContentValues;
import android.database.Cursor;

/**
 * A class representation of a row in table "Movie".
 */
public class Movie {

    // SQL convention says Table name should be "singular", so not Movies
    public static final String TABLE_NAME = "Movie";
    // Naming the id column with an underscore is good to be consistent
    // with other Android things. This is ALWAYS needed
    public static final String COL_ID = "_id";
    // These fields can be anything you want.
    public static final String COL_TITLE = "title";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_IMAGEPATH = "image_path";
    public static final String COL_WEBURL = "web_url";

    // For database projection so order is consistent
    public static final String[] FIELDS = { COL_ID, COL_TITLE, COL_DESCRIPTION,COL_IMAGEPATH, COL_WEBURL };

    /*
     * The SQL code that creates a Table for storing Movies in.
     * Note that the last row does NOT end in a comma like the others.
     * This is a common source of error.
     */
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COL_ID + " INTEGER PRIMARY KEY,"
                    + COL_TITLE + " TEXT NOT NULL DEFAULT '',"
                    + COL_DESCRIPTION + " TEXT NOT NULL DEFAULT '',"
                    + COL_IMAGEPATH + " TEXT NOT NULL DEFAULT '',"
                    + COL_WEBURL + " TEXT NOT NULL DEFAULT ''"
                    + ")";

    // Fields corresponding to database columns
    public long id = -1;
    public String title = "";
    public String description = "";
    public String imagepath = "";
    public String weburl = "";

    /**
     * No need to do anything, fields are already set to default values above
     */
    public Movie() {
    }

    /**
     * Convert information from the database into a Person object.
     */
    public Movie(final Cursor cursor) {
        // Indices expected to match order in FIELDS!
        this.id = cursor.getLong(0);
        this.title = cursor.getString(1);
        this.description = cursor.getString(2);
        this.imagepath = cursor.getString(3);
        this.weburl = cursor.getString(4);
    }

    /**
     * Return the fields in a ContentValues object, suitable for insertion
     * into the database.
     */
    public ContentValues getContent() {
        final ContentValues values = new ContentValues();
        // Note that ID is NOT included here
        values.put(COL_TITLE, title);
        values.put(COL_DESCRIPTION, description);
        values.put(COL_IMAGEPATH, imagepath);
        values.put(COL_WEBURL, weburl);

        return values;
    }
}
