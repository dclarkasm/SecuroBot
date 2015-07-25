package com.unhcfreg.securobot;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Devon on 7/6/2015.
 */
public class JokeEngine {
    Random r = new Random();
    private static final String intros[] = {
            "I heard this great joke the other day. ",
            "A computer friend of mine told me this funny joke. "
    };
    private ArrayList<String> jokes = new ArrayList<String>();

    public JokeEngine() {
        jokes.add("A guy walks into a bar. Ouch.");
        /*jokes.add("Just kidding, cyber security is no joke, stop laughing.");
        jokes.add("Your computer is a joke without virus protection. You should get some virus protection");
        //from http://www.ducksters.com/jokesforkids/computer.php
        jokes.add("What did the computer do at lunchtime? Had a byte");
        jokes.add("What does a baby computer call his father? Data.");
        jokes.add("Why did the computer keep sneezing? It had a virus.");
        jokes.add("What is a computer virus? A terminal illness");
        jokes.add("Why did the computer sqweak? Because someone stepped on it's mouse.");
        //http://www.ajokeaday.com/Clasificacion.asp?ID=18
        jokes.add("Computers are like air conditioners. They work fine until you start opening windows");
        jokes.add("How many programmers does it take to change a lightbulb? " +
                "None, thats a hardware problem.");
        jokes.add("Computers can never replace humans. They may become capable of artificial intelligence,"+
                "but they will never master real stupidity.");
        jokes.add("Why did the spider cross the computer keyboard? To get to the World Wide Web.");*/
    }

    public String generateJoke() {
        int rn1 = r.nextInt(intros.length-0);
        int rn2 = r.nextInt(jokes.size()-0);
        return intros[rn1] + jokes.get(rn2);
    }

    public void printContent() {
        for(String q : jokes) {
            Log.d("Joke", q);
        }
    }

    public void addContent(ArrayList<String> content) {
        if(content!=null) {
            for(String c : content) {
                if(!jokes.contains(c)) jokes.add(c);
            }
        }

        Log.d("Joke", "content:\n");
        printContent();
    }
}
