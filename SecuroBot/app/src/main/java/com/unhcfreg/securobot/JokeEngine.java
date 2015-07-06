package com.unhcfreg.securobot;

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
    private static final String jokes[] = {
            "A guy walks into a bar. Ouch.",
            "Just kidding, cyber security is no joke, stop laughing.",
            "Your computer is a joke after a virus. Get better virus protection.",

            //from http://www.ducksters.com/jokesforkids/computer.php
            "What did the computer do at lunchtime? Had a byte",
            "What does a baby computer call his father? Data.",
            "Why did the computer keep sneezing? It had a virus.",
            "What is a computer virus? A terminal illness",
            "Why did the computer sqweak? Because someone stepped on it's mouse.",

            //http://www.ajokeaday.com/Clasificacion.asp?ID=18
            "Computers are like air conditioners. They work fine until you start opening windows",
            "How many programmers does it take to change a lightbulb? " +
            "None, thats a hardware problem.",
            "Computers can never replace humans. They may become capable of artificial intelligence," +
            "but they will never master real stupidity.",
            "Why did the spider cross the computer keyboard? To get to the World Wide Web."
    };

    public String generateJoke() {
        int rn1 = r.nextInt(intros.length-0);
        int rn2 = r.nextInt(jokes.length-0);
        return intros[rn1] + jokes[rn2];
    }
}
