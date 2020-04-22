package com.example.mynewsapplication;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.AsyncTaskLoader;

import java.util.List;

public class MyNewsLoader extends AsyncTaskLoader<List<MyNews>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = MyNewsLoader.class.getSimpleName();

    /**
     * Query URL
     */
    private String mUrl;


    /**
     * Constructs a new {@link MyNewsLoader}
     *
     * @param context
     * @param url
     */
    public MyNewsLoader(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        //System.out.println("Enter onStartLoading");
        forceLoad();
    }

    /**
     * This is on a background thread.
     *
     * @return
     */
    @Nullable
    @Override
    public List<MyNews> loadInBackground() {
        if (mUrl == null) {
            //System.out.println("URL is null");
            return null;
        }

        //Perform the network request, parse the response,
        // and extract a list of earthquakes.

        List<MyNews> myNews = QueryUtils.fetchMyNewsData(mUrl);
        //System.out.println("Fetched the news data and we got");
        return myNews;
    }
}
