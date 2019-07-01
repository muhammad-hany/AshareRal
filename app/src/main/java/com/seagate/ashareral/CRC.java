package com.seagate.ashareral;

public class CRC {
    String title,dateFrom,describtion,location,packages,program,coverimageDownloadLink;
    long timestamp;
    int perioud;
    String [] sponser_download_links;


    public CRC(String title, String dateFrom, String describtion, String location, String packages, String program, String coverimageDownloadLink, long timestamp, int perioud, String[] sponser_download_links) {
        this.title = title;
        this.dateFrom = dateFrom;
        this.describtion = describtion;
        this.location = location;
        this.packages = packages;
        this.program = program;
        this.coverimageDownloadLink = coverimageDownloadLink;
        this.timestamp = timestamp;
        this.perioud = perioud;
        this.sponser_download_links = sponser_download_links;
    }

    public String getCoverimageDownloadLink() {
        return coverimageDownloadLink;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getTitle() {
        return title;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public String getDescribtion() {
        return describtion;
    }

    public String getLocation() {
        return location;
    }

    public String getPackages() {
        return packages;
    }

    public String getProgram() {
        return program;
    }

    public int getPerioud() {
        return perioud;
    }

    public String[] getSponser_download_links() {
        return sponser_download_links;
    }
}
