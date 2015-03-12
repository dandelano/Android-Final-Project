package com.digitalsolutions.finalproject.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class MovieContent {
    private String imgPath = "/23ljwfe.jpg";
    private String movieDescription = "Candy bonbon pastry jujubes lollipop wafer biscuit biscuit. Topping brownie sesame snaps sweet roll pie. Croissant danish biscuit soufflé caramels jujubes jelly. Dragée danish caramels lemon drops dragée. Gummi bears cupcake biscuit tiramisu sugar plum pastry. Dragée gummies applicake pudding liquorice. Donut jujubes oat cake jelly-o. Dessert bear claw chocolate cake gummies lollipop sugar plum ice cream gummies cheesecake.";
    private String movieWebUrl = "http://www.google.com";
    private String[] movieTitles = {"Mr. Peabody and Sherman",
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

    /**
     * An array of sample (dummy) items.
     */
    private ArrayList<MovieItem> movieArray = new ArrayList<MovieItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    private Map<String, MovieItem> ITEM_MAP = new HashMap<String, MovieItem>();

    public MovieContent()
    {
        for(int i = 0; i < movieTitles.length; i++) {
            addItem(new MovieItem(""+i, imgPath, movieTitles[i], movieDescription + i, movieWebUrl));
        }
    }

    public ArrayList<MovieItem> getMovies()
    {
        return movieArray;
    }

    private void addItem(MovieItem item) {
        movieArray.add(item);
        ITEM_MAP.put(item.id, item);
    }


}
