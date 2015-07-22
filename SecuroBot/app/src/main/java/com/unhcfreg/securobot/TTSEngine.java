package com.unhcfreg.securobot;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Devon on 7/5/2015.
 */
public class TTSEngine implements TextToSpeech.OnInitListener{
    public TextToSpeech t1;

    public TTSEngine(Context c) {
        t1 = new TextToSpeech(c, this);
    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
    }

    @Override
    public void onInit ( int status){
        if (status == TextToSpeech.SUCCESS) {
            Log.d("TTS", "Success!");
            t1.setLanguage(Locale.US);
        }
    }

    public void speak(String string, int queueMode, Bundle params, String UtteranceId){
        while(t1.isSpeaking()); //wait until we finish speaking before saying something else
        if(!t1.isSpeaking()){
            //TODO: Add a one shot timer here to wait for a few ms for the TTS engine to begin speaking
            t1.speak(string, queueMode, params, UtteranceId);
            while(t1.isSpeaking()); //wait until we finish speaking before saying something else
        }
    }
}
