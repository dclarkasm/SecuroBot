package com.unhcfreg.securobot;

import java.util.Random;

/**
 * Created by Devon on 7/6/2015.
 */
public class GreetingEngine {
    Random r = new Random();
    private static final String greetings[] = {
            "Hello, student",
            "Hi, How are you today?",
            "Hello, would you like to learn something about cyber security?",
            "How's it going?",
            "Hey there",
            "Come and learn something about cyber security"
    };

    public String generateGreeting() {
        int rn = r.nextInt(greetings.length-0);
        return greetings[rn];
    }
}
