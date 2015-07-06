package com.unhcfreg.securobot;

import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Devon on 7/5/2015.
 */
public class ActionEngine {
    public TTSEngine TTSE;
    private GreetingEngine greetingE = new GreetingEngine();
    private JokeEngine jokeE = new JokeEngine();
    private TipEngine tipE = new TipEngine();
    Random r = new Random();
    private TwitterEngine twitSearch = new TwitterEngine();

    public static final int ACTION_TWEET = 0;
    public static final int ACTION_RSS = 1;
    public static final int ACTION_JOKE = 2;
    public static final int ACTION_QUIZ = 3;
    public static final int ACTION_PAGE = 4;
    public static final int ACTION_TIP = 5;

    public void setTTSEngine(TTSEngine e){
        TTSE = e;
        twitSearch.setTTSEngine(e);
    }

    public void executeSpeech(String speech) {
        TTSE.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void executeGreeting() {
        executeSpeech(greetingE.generateGreeting());
    }

    public void executeRandActivity() {
        int rn = r.nextInt(6-0);

        switch(rn) {
            case ACTION_TWEET: executeTweetSearch(true); break;
            case ACTION_RSS: executeRSS(true); break;
            case ACTION_JOKE: executeJoke(); break;
            case ACTION_QUIZ: executeQuiz(); break;
            case ACTION_PAGE: executePage(); break;
            case ACTION_TIP: executeTip(); break;
            default: executeTweetSearch(true); break;
        }
    }

    public void executeTweetSearch(boolean speak) {
        executeTweetSearch("cyber security", speak);
    }

    public void executeTweetSearch(String search, boolean speak) {
        try{
            twitSearch.searchOnTwitter(search);
            if(speak) {
                executeSpeech("I just red this on twitter.");
                twitSearch.speakLatestTweet();
            }
        }
        catch(Exception e) {
            Log.d("TWITTER", "Caught an exception!");
            e.printStackTrace();
        }
    }

    public void executeMakeTweet() {

    }

    public void executeRSS(boolean speak) {
        RSSEngine RSSFeed;
        RSSFeed = new RSSEngine(null);
        RSSFeed.fetchXML();

        while(RSSFeed.processing);
        if(!RSSFeed.isProcessingFailure()) {
            executeSpeech("According to this RSS feed I just red from " + RSSFeed.getAuthor() +
                    ", " + RSSFeed.getTitle());
            Log.d("RSS FEED", "Author: " + RSSFeed.getAuthor() + "\nTitle: " + RSSFeed.getTitle()
                    + "\nDescription: " + RSSFeed.getDescription());
        }
        else Log.d("RSS FEED", "RSS Failure");
    }

    public void executeJoke() {
        executeSpeech(jokeE.generateJoke());
    }

    public void executeQuiz() {
        executeSpeech("Would you like to take a quiz?");
    }

    public void executePage() {
        executeSpeech("Check out this article eye just red.");    //read is spelled red for phonetics same with I (eye)
    }

    public void executeTip() {
        executeSpeech(tipE.generateGreeting());
    }
}
