package com.seagate.ashareral;

import java.io.Serializable;

public class Committee implements Serializable {
   String committee,name,position,email,bio,download_link;
   long timestamp;

    public Committee() {
    }

    public Committee(String committee, String name, String position, String email, String bio, String download_link, long timestamp) {
        this.committee = committee;
        this.name = name;
        this.position = position;
        this.email = email;
        this.bio = bio;
        this.download_link = download_link;
        this.timestamp = timestamp;
    }

    public String getDownload_link() {
        return download_link;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getCommittee() {
        return committee;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getEmail() {
        return email;
    }

    public String getBio() {
        return bio;
    }
}
