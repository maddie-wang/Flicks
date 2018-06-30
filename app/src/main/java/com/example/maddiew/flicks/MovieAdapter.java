package com.example.maddiew.flicks;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.example.maddiew.flicks.models.Config;
import com.example.maddiew.flicks.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

// Creates a MovieAdapter that connects to a recyclerview
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    // list of movies
    public final static String API_BASE_URL = "https://api.themoviedb.org/3"; // base API url
    public final static String API_KEY_PARAM = "api_key";
    ArrayList<Movie> movies;
    Context context; // context for rendering
    Config config;
    AsyncHttpClient client;

    // initialize with list

    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }


    public void setConfig(Config config) {
        this.config = config;
    }

    // create and inflate a new view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // get context and create inflater
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // create view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, viewGroup, false);
        // item_movie is the layout we made w/ movie+title+overview
        return new ViewHolder(movieView);
    }

    // bind view to a new item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        // get movie data at specified position
        final Movie movie = movies.get(i); // get movie data specified position
        viewHolder.tvTitle.setText(movie.getTitle()); // sets viewholder's "title" to movie title
        viewHolder.tvOverview.setText(movie.getOverview());

        // determine the current orientation
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;



        // build url for poster image - combines base url with size and path
        String imageUrl = null;

        // if in portrait mode, load poster image:
        if (isPortrait) {
            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        } else {
            imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
        }

        // get correct placeholder and image
        int placeholderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? viewHolder.ivPosterImage : viewHolder.ivBackdropImage;
        // load image with glide!
        Glide.with(context)
                .load(imageUrl)
                .apply(RequestOptions.placeholderOf(placeholderId))
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(15, 0)))
                .apply(RequestOptions.errorOf(placeholderId))
                .into(imageView);
        // if you click on the image, opens youtube video
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // working on youtube part
                Intent intent = new Intent(context, MovieTrailerActivity.class); // makes intent

                // grab videolink
//
//                String url = API_BASE_URL + "/movie/" + movie.getId() + "/videos"; // create url -- endpoint is videos
//                RequestParams params = new RequestParams();
//                params.put(API_KEY_PARAM, "a07e22bc18f5cb106bfe4cc1f83ad8ed"); // api key always required
//                client.get(url, params, new JsonHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                        try {
//                            JSONArray results = response.getJSONArray("results"); // root node
//                            String video_key = results.getJSONObject(0).getString("key");
//                            Toast.makeText(context, video_key, Toast.LENGTH_SHORT).show();
//
//                        } catch (JSONException e) {
//                            Toast.makeText(context, "failed to retreive video", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                        Toast.makeText(context, "failed to retreive video", Toast.LENGTH_SHORT).show();
//                    }
//                });

                //intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // ^^ serializes movie using parceler. it uses short name as key, movie as value
                context.startActivity(intent); // start activity
            }
        });


    }

    // returns total number of items in list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // create the viewholder as static inner class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // track view objects
        ImageView ivPosterImage;
        ImageView ivBackdropImage;
        TextView tvTitle;
        TextView tvOverview;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // lookup view objects by id
            ivPosterImage = itemView.findViewById(R.id.ivPosterImage);
            ivBackdropImage = itemView.findViewById(R.id.ivBackdropImage);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(this);
        }
        // when the user clicks on a row, show MovieDetailsActivity for the selected movie

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // item pos
            if (position != RecyclerView.NO_POSITION) { // if position is valid
                Movie movie = movies.get(position); // get movie at current position
                Intent intent = new Intent(context, MovieDetailsActivity.class); // makes intent
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // ^^ serializes movie using parceler. it uses short name as key, movie as value
                context.startActivity(intent); // start activity
            }
        }
    }
}
