package com.codepath.apps.mysimpletweets;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Owner on 8/4/2017.
 */

public class FragmentTweet extends DialogFragment {
    FragmentManager fragmentManager;
    Button btnTweet;
    EditText etTweet;
    TextView tvCounter;
    ImageView ivClear;
    private TwitterClient client;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_layout,null);
        fragmentManager = getFragmentManager();
        tvCounter = (TextView) view.findViewById(R.id.tvCounter);
        etTweet = (EditText) view.findViewById(R.id.etTweet);

        client = TwitterApplication.getRestClient(); // singleton client

        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                        tvCounter.setText(String.valueOf(140 - etTweet.getText().length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnTweet = (Button) view.findViewById(R.id.btnTweet);
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendTweet( etTweet.getText().toString());

                dismiss();
            }
        });

        ivClear = (ImageView) view.findViewById(R.id.ivClear);
        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;

    }



    public void sendTweet(String body){

        client.postTweet(body,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("DEBUG", response.toString());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                if(errorResponse != null)
                    Log.d("DEBUG", errorResponse.toString());

                if(throwable != null)
                    throwable.printStackTrace();
            }
        });

    }


}
