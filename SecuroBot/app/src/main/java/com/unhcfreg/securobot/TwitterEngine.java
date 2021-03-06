package com.unhcfreg.securobot;

import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.auth.RequestToken;
import twitter4j.auth.AccessToken;
import java.io.InputStreamReader;

import java.io.BufferedReader;

/**
 * Created by Devon on 7/5/2015.
 */
public class TwitterEngine {
    public TTSEngine engine;
    private ArrayList<String> parsedReTweets = new ArrayList<String>(); //list of the most recent retweets for speaking (parsed)
    private ArrayList<String> parsedTips = new ArrayList<String>();
    private ArrayList<String> parsedQuizLinks = new ArrayList<String>();
    private ArrayList<String> parsedRSSLinks = new ArrayList<String>();
    private ArrayList<String> parsedJokes = new ArrayList<String>();
    private ArrayList<String> parsedArticleLinks = new ArrayList<String>();
    private ArrayList<Tweet> parsedRandTweets = new ArrayList<Tweet>(); //list of the most recent random tweets for speaking (parsed)
    private ArrayList<Tweet> parsedStatuses = new ArrayList<Tweet>(); //list of the most recent status updates for speaking (parsed)
    Twitter twitter;
    private static final String TWITTER_KEY = "JlxXwwVxSH8KuiqIktrNE2VQp";
    private static final String TWITTER_SECRET = "4m1kuoWKOrHDLX7CulAs6uAzEKpjFUWUkweWFunQCXlZCVpGXm";
    private static final String TWITTER_TOKEN = "3364737443-ilf4qCoDyaKcsD5fZME80qGpwmfMiv1yDgMaoJM";
    private static final String TOKEN_SECRET = "YLZbvOwFOSa6akO50Pur3aTS059QTl5qUL4c8BScwHKA6";

    public TwitterEngine() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(TWITTER_KEY)
                .setOAuthConsumerSecret(TWITTER_SECRET)
                .setOAuthAccessToken(TWITTER_TOKEN)
                .setOAuthAccessTokenSecret(TOKEN_SECRET);
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }

    public void searchOnTwitter(String text) {  //for searching for random things on twitter
        try {
            Query query = new Query(text);
            QueryResult result;
            result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            //The latest tweet is in the first spot in the list
            Log.d("Twitter", "@" + tweets.get(0).getUser().getScreenName() + " - " + tweets.get(0).getText());
            for(int i=0; i<10 && i<tweets.size(); i++) {
                parsedRandTweets.add(new Tweet(tweets.get(i)));
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            Log.d("Twitter", "Failed to search tweets: " + te.getMessage());
        }
    }

    public void getTimeline() {
        try {
            User user = twitter.verifyCredentials();
            List<Status> statuses = twitter.getHomeTimeline();
            Log.d("Twitter", "Showing @" + user.getScreenName() + "'s home timeline.");
            for (Status status : statuses) {
                Log.d("Twitter", "@" + status.getUser().getScreenName() + " - " + status.getText());
                Tweet newTweet = new Tweet(status);
                parsedStatuses.add(newTweet);
                switch(newTweet.getContentType()) {
                    case Tweet.SECUROBOT_ARTICLE:
                        if(!parsedArticleLinks.contains(newTweet.getContent())) {
                            parsedArticleLinks.add(newTweet.getContent());
                        }
                        break;
                    case Tweet.SECUROBOT_JOKE:
                        if(!parsedJokes.contains(newTweet.getContent())){
                            parsedJokes.add(newTweet.getContent());
                        }
                        break;
                    case Tweet.SECUROBOT_QUIZ:
                        if(!parsedQuizLinks.contains(newTweet.getContent())) {
                            parsedQuizLinks.add(newTweet.getContent());
                        }
                        break;
                    case Tweet.SECUROBOT_RSSFEED:
                        if(!parsedRSSLinks.contains(newTweet.getContent())) {
                            parsedRSSLinks.add(newTweet.getContent());
                        }
                        break;
                    case Tweet.SECUROBOT_TIP:
                        if(!parsedTips.contains(newTweet.getContent())) {
                            parsedTips.add(newTweet.getContent());
                        }
                        break;
                    case Tweet.SECUROBOT_RT:
                        if(!parsedReTweets.contains(newTweet.getTweetBy() + " says " + newTweet.getContent())) {
                            parsedReTweets.add(newTweet.getTweetBy() + " says " + newTweet.getContent());
                        }
                        break;
                    default: break;
                }
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            Log.d("Twitter", "Failed to get timeline: " + te.getMessage());
            return;
        }
    }

    public ArrayList<String> getContent(final int contentType) {
        switch(contentType) {
            case Tweet.SECUROBOT_ARTICLE: return parsedArticleLinks;
            case Tweet.SECUROBOT_JOKE: return parsedJokes;
            case Tweet.SECUROBOT_QUIZ: return parsedQuizLinks;
            case Tweet.SECUROBOT_RSSFEED: return parsedRSSLinks;
            case Tweet.SECUROBOT_TIP: return parsedTips;
            default: break;
        }
        return null;
    }

    public void setTTSEngine(TTSEngine e) {
        engine = e;
    }

    public void speakLatestTweet(){
        engine.speak(parsedReTweets.get(0), TextToSpeech.QUEUE_FLUSH, null);
    }

    public void speakLatestStatus() {
        engine.speak(parsedStatuses.get(0).getTweet(), TextToSpeech.QUEUE_FLUSH, null);
    }

    public void updateStatus(String text) {
        try {
            try {
                // get request token.
                // this will throw IllegalStateException if access token is already available
                RequestToken requestToken = twitter.getOAuthRequestToken();
                Log.d("Twitter", "Got request token.");
                Log.d("Twitter", "Request token: " + requestToken.getToken());
                Log.d("Twitter", "Request token secret: " + requestToken.getTokenSecret());
                AccessToken accessToken = null;

                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                while (null == accessToken) {
                    Log.d("Twitter", "Open the following URL and grant access to your account:");
                    Log.d("Twitter", requestToken.getAuthorizationURL());
                    Log.d("Twitter", "Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
                    String pin = br.readLine();
                    try {
                        if (pin.length() > 0) {
                            accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                        } else {
                            accessToken = twitter.getOAuthAccessToken(requestToken);
                        }
                    } catch (TwitterException te) {
                        if (401 == te.getStatusCode()) {
                            Log.d("Twitter", "Unable to get the access token.");
                        } else {
                            te.printStackTrace();
                        }
                    }
                }
                Log.d("Twitter", "Got access token.");
                Log.d("Twitter", "Access token: " + accessToken.getToken());
                Log.d("Twitter", "Access token secret: " + accessToken.getTokenSecret());
            } catch (IllegalStateException ie) {
                // access token is already available, or consumer key/secret is not set.
                if (!twitter.getAuthorization().isEnabled()) {
                    Log.d("Twitter", "OAuth consumer key/secret is not set.");
                    return;
                }
            }
            Status status = twitter.updateStatus(text);
            Log.d("Twitter", "Successfully updated the status to [" + status.getText() + "].");
            return;
        } catch (TwitterException te) {
            te.printStackTrace();
            Log.d("Twitter", "Failed to get timeline: " + te.getMessage());
            return;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Log.d("Twitter", "Failed to read the system input.");
            return;
        }
    }

}

