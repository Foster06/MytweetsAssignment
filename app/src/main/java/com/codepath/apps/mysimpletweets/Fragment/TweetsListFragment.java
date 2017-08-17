package com.codepath.apps.mysimpletweets.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Forteson on 11/08/2017.
 */

public class TweetsListFragment extends Fragment {
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;


    // inflate logic
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // create the ArrayList (from data source)
        tweets = new ArrayList<>();
        // construct the adapter from data source
        aTweets = new TweetsArrayAdapter(getActivity(),tweets);
    }

    public void addAll(List<Tweet> tweets){
        aTweets.addAll(tweets);
        aTweets.notifyDataSetChanged();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View f = inflater.inflate(R.layout.fragment_tweet_list, container, false);

        lvTweets = (ListView) f.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);
        //creation lifecycle event
        return f;

    }

}
