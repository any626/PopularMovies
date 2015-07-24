package com.example.alejandro.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, String[]>{

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {

            final String defaultSort = "popularity.desc";

            if(params.length == 0){
                params[0] = defaultSort;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonString = null;

            try{
                Uri builtUri = Uri.parse(getString(R.string.movies_api_base)).buildUpon()
                        .appendQueryParameter(getString(R.string.movies_api_key_param), getString(R.string.movies_api_key))
                        .appendQueryParameter(getString(R.string.movies_sort_by_param), params[0]).build();
                URL url = new URL(builtUri.toString());

                //create request to api
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //read the input stream into string.
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if(inputStream == null){
                    //nothing to do
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = reader.readLine()) != null){
                    buffer.append(line).append("\n");
                }
                if(buffer.length() == 0){
                    return null;
                }

                moviesJsonString = buffer.toString();
            } catch (IOException e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }
    }
}
