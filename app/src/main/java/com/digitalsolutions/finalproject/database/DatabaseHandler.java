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

        String[][] movieTitles =
        {
                {"Big Hero 6",
                 "The special bond that develops between plus-sized inflatable robot Baymax, and prodigy Hiro Hamada, who team up with a group of friends to form a band of high-tech heroes.",
                        "http://image.tmdb.org/t/p/w154/3zQvuSAUdC3mrx9vnSEpkFX0968.jpg",
                        "http://movies.disney.com/big-hero-6"},

                {"Cars",
                        "Lightning McQueen, a hotshot rookie race car driven to succeed, discovers that life is about the journey, not the finish line, when he finds himself unexpectedly detoured in the sleepy Route 66 town of Radiator Springs. On route across the country to the big Piston Cup Championship in California to compete against two seasoned pros, McQueen gets to know the town's offbeat characters.",
                        "http://image.tmdb.org/t/p/w154/5damnMcRFKSjhCirgX3CMa88MBj.jpg",
                        "http://disney.go.com/disneyvideos/animatedfilms/cars/"},

                {"Finding Nemo",
                        "A tale which follows the comedic and eventful journeys of two fish, the fretful Marlin and his young son Nemo, who are separated from each other in the Great Barrier Reef when Nemo is unexpectedly taken from his home and thrust into a fish tank in a dentist's office overlooking Sydney Harbor. Buoyed by the companionship of a friendly but forgetful fish named Dory, the overly cautious Marlin embarks on a dangerous trek and finds himself the unlikely hero of an epic journey to rescue his son.",
                        "http://image.tmdb.org/t/p/w154/zjqInUwldOBa0q07fOyohYCWxWX.jpg",
                        ""},

                {"Frozen",
                        "Young princess Anna of Arendelle dreams about finding true love at her sister Elsa’s coronation. Fate takes her on a dangerous journey in an attempt to end the eternal winter that has fallen over the kingdom. She's accompanied by ice delivery man Kristoff, his reindeer Sven, and snowman Olaf. On an adventure where she will find out what friendship, courage, family, and true love really means.",
                        "http://image.tmdb.org/t/p/w154/jIjdFXKUNtdf1bwqMrhearpyjMj.jpg",
                        "http://movies.disney.com/frozen"},

                {"Ice Age",
                        "With the impending ice age almost upon them, a mismatched trio of prehistoric critters -- Manny the woolly mammoth, Diego the saber-toothed tiger and Sid the giant sloth -- find an orphaned infant and decide to return it to its human parents. Along the way, the unlikely allies become friends. But when enemies attack, their quest takes on far nobler aims..",
                        "http://image.tmdb.org/t/p/w154/zpaQwR0YViPd83bx1e559QyZ35i.jpg",
                        "http://www.iceagemovie.com/original/"},

                {"Megamind",
                        "Bumbling supervillain Megamind finally defeats his nemesis, the superhero Metro Man. But without a hero, he loses all purpose and must find new meaning to his life.",
                        "http://image.tmdb.org/t/p/w154/amXAUSAUrnGuLGEyc1ZNhBvgbnF.jpg",
                        "http://www.megamind.com"},

                {"Mr. Peabody & Sherman",
                        "A young boy and his dog, who happens to have a genius-level IQ, spring into action when their time-travel machine is stolen and moments in history begin to be changed.",
                        "http://image.tmdb.org/t/p/w154/q4FQXK4a4HSb4pajZL4ZFKHVsjC.jpg",
                        ""},

                {"Toy Story",
                        "Woody the cowboy is young Andy’s favorite toy. Yet this changes when Andy get the new super toy Buzz Lightyear for his birthday. Now that Woody is no longer number one he plans his revenge on Buzz. Toy Story is a milestone in film history for being the first feature film to use entirely computer animation.",
                        "http://image.tmdb.org/t/p/w154/agy8DheVu5zpQFbXfAdvYivF2FU.jpg",
                        "http://www.pixar.com/featurefilms/ts/"}
        };

        Movie movie = new Movie();

        for(int i = 0; i < movieTitles.length; i++) {

            movie.title = movieTitles[i][0];
            movie.description = movieTitles[i][1];
            movie.imagepath = movieTitles[i][2];
            movie.weburl = movieTitles[i][3];

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
