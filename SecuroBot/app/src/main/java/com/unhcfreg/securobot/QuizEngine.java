package com.unhcfreg.securobot;

import java.util.Random;

/**
 * Created by Devon on 7/7/2015.
 */
public class QuizEngine {
    Random r = new Random();
    private static final String quizes[] = {
            "https://www.onlineassessmenttool.com/test/assessment-26343"
    };

    public String generateQuiz() {
        int rn = r.nextInt(quizes.length - 0);
        return quizes[rn];
    }
}
