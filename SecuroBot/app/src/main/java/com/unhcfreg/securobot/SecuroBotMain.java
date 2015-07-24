package com.unhcfreg.securobot;

import com.unhcfreg.securobot.util.SystemUiHider;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimerTask;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import twitter4j.Query;
import twitter4j.QueryResult;
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
public class SecuroBotMain extends IOIOActivity //implements TextToSpeech.OnInitListener
{

    //TextToSpeech t1;
    TTSEngine t1;
    //TwitterEngine twitSearch = new TwitterEngine();
    ActionEngine action;

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
    private Handler mHandler;
    Random r = new Random();
    WebView webPageView;
    WebView webQuizView;
    boolean loadingFinished = true;
    boolean redirect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_securo_bot_main);

        t1=new TTSEngine(this);

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

        webPageView = (WebView) findViewById(R.id.webview);
        webQuizView = (WebView) findViewById(R.id.webview);
        WebSettings webQuizSettings = webQuizView.getSettings();
        webQuizSettings.setJavaScriptEnabled(true);
        webPageView.setVisibility(View.INVISIBLE);

        action = new ActionEngine(t1, webPageView);
        mHandler = new Handler();

        startRepeatingTask();
    }

    void startRepeatingTask() {
        runnable.run();
        webpageRunnable.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(runnable);
        mHandler.removeCallbacks(webpageRunnable);
    }

    Runnable runnable = new Runnable() {
    @Override
    public void run() {
        int il = r.nextInt(0+100);
        int ir = r.nextInt(0+100);

        if(il>50) {

            if(lEResource == R.drawable.blueeyesclosedleft) {
                lEResource = R.drawable.blueeyesopenleft;
            }
            else {
                lEResource = R.drawable.blueeyesclosedleft;
            }
            leftEye.setImageResource(lEResource);
        }

        if(ir>50) {

            if(rEResource == R.drawable.blueeyesclosedright) {
                rEResource = R.drawable.blueeyesopenright;
            }
            else rEResource = R.drawable.blueeyesclosedright;
            rightEye.setImageResource(rEResource);
        }
        mHandler.postDelayed(runnable, 5000);
    }
};

    Runnable webpageRunnable = new Runnable() {
        String currentPage = "";
        String currentQuiz = "";

        @Override
        public void run() {
            if(action.displayPage && webPageView.getVisibility()==View.INVISIBLE) {
                webQuizView.setVisibility(View.INVISIBLE);
                currentPage = action.getWebPage();
                webPageView.loadUrl(currentPage);
                webPageView.setVisibility(View.VISIBLE);
            }
            else if(action.displayQuiz && webQuizView.getVisibility()==View.INVISIBLE) {
                webPageView.setVisibility(View.INVISIBLE);
                currentQuiz = action.getQuiz();
                webPageView.loadUrl(currentQuiz);
                webQuizView.setVisibility(View.VISIBLE);
            }
            else if(!action.displayPage && !action.displayQuiz &&
                    (webPageView.getVisibility()==View.VISIBLE ||
                    webQuizView.getVisibility()==View.VISIBLE)) {
                webPageView.setVisibility(View.INVISIBLE);
                webQuizView.setVisibility(View.INVISIBLE);
                webPageView.loadUrl(currentQuiz);
                webPageView.loadUrl(currentPage);
            }

            mHandler.postDelayed(webpageRunnable, 100);
        }
    };

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }
/*
    public void onPause(){
        t1.pause();
        super.onPause();
    }

    @Override
    public void onInit ( int status){
        t1.init(status);
        twitSearch.setTTSEngine(t1);
    }
*/
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
        private IRSensor iRSensors = new IRSensor(33);
        private PwmOutput pwm;
        float value, volts;
        int newPos, currentPos;

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
        protected void setup() throws ConnectionLostException, InterruptedException {
            showVersions(ioio_, "IOIO connected!");
            led_ = ioio_.openDigitalOutput(0, true);
            iRSensors.input = ioio_.openAnalogInput(iRSensors.pin);
            initIR();

            try {
                pwm= ioio_.openPwmOutput(35, 100);  //new DigitalOutput.Spec(35, DigitalOutput.Spec.Mode.OPEN_DRAIN)
            } catch (ConnectionLostException e) {
                Log.d("Connection Lost", "IO Connection Lost");
            }
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
            int re = r.nextInt(100-0); //random number between 0 and 100 for rotation enable
            int ra = r.nextInt(3-0); //random number between 0 and 100 for rotation angle

            if(re <= 1) {  //20% chance that the head will rotate
                switch(ra){
                    case 0: newPos = 1000; break;    //limit 600
                    case 1: newPos = 1550; break;
                    case 2: newPos = 2000; break;   //limit 2450
                    default: break;
                }

                if(newPos != currentPos)
                {
                    led_.write(true);
                    pwm.setPulseWidth(newPos);
                    currentPos = newPos;
                    Log.d("ROTATE", "Moving to position: " + newPos + "...");
                    Thread.sleep(1000);
                    Log.d("ROTATE", "At position: " + newPos);
                    initIR();
                }
            }
            else{
                float measVal = iRSensors.input.read();
                float measVolt = iRSensors.input.getVoltage();
                if(!action.TTSE.t1.isSpeaking())
                {
                    if(iRSensors.motionDetect(measVal, measVolt)) {
                        led_.write(false);
                        Log.d("MOTION", "Detected motion!"
                                        + " BaseVal: " + iRSensors.baseValue + "/" + measVal +
                                        ", BaseVolt: " + iRSensors.baseVolt + "/" + measVolt
                        );

                        action.executeGreeting();   //execute a random greeting

                        action.displayPage = false;
                        action.displayQuiz = false;
                        //action.executeRandActivity();
                    /*
                    int rn = r.nextInt(2-0);
                    if(rn == 1) action.executeQuiz();
                    else action.executePage();
                    */
                        //action.executeMakeTweet("IR sensor voltage: " + measVolt);
                        //action.executeTweetSearch(true);
                        action.executeTimelineSearch(true);

                        Log.d("IR SENSORS", "reinitializing...");
                        initIR();
                    }
                    else {
                        led_.write(true);
                    }
                }
                else {
                    led_.write(true);
                }
            }

            Thread.sleep(100);
        }

        public void initIR() throws ConnectionLostException, InterruptedException {
            float baseVal=0f, baseVolt=0f;

            for(int i=0; i<iRSensors.iSamples; i++) {
                baseVal += iRSensors.input.read();
                baseVolt += iRSensors.input.getVoltage();
                //Thread.sleep(5);
            }
            iRSensors.initialize(baseVal/iRSensors.iSamples, baseVolt/iRSensors.iSamples);
            /*
            Log.d("INIT IR", "Base Val: " + baseVal/iRSensors.iSamples +
                    ", base Volt: " + baseVolt/iRSensors.iSamples);*/
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


}


