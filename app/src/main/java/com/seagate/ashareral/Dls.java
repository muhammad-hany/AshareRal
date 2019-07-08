package com.seagate.ashareral;

import java.io.Serializable;

public class Dls implements Serializable {
    String name,position,bio,download_link,couseTought;
    long timestamo;

    public String getCouseTought() {
        return couseTought;
    }

    public Dls(String name, String position, String bio, String download_link, String couseTought, long timestamo) {
        this.name = name;
        this.position = position;
        this.bio = bio;
        this.download_link = download_link;
        this.couseTought = couseTought;
        this.timestamo = timestamo;
    }

    public Dls() {
    }

    public void setDownload_link(String download_link) {
        this.download_link = download_link;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getBio() {
        return bio;
    }

    public String getDownload_link() {
        return download_link;
    }

    public long getTimestamo() {
        return timestamo;
    }
}
