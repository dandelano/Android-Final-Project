package com.digitalsolutions.finalproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.digitalsolutions.finalproject.database.DatabaseHandler;
import com.digitalsolutions.finalproject.database.Movie;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Movie mItem;

    /**
     * The UI elements showing the details of the Movie
     */
    private TextView txtTitle;
    private TextView txtDescription;
    private TextView txtWebUrl;
    private ImageView imgCoverImg;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DatabaseHandler.getInstance(getActivity()).getMovie(getArguments().getLong(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            txtTitle = ((TextView) rootView.findViewById(R.id.movie_title));
            txtTitle.setText(mItem.title);

            txtDescription = ((TextView) rootView.findViewById(R.id.movie_description));
            txtDescription.setText(mItem.description);

            txtWebUrl = ((TextView) rootView.findViewById(R.id.movie_url));
            txtWebUrl.setText(mItem.weburl);

            imgCoverImg = ((ImageView) rootView.findViewById(R.id.movie_image));
            // TODO: set the image using picasso
            imgCoverImg.setImageResource(R.drawable.fillerimg);
        }

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        //updateMovieFromUI();
    }

    private void updateMovieFromUI()
    {
        if (mItem != null)
        {
            mItem.title = txtTitle.getText().toString();
            mItem.description = txtDescription.getText().toString();
            mItem.weburl = txtWebUrl.getText().toString();
            //mItem.imagepath = mItem.imagepath;

            DatabaseHandler.getInstance(getActivity()).putMovie(mItem);
        }
    }
}
