package com.example.maddiew.flicks.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel // makes class parcelable
public class Movie {
    // collect values from API. All must be public for parcel
    Double voteAverage;
    String title;
    Integer id;
    String overview;
    String posterPath; // only path
    String backdropPath;
    // no-arg, empty constructor required for Parceler
    public Movie() {}

    // initialize from JSON data
    // So given the JSON Object, it extracts the objects shown in that JSON response {}
    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
        voteAverage = object.getDouble("vote_average");
        id = object.getInt("id");
    }

    // getters help our Activity grab information from the movie object

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public Integer getId() {
        return id;
    }
}
