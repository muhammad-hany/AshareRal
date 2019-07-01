package com.seagate.ashareral;

import java.io.Serializable;

public class Chapter implements Serializable {
    String country,location,web,person,email,phone,download_link,subRegion;
    int chapterNumber;
    long timestamp;

    public Chapter() {
    }

    public Chapter(String country, String location, String web, String person, String email,
                   String phone, String download_link, int chapterNumber, long timestamp,
                   String subRegion) {
        this.country = country;
        this.location = location;
        this.web = web;
        this.person = person;
        this.email = email;
        this.phone = phone;
        this.download_link = download_link;
        this.chapterNumber = chapterNumber;
        this.timestamp = timestamp;
        this.subRegion=subRegion;
    }

    public String getDownload_link() {
        return download_link;
    }

    public String getSubRegion() {
        return subRegion;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(int chapterNumber) {
        this.chapterNumber = chapterNumber;
    }
}
