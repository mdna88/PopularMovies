package com.narvin.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private ImageView posterImageview;
    private TextView titleView;
    private TextView releaseView;
    private TextView ratingView;
    private TextView overViewView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Reference to each View that will display some details
        posterImageview = (ImageView) findViewById(R.id.detail_poster);
        titleView = (TextView) findViewById(R.id.detail_title);
        releaseView = (TextView) findViewById(R.id.detail_release);
        ratingView = (TextView) findViewById(R.id.detail_rating);
        overViewView = (TextView) findViewById(R.id.detail_overview);

        // Bundle contains the specific movie details
        Bundle bundle = getIntent().getExtras();

        // Download and display image
        Picasso.with(this).load(bundle.getString(getString(R.string.poster_key)))
                .into(posterImageview);

        titleView.setText(bundle.getString(getString(R.string.title_key)));

        releaseView.setText(getString(R.string.release_view) +
                bundle.getString(getString(R.string.release_key)));

        ratingView.setText(getString(R.string.rating_view) +
                bundle.getDouble(getString(R.string.rating_key)));

        overViewView.setText(bundle.getString(getString(R.string.overview_key)));

    }
}
