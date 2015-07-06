package com.unhcfreg.securobot;

import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.util.Log;

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
 * Created by Devon on 7/5/2015.
 */
public class TwitterEngine {
    public TTSEngine engine;
    private String latestTweet;

    public void searchOnTwitter(String text) {
        new SearchOnTwitter().execute(text);
    }

    public void setTTSEngine(TTSEngine e) {
        engine = e;
    }

    public void speakLatestTweet(){
        engine.speak(latestTweet, TextToSpeech.QUEUE_FLUSH, null);
    }

    class SearchOnTwitter extends AsyncTask<String, Void, Integer> {
        ArrayList<Tweet> tweets;
        final int SUCCESS = 0;
        final int FAILURE = SUCCESS + 1;
        private final String TWIT_CONS_KEY = "r5Z9pl8sE6TOuuFGCvea0ySwF";
        private final String TWIT_CONS_SEC_KEY = "Qhthb9U8FRErYKhtq0Bl09M0uJMmNAizW893c01Ye2m6apvYPk";
        boolean searching = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                searching = true;
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setUseSSL(true);
                builder.setApplicationOnlyAuthEnabled(true);
                builder.setOAuthConsumerKey(TWIT_CONS_KEY);
                builder.setOAuthConsumerSecret(TWIT_CONS_SEC_KEY);

                OAuth2Token token = new TwitterFactory(builder.build()).getInstance().getOAuth2Token();

                builder = new ConfigurationBuilder();
                builder.setUseSSL(true);
                builder.setApplicationOnlyAuthEnabled(true);
                builder.setOAuthConsumerKey(TWIT_CONS_KEY);
                builder.setOAuthConsumerSecret(TWIT_CONS_SEC_KEY);
                builder.setOAuth2TokenType(token.getTokenType());
                builder.setOAuth2AccessToken(token.getAccessToken());

                Twitter twitter = new TwitterFactory(builder.build()).getInstance();

                Query query = new Query(params[0]);
                // YOu can set the count of maximum records here
                query.setCount(1);
                QueryResult result;
                result = twitter.search(query);
                List<twitter4j.Status> tweets = result.getTweets();
                StringBuilder str = new StringBuilder();
                if (tweets != null) {
                    this.tweets = new ArrayList<Tweet>();
                    for (twitter4j.Status tweet : tweets) {
                        str.append("@" + tweet.getUser().getScreenName() + " - " + tweet.getText() + "\n");
                        System.out.println(str);
                        this.tweets.add(new Tweet("@" + tweet.getUser().getScreenName(), tweet.getText()));
                    }
                    return SUCCESS;
                }
            } catch (Exception e) {
                searching = false;
                e.printStackTrace();
            }

            return FAILURE;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == SUCCESS) {
                for(Tweet t : tweets)
                {
                    latestTweet = t.getTweetBy() + " says " + t.getTweet();
                    Log.d("Twater", latestTweet);
                    //engine.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
            } else {
                Log.d("Twater", "Search was not successful");
            }
            searching = false;
        }

        public boolean isSearching() {
            return searching;
        }
    }
}

