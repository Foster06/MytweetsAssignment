package com.codepath.apps.mysimpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    // list attributes
    private  String name;
    private  String screeName;
    private  String profileImageUrl;
    private  long uid;
    private String tagline;
    private int followersCount;
    private int followingCount;

    public String getName() {
        return name;
    }

    public String getScreeName() {
        return screeName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public long getUid() {
        return uid;
    }

    public String getTagline() {
        return tagline;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFriendsCount() {
        return followingCount;
    }

    // deserialize the user json => User
    public  static  User fromJSON(JSONObject jsonObject){
        User user =  new User();

        try {
            user.name = jsonObject.getString("name");
            user.uid = jsonObject.getLong("id");
            user.screeName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
            user.tagline = jsonObject.getString("description");
            user.followersCount = jsonObject.getInt("followers_count");
            user.followingCount = jsonObject.getInt("friends_count");


        } catch (JSONException e) {
            e.printStackTrace();
        }


        // return a user
        return user;

    }


}
