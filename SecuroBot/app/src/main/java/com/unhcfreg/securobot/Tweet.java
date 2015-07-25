package com.unhcfreg.securobot;

import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import twitter4j.HashtagEntity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by Devon on 6/13/2015.
 */
public class Tweet {
    public static final String HASHTAG_QUIZ = "securobotquiz";
    public static final String HASHTAG_TIP = "securobottip";
    public static final String HASHTAG_JOKE = "securobotjoke";
    public static final String HASHTAG_ARTICLE = "securobotarticle";
    public static final String HASHTAG_RSSFEED = "securobotrssfeed";
    public static final String TWITTER_RT = "RT";

    public static final int UNKNOWN = 0;
    public static final int SECUROBOT_QUIZ = 1;
    public static final int SECUROBOT_TIP = 2;
    public static final int SECUROBOT_JOKE = 3;
    public static final int SECUROBOT_ARTICLE = 4;
    public static final int SECUROBOT_RSSFEED = 5;
    public static final int SECUROBOT_RT = 6;

    private String tweetBy;
    private String tweet;
    private Status status;
    private String parsedContent;
    private int contentType = UNKNOWN;
    HashtagEntity hashtags [];
    private ArrayList<String> URLs = new ArrayList<String>();


    public Tweet(Status status) {
        this.status = status;
        this.tweetBy = status.getUser().getScreenName();
        this.tweet = status.getText();

        parseTweet();
    }

    public String getTweetBy() {
        return tweetBy;
    }

    public String getTweet() {
        return tweet;
    }

    private void parseTweet() {
        Log.d("TweetParser", "Original Tweet:\n" + "Tweet By: " + tweetBy +
        "\nTweet content: " + tweet);

        if(status.isRetweet()){
            Status nStatus = status.getRetweetedStatus();
            contentType = SECUROBOT_RT;
            parsedContent = nStatus.getText();
            Log.d("TweetParser", "Found retweeted status!\n" + nStatus.getText());
        }
        else {
            hashtags = status.getHashtagEntities();
            Log.d("TweetParser", "Hashtags:");
            for(HashtagEntity ht : hashtags) {
                Log.d("TweetParser", ht.getText());
                contentType = getTweetType();
                parsedContent = removeHashtags(status);
            }
        }
    }

    private String removeHashtags(Status withTags) {
        String noTags;

        //grab just the link if one of the below types
        if(contentType == SECUROBOT_QUIZ ||
                contentType == SECUROBOT_RSSFEED ||
                contentType == SECUROBOT_ARTICLE) {
            for(URLEntity l : status.getURLEntities()) {
                URLs.add(l.getURL());
                Log.d("Parsed URL", l.getURL());
            }
            noTags = URLs.get(0);
        }
        else noTags = status.getText();
        return noTags;
    }
/*
    public String getContentByType(final int contentType) {
        switch(contentType){
            case SECUROBOT_QUIZ: return parsedContent;
            case SECUROBOT_TIP: return parsedContent;
            case SECUROBOT_JOKE: return parsedContent;
            case SECUROBOT_ARTICLE: return parsedContent;
            case SECUROBOT_RSSFEED: return parsedContent;
            case SECUROBOT_RT: return parsedContent;
            case UNKNOWN: return null;
            default: return null;
        }
    }
*/
    public String getContent() {
        return parsedContent;
    }

    private int getTweetType() {
        for(HashtagEntity ht : hashtags) {
            switch(ht.getText()) {
                case HASHTAG_QUIZ: return SECUROBOT_QUIZ;
                case HASHTAG_TIP: return SECUROBOT_TIP;
                case HASHTAG_JOKE: return SECUROBOT_JOKE;
                case HASHTAG_ARTICLE: return SECUROBOT_ARTICLE;
                case HASHTAG_RSSFEED: return SECUROBOT_RSSFEED;
                default: break;
            }
        }

        return UNKNOWN;
    }

    public int getContentType() {
        return contentType;
    }
}