package com.codepath.apps.mysimpletweets.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.mysimpletweets.R.id.swipeContainer;

/**
 * Created by Forteson on 11/08/2017.
 */

public class HomeTimelineFragment extends TweetsListFragment {
    private TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the client
        client = TwitterApplication.getRestClient(); // Singleton client
        populateTimeline(0);
    }

    //
    //
    //method to populate list tweet

    public void populateTimeline(int oldest){

        client.geHomeTimeLine(oldest,new  JsonHttpResponseHandler(){
            //  SUCCESS

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("DEBUG",response.toString());
                // DESERIALIZE JSON
                // CREATE MODELS
                // LOAD THE MODEL DATA INTO LISTVIEW
                addAll(Tweet.fromJSON(response));
                // Log.d("DEBUG",aTweets.toString());
                // swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("DEBUG",errorResponse.toString());
            }
        });

    }


    }


