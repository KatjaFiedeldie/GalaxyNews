package com.example.android.galaxynews;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Galaxy>>{

    /** Adapter for the list of Galaxies */
    private GalaxyAdapter mAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    // To test and create messages!
    public static final String LOG_TAG = MainActivity.class.getName();

    private static final int GALAXY_LOADER_ID = 1;

    private static final String QUERY_API_KEY_TAG = "api-key";
    private static final String API_KEY_VALUE = "e8756a70-532c-4cdb-970c-65a5c7ee929d";


    /** URL for news data from the Guardian data set */
    private static final String GUARDIAN_URL =
            "https://content.guardianapis.com/search?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        ListView galaxyListView = findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of galaxies
        mAdapter = new GalaxyAdapter(this, new ArrayList <Galaxy>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        galaxyListView.setAdapter(mAdapter);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        galaxyListView.setEmptyView(mEmptyStateTextView);

        galaxyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current galaxy that was clicked on
                Galaxy currentGalaxy = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri galaxyUri = Uri.parse(currentGalaxy.getUrl());

                // Create a new intent to view the galaxy URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, galaxyUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            getLoaderManager().initLoader(1, null, this).forceLoad();
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(GALAXY_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Galaxy>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.

        String date = preferences.getString(
                getString(R.string.date_list_key),getString(R.string.date_list_default_value));

        String order = preferences.getString(
                getString(R.string.order_list_key),getString(R.string.order_list_default_value));

        // parse breaks apart the URI string that's passed into its parameter
        Uri uri = Uri.parse(GUARDIAN_URL);
        Uri.Builder uriBuilder = uri.buildUpon();

        // Append query parameter and its value. For example, the`section=science`
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("section", "science");
        uriBuilder.appendQueryParameter("from-date", date);
        uriBuilder.appendQueryParameter("q", "galaxy");
        uriBuilder.appendQueryParameter("order-by", order);
        uriBuilder.appendQueryParameter(QUERY_API_KEY_TAG, API_KEY_VALUE);

        // execute GalaxyLoader https://content.guardianapis.com/search?show-tags=contributor&section=science&from-date=2018-01-01&q=%27galaxy%27&api-key=test
        return new GalaxyLoader(this , uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Galaxy>> loader, List<Galaxy> galaxies) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        // Set empty state text to display "No galaxies found."
        mEmptyStateTextView.setText(R.string.no_galaxies);
        // Clear the adapter of previous galaxy data
        mAdapter.clear();

        // If there is a valid list of {@link galaxy), then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (galaxies != null && !galaxies.isEmpty()) {
            mAdapter.addAll(galaxies);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Galaxy>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    }
