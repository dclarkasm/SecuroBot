package com.unhcfreg.securobot;

import com.unhcfreg.securobot.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimerTask;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class SecuroBotMain extends IOIOActivity implements TextToSpeech.OnInitListener{
//test
    //test2

    TextToSpeech t1;
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    //**********************************************
    ImageView leftEye;
    ImageView rightEye;
    int lEResource;
    int rEResource;
    TimerTask timerTask;
    final Handler handler = new Handler();
    //Runnable runnable;
    //private Handler mHandler;
    Random r = new Random();

    /*
    void startRepeatingTask() {
        runnable.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(runnable);
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_securo_bot_main);

        t1=new TextToSpeech(this, this);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        //left
        leftEye = (ImageView) findViewById(R.id.leftEye);
        lEResource = R.drawable.blueeyesopenleft;
        leftEye.setImageResource(lEResource);
        //right
        rightEye = (ImageView) findViewById(R.id.rightEye);
        rEResource = R.drawable.blueeyesopenright;
        rightEye.setImageResource(rEResource);
        final View contentView = findViewById(R.id.eyes);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });
/*
        runnable = new Runnable() {
            @Override
            public void run() {

            }

            public void wait(int time) {
                Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //do nothing
                    }
                }, time); //adding one sec delay
            }

        };
        mHandler = new Handler();
        startRepeatingTask();
*/
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    @Override
    public void onInit ( int status){
        if (status == TextToSpeech.SUCCESS) {
            Log.d("TTS", "Success!");
            t1.setLanguage(Locale.US);
        }
    }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

//****************************************************************************************
//****************************************************************************************
    /**
     * This is the thread on which all the IOIO activity happens. It will be run
     * every time the application is resumed and aborted when it is paused. The
     * method setup() will be called right after a connection with the IOIO has
     * been established (which might happen several times!). Then, loop() will
     * be called repetitively until the IOIO gets disconnected.
     */
    class Looper extends BaseIOIOLooper {
        /** The on-board LED. */
        private DigitalOutput led_;

        /**
         * Called every time a connection with IOIO has been established.
         * Typically used to open pins.
         *
         * @throws ConnectionLostException
         *             When IOIO connection is lost.
         *
         * @see ioio.lib.util.IOIOLooper
         */
        @Override
        protected void setup() throws ConnectionLostException {
            showVersions(ioio_, "IOIO connected!");
            led_ = ioio_.openDigitalOutput(0, true);
        }

        /**
         * Called repetitively while the IOIO is connected.
         *
         * @throws ConnectionLostException
         *             When IOIO connection is lost.
         * @throws InterruptedException
         * 				When the IOIO thread has been interrupted.
         *
         * @see ioio.lib.util.IOIOLooper#loop()
         */
        @Override
        public void loop() throws ConnectionLostException, InterruptedException {
            int il = r.nextInt(100-0)+0; //random number between 0 and 100
            int ir = r.nextInt(100-0)+0;

            if(il>50) {
                if(lEResource == R.drawable.blueeyesclosedleft) {
                    lEResource = R.drawable.blueeyesopenleft;
                }
                else lEResource = R.drawable.blueeyesclosedleft;
                leftEye.setImageResource(lEResource);
                led_.write(false);
            }

            if(ir>50) {
                if(rEResource == R.drawable.blueeyesclosedright) {
                    rEResource = R.drawable.blueeyesopenright;
                }
                else rEResource = R.drawable.blueeyesclosedright;
                rightEye.setImageResource(rEResource);
                led_.write(true);
            }

            new SearchOnTwitter().execute("cyber security");

            Thread.sleep(1000);
        }

        /**
         * Called when the IOIO is disconnected.
         *
         * @see ioio.lib.util.IOIOLooper#disconnected()
         */
        @Override
        public void disconnected() {
            toast("IOIO disconnected");
        }

        /**
         * Called when the IOIO is connected, but has an incompatible firmware version.
         *
         * @see ioio.lib.util.IOIOLooper#incompatible(IOIO)
         */
        @Override
        public void incompatible() {
            showVersions(ioio_, "Incompatible firmware version!");
        }
    }
//test
    //hrbrhtn
    int i=0;
    /**
     * A method to create our IOIO thread.
     *
     * @see ioio.lib.util.AbstractIOIOActivity#createIOIOThread()
     */
    @Override
    protected IOIOLooper createIOIOLooper() {
        return new Looper();
    }

    private void showVersions(IOIO ioio, String title) {
        toast(String.format("%s\n" +
                        "IOIOLib: %s\n" +
                        "Application firmware: %s\n" +
                        "Bootloader firmware: %s\n" +
                        "Hardware: %s",
                title,
                ioio.getImplVersion(IOIO.VersionType.IOIOLIB_VER),
                ioio.getImplVersion(IOIO.VersionType.APP_FIRMWARE_VER),
                ioio.getImplVersion(IOIO.VersionType.BOOTLOADER_VER),
                ioio.getImplVersion(IOIO.VersionType.HARDWARE_VER)));
    }

    private void toast(final String message) {
        final Context context = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private int numConnected_ = 0;
/*
    private void enableUi(final boolean enable) {
        // This is slightly trickier than expected to support a multi-IOIO use-case.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (enable) {
                    if (numConnected_++ == 0) {
                        button_.setEnabled(true);
                    }
                } else {
                    if (--numConnected_ == 0) {
                        button_.setEnabled(false);
                    }
                }
            }
        });
    }
*/

    class SearchOnTwitter extends AsyncTask<String, Void, Integer> {
        ArrayList<Tweet> tweets;
        final int SUCCESS = 0;
        final int FAILURE = SUCCESS + 1;
        private final String TWIT_CONS_KEY = "r5Z9pl8sE6TOuuFGCvea0ySwF";
        private final String TWIT_CONS_SEC_KEY = "Qhthb9U8FRErYKhtq0Bl09M0uJMmNAizW893c01Ye2m6apvYPk";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
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
                e.printStackTrace();
            }

            return FAILURE;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == SUCCESS) {
                Toast.makeText(SecuroBotMain.this, "Search was successful", Toast.LENGTH_LONG).show();
                //t1.speak("hello", TextToSpeech.QUEUE_FLUSH, null, null);
                for(Tweet t : tweets)
                {
                    String toSpeak = t.getTweetBy() + " says " + t.getTweet();
                    Log.d("Twater", toSpeak);
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
            } else {
                Toast.makeText(SecuroBotMain.this, "Could not find results for search", Toast.LENGTH_LONG).show();
                Log.d("Twater", "Search was not successful");
            }
        }
    }
}


