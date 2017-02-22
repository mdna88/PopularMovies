package com.narvin.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Custom Adapter for the binding of the movies
 */
public class MoviesAdapter extends ArrayAdapter<Movies> {

    // Public constructor
    public MoviesAdapter(Context context, ArrayList<Movies> objects) {
        super(context, 0, objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check current listItem status
        View gridItem = convertView;
        if (gridItem == null) {
            gridItem = LayoutInflater.from(getContext()).inflate(
                    R.layout.grid_item, parent, false);
        }

        // Gets individual movie item
        Movies currentMovie = getItem(position);

        // Image view reference, the movie poster will be placed here
        final ImageView posterImageView = (ImageView) gridItem.findViewById(R.id.grid_item_imageView);

        // Downloads movie poster image and places in the respective imageView
        Picasso.with(getContext())
                .load(currentMovie.getPosterUrl())
                .error(R.drawable.image_placeholder)
                .into(posterImageView, new Callback() {
                    @Override
                    public void onSuccess() {

                        // Check if image was successfully loaded into imageView,
                        // if not, set image not available placeholder. I Did not use
                        // the Picasso .placeholder() method to avoid displaying the
                        // placeholder while loading in background
                        if (posterImageView.getDrawable() == null) {
                            posterImageView.setImageDrawable(getContext().getResources()
                                    .getDrawable(R.drawable.image_placeholder));
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });


        // Bundle includes the movie info that will be sent to the details activity trough intent
        final Bundle bundle = new Bundle();
        bundle.putString(getContext().getString(R.string.title_key), currentMovie.getTitle());
        bundle.putString(getContext().getString(R.string.overview_key), currentMovie.getOverview());
        bundle.putString(getContext().getString(R.string.release_key), currentMovie.getReleaseDate());
        bundle.putString(getContext().getString(R.string.poster_key), currentMovie.getPosterUrl());
        bundle.putDouble(getContext().getString(R.string.rating_key), currentMovie.getRating());

            gridItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getContext().startActivity(new Intent(
                            getContext(), DetailActivity.class).putExtras(bundle));

                }
            });

        return gridItem;
    }
}
