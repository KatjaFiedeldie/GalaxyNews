package com.example.android.galaxynews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class GalaxyLoader extends AsyncTaskLoader<List<Galaxy>> {

    /** Tag for log messages */
    private static final String LOG_TAG = GalaxyLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link GalaxyLoader}.
     *
     * @param context of the activity
     * @param url to load data from galaxy
     **/

    public GalaxyLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Galaxy> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of galaxies.
        List<Galaxy> galaxies = QueryUtils.fetchGalaxyData(mUrl);
        return galaxies;
    }

}
