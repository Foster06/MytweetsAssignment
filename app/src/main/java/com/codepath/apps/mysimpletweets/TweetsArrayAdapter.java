package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


/**
 * Created by Owner on 8/3/2017.
 */

public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context,android.R.layout.simple_list_item_1,tweets);
    }


    //Override and setup_custom template
    // ViewHolder pattern
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // 1. Get the tweet
        Tweet tweet = this.getItem(position);
        // 2. find or inflate the template
        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
        // 3. find the subviews to fill with data the template
        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        ivProfileImage.setImageResource(0);
        ImageView ivTweet = (ImageView) convertView.findViewById(R.id.ivTweet);
        ivTweet.setImageResource(0);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView relativeTimestamp = (TextView) convertView.findViewById(R.id.tvRelativeTimestamp);
        relativeTimestamp.setText(getRelativeTimeAgo(tweet.getCreateAt()));
        // 4. Populate data into the subviews
        tvUserName.setText(tweet.getUser().getScreeName());
        tvBody.setText(tweet.getBody());
        ivProfileImage.setImageResource(android.R.color.transparent);
        // clear out the image for recycled view
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).transform(new RoundedCornersTransformation(3, 3)).into(ivProfileImage);

        //tweet.getUser().getScreeName();
        //tweet.getMediaUrl();
        if(tweet.getMediaUrl().size()>0) {
            if(!TextUtils.isEmpty(tweet.getMediaUrl().get(0))) {
                Picasso.with(getContext()).load(tweet.getMediaUrl().get(0)).transform(new RoundedCornersTransformation(10,10)).into(ivTweet);
                tweet.getMediaUrl().clear();
            }
        }
        // 5. Return the view to be inserted into the list
        return convertView;


        // Override and setup custom template
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

}
