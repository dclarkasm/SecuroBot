package com.unhcfreg.securobot;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Devon on 7/7/2015.
 */
public class QuizEngine{
    private Random r = new Random();
    private ArrayList<String> quizes = new ArrayList<String>();   //initial array. we add content via constructor and twitter

    public QuizEngine() {
        quizes.add("https://www.onlineassessmenttool.com/test/assessment-26343");
    }

    public String generateQuiz() {
        int rn = r.nextInt(quizes.size() - 0);
        return quizes.get(rn);
    }

    public void printContent() {
        for(String q : quizes) {
            Log.d("Quiz", q);
        }
    }

    public void addContent(ArrayList<String> content) {
        quizes.addAll(content);
    }
}
