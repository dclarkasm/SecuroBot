package com.unhcfreg.securobot;

import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by Devon on 6/13/2015.
 */
public class Tweet {
    String tweetBy;
    String tweet;
    Status status;

    public static final String HASHTAG_QUIZ = "#securobotquiz";
    public static final String HASHTAG_TIP = "#securobottip";
    public static final String HASHTAG_ARTICLE = "#securobotarticle";
    public static final String HASHTAG_RSSFEED = "#securobotrssfeed";

    public Tweet(Status status) {
        this.status = status;
        this.tweetBy = status.getUser().getScreenName();
        this.tweet = status.getText();
    }

    public String getTweetBy() {
        return tweetBy;
    }

    public void setTweetBy(String tweetBy) {
        this.tweetBy = tweetBy;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public void parseTweet() {

    }
}


