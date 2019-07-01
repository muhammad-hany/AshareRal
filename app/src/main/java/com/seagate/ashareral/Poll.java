package com.seagate.ashareral;

import java.io.Serializable;
import java.util.List;

public class Poll implements Serializable {
    private String question,password;
    private long timestamp;
    private boolean isItActive=true;
    private List<String> choices;
    private boolean multiChoice;
    private boolean published;

    public Poll(String question, String password, long timestamp, boolean isItActive,
                List<String> choices,boolean multiChoice,boolean published) {
        this.multiChoice =multiChoice;
        this.question = question;
        this.password = password;
        this.timestamp = timestamp;
        this.isItActive = isItActive;
        this.choices = choices;
        this.published=published;
    }

    public Poll() {
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isPublished() {
        return published;
    }

    public boolean isMultiChoice() {
        return multiChoice;
    }

    public List<String> getChoices() {
        return choices;
    }

    public String getPassword() {
        return password;
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


    public long getTimestamp() {
        return timestamp;
    }
}
