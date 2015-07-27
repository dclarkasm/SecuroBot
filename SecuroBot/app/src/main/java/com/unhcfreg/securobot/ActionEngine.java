package com.unhcfreg.securobot;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Devon on 7/5/2015.
 */
public class ActionEngine {
    public TTSEngine TTSE;
    private GreetingEngine greetingE = new GreetingEngine();
    private RSSEngine RSSFeed = new RSSEngine();
    private QuizEngine quizE = new QuizEngine();
    private JokeEngine jokeE = new JokeEngine();
    private TipEngine tipE = new TipEngine();
    private TwitterEngine twitE = new TwitterEngine();
    Random r = new Random();

    public static final int ACTION_TWEET = 0;
    public static final int ACTION_RSS = 1;
    public static final int ACTION_JOKE = 2;
    public static final int ACTION_QUIZ = 3;
    public static final int ACTION_PAGE = 4;
    public static final int ACTION_TIP = 5;

    private int currentAction;

    public ActionEngine(TTSEngine e) {
        setTTSEngine(e);
        //executeTweetSearch(false);
        executeRSS(false);
    }

    public void setTTSEngine(TTSEngine e){
        TTSE = e;
        twitE.setTTSEngine(e);
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
            case ACTION_TWEET: executeTimelineSearch(true); break;
            case ACTION_RSS: executeRSS(true); break;
            case ACTION_JOKE: executeJoke(); break;
            case ACTION_QUIZ: executeQuiz(); break;
            case ACTION_PAGE: executePage(); break;
            case ACTION_TIP: executeTip(); break;
            default: executeTimelineSearch(true); break;
        }
        currentAction = rn;
    }

    public int getCurrentAction() {
        return currentAction;
    }

    public void executeTweetSearch(boolean speak) {
        executeTweetSearch("cyber security", speak);
    }

    public void executeTweetSearch(String search, boolean speak) {
        try{
            twitE.searchOnTwitter(search);
            if(speak) {
                executeSpeech("I just red this on twitter.");
                twitE.speakLatestTweet();
            }
        }
        catch(Exception e) {
            Log.d("TWITTER", "Caught an exception! - execute tweet search");
            e.printStackTrace();
        }
    }

    public void executeTimelineSearch(boolean speak) {
        fetchContent();
        try{
            if(speak) {
                executeSpeech("Check out my latest status update. ");
                twitE.speakLatestStatus();
            }
        }
        catch(Exception e) {
            Log.d("TWITTER", "Caught an exception! - execute timeline search");
            e.printStackTrace();
        }
    }

    public void executeMakeTweet(String text) {
        twitE.updateStatus(text);
    }

    public void executeRSS(boolean speak) {
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
        RSSFeed.fetchXML();

        while(RSSFeed.processing);
        if(!RSSFeed.isProcessingFailure()) {
            executeSpeech("Check out this article eye just red from " + RSSFeed.getAuthor());    //read is spelled red for phonetics same with I (eye)
            //webPageView.loadUrl(RSSFeed.getLink());
            while(TTSE.t1.isSpeaking());
            Log.d("WEB PAGE", "Author: " + RSSFeed.getAuthor() + "\nLink: " + RSSFeed.getLink());
        }
    }

    public String getWebPage() {
        return RSSFeed.getLink();
    }

    public String getQuiz() {
        return quizE.generateQuiz();
    }

    public void executeTip() {
        executeSpeech(tipE.generateGreeting());
    }

    /*
    public static final int ACTION_TWEET = 0;
    public static final int ACTION_RSS = 1;
    public static final int ACTION_JOKE = 2;
    public static final int ACTION_QUIZ = 3;
    public static final int ACTION_PAGE = 4;
    public static final int ACTION_TIP = 5;

    private RSSEngine RSSFeed;
    private QuizEngine quizE = new QuizEngine();
    private JokeEngine jokeE = new JokeEngine();
    private TipEngine tipE = new TipEngine();
     */
    public void fetchContent(){
        twitE.getTimeline();
        //twitter adds all tweet content to itself automatically
        quizE.addContent(twitE.getContent(Tweet.SECUROBOT_QUIZ));
        RSSFeed.addContent(twitE.getContent(Tweet.SECUROBOT_RSSFEED));
        jokeE.addContent(twitE.getContent(Tweet.SECUROBOT_JOKE));
        tipE.addContent(twitE.getContent(Tweet.SECUROBOT_TIP));
    }
}
