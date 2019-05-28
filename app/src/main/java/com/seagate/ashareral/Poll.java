package com.seagate.ashareral;

import java.io.Serializable;

public class Poll implements Serializable {
    private String question,choice1,choice2,choice3;
    private long timestamp;
    private boolean isItActive=true;

    public Poll(String question, String choice1, String choice2, String choice3, long timestamp,
                boolean isItActive) {
        this.question = question;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.timestamp = timestamp;
        this.isItActive=isItActive;
    }

    public Poll() {
    }

    public boolean isItActive() {
        return isItActive;
    }

    public void setItActive(boolean itActive) {
        isItActive = itActive;
    }

    public String getQuestion() {
        return question;
    }

    public String getChoice1() {
        return choice1;
    }

    public String getChoice2() {
        return choice2;
    }

    public String getChoice3() {
        return choice3;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
