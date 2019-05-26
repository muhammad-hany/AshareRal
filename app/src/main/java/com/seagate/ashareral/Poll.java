package com.seagate.ashareral;

import java.io.Serializable;

public class Poll implements Serializable {
    private String question,choice1,choice2,choice3;
    private long timestamp;

    public Poll(String question, String choice1, String choice2, String choice3, long timestamp) {
        this.question = question;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.timestamp = timestamp;
    }

    public Poll() {
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
