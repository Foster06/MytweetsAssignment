package com.codepath.apps.mysimpletweets;


import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget   .SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.Fragment.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.Fragment.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweets.Fragment.TweetsListFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.mysimpletweets.R.id.lvTweets;

public class TimelineActivity extends AppCompatActivity {

    private TweetsListFragment tweetsListFragment;
    private TwitterClient client;
    FragmentTweet fragmentTweet;
    FragmentManager fragmentManager;
    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Get the viewpager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        // Set the viewpager for the pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        // Find the sliding tabstrip
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        //Attach the tabsrtip to the viewpager
       tabStrip.setViewPager(vpPager);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_twitter);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4099ff")));

        fragmentManager = getFragmentManager();
        fragmentTweet = new FragmentTweet();
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);


        // Get the Client
        client = TwitterApplication.getRestClient(); // singleton client
        populateTimeline(0);
        if(savedInstanceState == null) {

            tweetsListFragment = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.lvTweets);

        }


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                loadNextDataFromApi(0);

            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }


    public void onTweet (View view){
        fragmentTweet.show(fragmentManager,"Tweet");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline_menu,menu);
        return true;
    }

    public void onMenu(MenuItem item) {

        fragmentTweet.show(fragmentManager,"Tweet");

    }

    private void loadNextDataFromApi(int page) {
        client.onloadNext(page,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
               // aTweets.clear();

                Log.d("DEBUG",response.toString());
                // DESERIALIZE JSON
                // CREATE MODELS
                // LOAD THE MODEL DATA INTO LISTVIEW
                //aTweets.addAll(Tweet.fromJSON(response));
                //Log.d("DEBUG",aTweets.toString());

                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
               // Log.d("DEBUG",errorResponse.toString());
            }
        });

    }

    // Send an API request to get timeline json
    // Fill the listView by creating the tweet objects from json


    private void populateTimeline(int oldest) {
        client.geHomeTimeLine(oldest,new  JsonHttpResponseHandler(){
            //  SUCCESS

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("DEBUG",response.toString());
                // DESERIALIZE JSON
                // CREATE MODELS
                // LOAD THE MODEL DATA INTO LISTVIEW
                tweetsListFragment.addAll(Tweet.fromJSON(response));
                Log.d("DEBUG",tweetsListFragment.toString());
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("DEBUG",errorResponse.toString());
            }
        });
    }

    public void onProfileView(MenuItem item) {
        // Launch the profile view
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    // Return the order of the fragment in the view pager
            public class TweetsPagerAdapter extends FragmentPagerAdapter{
                final int PAGE_COUNT = 2;
                private String tabTitles[] = {"Home", "Mentions"};

               // Adapter gets the manager insert or remove fragment from activity
                public TweetsPagerAdapter(android.support.v4.app.FragmentManager fm){
                    super(fm);
                }

                // The order and creation of fragment within the pager
                @Override
                public Fragment getItem(int position) {
                    if (position == 0) {
                        return new  HomeTimelineFragment();
                    } else if (position == 1){
                        return new MentionsTimelineFragment();
                    } else {
                        return null;
                    }

                }

                // Return the tab title
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
        // How many fragments there are to swipe between ?
        @Override
                public int getCount() {
                    return tabTitles.length;
                }
            }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
