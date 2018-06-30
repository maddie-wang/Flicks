package com.example.maddiew.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {

    String imageBaseUrl; // base url for loading images
    String posterSize; // size of poster images
    String backdropSize; // backdrop

    public Config(JSONObject object) throws JSONException {

        JSONObject images = object.getJSONObject("images");
        // get image based url. ex: secure_base_url": "https://image.tmdb.org/t/p/"
        imageBaseUrl = images.getString("secure_base_url");
        // get poster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        // use option at index 3 or w342
        posterSize = posterSizeOptions.optString(3, "w342");
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOptions.optString(5, "w780");
    }
    // helper for creating urls
    public String getImageUrl(String size, String path) {
        return String.format("%s%s%s", imageBaseUrl, size, path); // concact all 3
    }
    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }

    public String getBackdropSize() {
        return backdropSize;
    }
}
