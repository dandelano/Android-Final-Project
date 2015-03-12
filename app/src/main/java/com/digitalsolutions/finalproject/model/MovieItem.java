package com.digitalsolutions.finalproject.model;

/**
 * Created by demont-imac on 3/11/15.
 */

/**
 * A dummy item representing a piece of content.
 */
public class MovieItem {
    public String id;
    public String image_path;
    public String title;
    public String description;
    public String web_url;


    public MovieItem(String id,String image_path, String title, String description, String web_url) {
        this.id = id;
        this.image_path = image_path;
        this.title = title;
        this.description = description;
        this.web_url = web_url;
    }

    @Override
    public String toString() {
        return title;
    }

}
