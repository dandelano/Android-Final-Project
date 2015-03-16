package com.digitalsolutions.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.digitalsolutions.finalproject.database.DatabaseHandler;
import com.digitalsolutions.finalproject.database.Movie;


/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link MovieListFragment} and the item details
 * (if present) is a {@link MovieDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link MovieListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class MovieListActivity extends FragmentActivity
        implements MovieListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private long selectedId = -1;
    static final int GET_MOVIE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((MovieListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.movie_list))
                    .setActivateOnItemClick(true);


        }


        // TODO: If exposing deep links into your app, handle intents here.
    }

    /**
     * Callback method from {@link MovieListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(long id) {
        if (mTwoPane) {
            // set the id
            selectedId = id;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putLong(MovieDetailFragment.ARG_ITEM_ID, id);
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, MovieDetailActivity.class);
            detailIntent.putExtra(MovieDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case R.id.newMovie:
                // Create a new movie.
                //TODO: start new activity for result
                startActivityForResult(new Intent(MovieListActivity.this, GetMovieActivity.class), GET_MOVIE_REQUEST);
                //Movie m_add = new Movie();
                //DatabaseHandler.getInstance(this).putMovie(m_add);
                // Open a new fragment with the new id
                //onItemSelected(m_add.id);
                return true;
            case R.id.deleteMovie:
                if (selectedId != -1) {
                    // TODO: Fix the delete method!!! Not actually deleting the movie data.(Issue in detail frag, was inserting new movie)
                    // TODO: put a confirmation before delete
                    Movie m_delete = DatabaseHandler.getInstance(this).getMovie(selectedId);
                    DatabaseHandler.getInstance(this).removeMovie(m_delete);
                    selectedId = -1;
                    onItemSelected(selectedId);
                } else {
                    Toast.makeText(this, "No movie selected", Toast.LENGTH_SHORT).show();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode)
        {
            case (GET_MOVIE_REQUEST):
            {
                if(resultCode == Activity.RESULT_OK)
                {
                    // TODO: Extract movie data returned from the child activity.
                    Movie m_add = new Movie();
                    DatabaseHandler.getInstance(this).putMovie(m_add);
                    // Open a new fragment with the new id
                    onItemSelected(m_add.id);
                }
                break;
            }
        }
    }
}
