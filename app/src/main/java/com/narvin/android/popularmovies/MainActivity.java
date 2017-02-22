package com.narvin.android.popularmovies;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start new instance of fragment if activity is new
        if (savedInstanceState == null) {
            //Set initial Fragment
            MoviesFragment fragment = new MoviesFragment();
            FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
            fragTransaction.replace(R.id.fragment_container, fragment,
                    getString(R.string.movies_tag));
            fragTransaction.commit();

        } else {
            getSupportFragmentManager()
                    .findFragmentByTag(getString(R.string.movies_tag));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));

                break;

            case R.id.action_refresh:
                MoviesFragment fragment = (MoviesFragment) getSupportFragmentManager()
                        .findFragmentByTag(getString(R.string.movies_tag));
                fragment.fetchMoviesFromWeb();

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
