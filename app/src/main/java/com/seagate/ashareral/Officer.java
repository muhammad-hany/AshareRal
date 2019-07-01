package com.seagate.ashareral;

import java.io.Serializable;

public class Officer implements Serializable {
    String name,position,email,bio,download_link;
    long timestamp;

    public Officer() {
    }

    public Officer(String name, String position, String email, String bio, String download_link, long timestamp) {
        this.name = name;
        this.position = position;
        this.email = email;
        this.bio = bio;
        this.download_link = download_link;
        this.timestamp = timestamp;
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

    public String getDownload_link() {
        return download_link;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
