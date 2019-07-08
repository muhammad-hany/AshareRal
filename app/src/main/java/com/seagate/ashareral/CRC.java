package com.seagate.ashareral;

import java.io.Serializable;
import java.util.List;

public class CRC implements Serializable {
    String title,describtion,coverimageDownloadLink,date;
    int perioud;
    long timestamp;
    List<String> sponser_download_links;


    public CRC() {
    }

    public CRC(String title, String describtion, String coverimageDownloadLink, String date, int perioud, long timestamp, List<String> sponser_download_links) {
        this.title = title;
        this.describtion = describtion;
        this.coverimageDownloadLink = coverimageDownloadLink;
        this.date = date;
        this.perioud = perioud;
        this.timestamp = timestamp;
        this.sponser_download_links = sponser_download_links;
    }

    public String getDate() {
        return date;
    }

    public int getPerioud() {
        return perioud;
    }

    public String getTitle() {
        return title;
    }

    public String getDescribtion() {
        return describtion;
    }

    public String getCoverimageDownloadLink() {
        return coverimageDownloadLink;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public List<String> getSponser_download_links() {
        return sponser_download_links;
    }
}
