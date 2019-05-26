package com.seagate.ashareral;

public class Vote {
    private String voterName,answer;
    private long timestamp,pollId;
    private int answerId;

    public Vote(String voterName, String answer, long timestamp, long pollId, int answerId) {
        this.voterName = voterName;
        this.answer = answer;
        this.timestamp = timestamp;
        this.pollId = pollId;
        this.answerId = answerId;
    }

    public Vote() {
    }

    public String getVoterName() {
        return voterName;
    }

    public String getAnswer() {
        return answer;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getPollId() {
        return pollId;
    }

    public int getAnswerId() {
        return answerId;
    }
}
