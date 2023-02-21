package com.example.trivia.model;

public class Score {

    private int score=0;

    public Score() {
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addPoints()
    {
        score=+100;
    }
    public void substractPoints()
    {
        if (score>0) {
            score = -100;
        }

    }

}
