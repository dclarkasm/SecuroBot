package com.unhcfreg.securobot;

import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Random;

/**
 * Created by Devon on 7/5/2015.
 */
public class ActionEngine {
    public TTSEngine engine;
    Random r = new Random();
    private TwitterEngine twitSearch = new TwitterEngine();

    public static final int ACTION_TWEET = 0;
    public static final int ACTION_RSS = 1;
    public static final int ACTION_JOKE = 2;
    public static final int ACTION_QUIZ = 3;
    public static final int ACTION_PAGE = 4;
    public static final int ACTION_TIP = 5;

    public void setTTSEngine(TTSEngine e){
        engine = e;
        twitSearch.setTTSEngine(e);
    }

    public void executeSpeech(String speech) {
        engine.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void executeGreeting() {
        int rn = r.nextInt(6-0);
        String g;

        switch(rn) {
            case 0: g = "Hello, student"; break;
            case 1: g = "Hi, How are you today?"; break;
            case 2: g = "Hello, would you like to learn something about cyber security?"; break;
            case 3: g = "How's it going?"; break;
            case 4: g = "Hey there"; break;
            case 5: g = "Come and learn something about cyber security"; break;
            default: g = "Hello"; break;
        }

        executeSpeech(g);
    }

    public void executeRandActivity() {
        int rn = r.nextInt(6-0);

        switch(rn) {
            case ACTION_TWEET: executeTweetSearch(true); break;
            case ACTION_RSS: executeRSS(); break;
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

    public void executeRSS() {
        executeSpeech("Check out the latest RSS feed I just saw.");
    }

    public void executeJoke() {
        executeSpeech("I heard this great joke the other day. A guy walks into a bar. Ouch.");
    }

    public void executeQuiz() {
        executeSpeech("Would you like to take a quiz?");
    }

    public void executePage() {
        executeSpeech("Check out this article eye just red.");    //read is spelled red for phonetics same with I (eye)
    }

    public void executeTip() {
        executeSpeech("Here's a tip. Keep your computer safe by choosing a password that includes letters,"
        + " numbers, and special characters. Try not to include birthdays, common names, or places." +
        " Using obscure words mixed with an assortment of random numbers or symbols can better protect you" +
        "against brute force password hacking attacks and will keep your personal information safe.");
    }
}
