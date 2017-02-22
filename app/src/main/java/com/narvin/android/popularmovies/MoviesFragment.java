package com.narvin.android.popularmovies;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.attr.data;


/**
 * Movies fragment that displays a gridView of movies fetched from the MoviesDB WEB API
 */
public class MoviesFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<ArrayList<Movies>> {

    private TextView emptyView;

    // Required empty public constructor
    public MoviesFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        emptyView = (TextView) getActivity().findViewById(R.id.empty_element);
        fetchMoviesFromWeb();
    }

    // Starts the Movies Loader
    public void fetchMoviesFromWeb() {
        if (isOnline()) {
            getActivity().getSupportLoaderManager().initLoader(1, null, this).forceLoad();

        } else {
            emptyView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public Loader<ArrayList<Movies>> onCreateLoader(int id, Bundle args) {

        return new MoviesLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movies>> loader, ArrayList<Movies> data) {
        updateDisplay(data);
        getLoaderManager().destroyLoader(loader.getId());

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movies>> loader) {
        updateDisplay(new ArrayList<Movies>());

    }

    @Override
    public void onResume() {
        super.onResume();
        fetchMoviesFromWeb();
    }

    // Updates ListView with extracted info from the Loader
    protected void updateDisplay(final ArrayList<Movies> moviesList) {

        if (moviesList != null) {

            // Initialize adapter if Activity has been fully created
            if (getActivity() != null) {

                MoviesAdapter adapter = new MoviesAdapter(getActivity(), moviesList);
                final GridView gridView = (GridView) getActivity().findViewById(R.id.movie_gridView);
                gridView.setEmptyView(emptyView);
                gridView.setAdapter(adapter);
            }
            emptyView.setVisibility(View.GONE);

        } else {
            emptyView.setVisibility(View.VISIBLE);
        }

    }

    // Checks internet connectivity.
    protected boolean isOnline() {

        ConnectivityManager connectManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = connectManager.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

}
