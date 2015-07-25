package com.unhcfreg.securobot;
import android.util.Log;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.android.IOIOActivity;

/**
 * Created by Devon on 7/5/2015.
 */
public class IRSensor{
    public AnalogInput input;
    public int pin;
    public float baseValue=0, baseVolt=0;
    public final int iSamples = 10;    //number of samples to take when initializing
    private final float valThresh = .08f;   //.05
    private final float volThresh = 0.12f;   //.1

    public IRSensor(int pin){
        this.pin = pin;
    }

    public void initialize(float baseValue, float baseVolt){
        this.baseValue = baseValue;
        this.baseVolt = baseVolt;
    }

    public boolean motionDetect(float measuredVal, float measuredVolt) throws ConnectionLostException, InterruptedException {
        if(measuredVal>(baseValue+valThresh) || measuredVolt>(baseVolt+volThresh) ||
                measuredVal<(baseValue-valThresh) || measuredVolt<(baseVolt-volThresh)){
            return true;
        }
        return false;
    }
}