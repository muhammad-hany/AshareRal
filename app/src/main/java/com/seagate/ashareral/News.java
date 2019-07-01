package com.seagate.ashareral;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class News implements Serializable,Comparable<News> {
  String title,body,imageUri,date;
  long imageTimestamp;

    public News(String body,String title, long imageTimestamp,String imageUri,String date) {
        this.date=date;
        this.title = title;
        this.body = body;
        this.imageUri = imageUri;
        this.imageTimestamp = imageTimestamp;
    }

    public News() {
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public long getImageTimestamp() {
        return imageTimestamp;
    }

    public void setImageTimestamp(long imageTimestamp) {
        this.imageTimestamp = imageTimestamp;
    }

    @Override
    public int compareTo(News o) {
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date=format.parse(o.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Utils.getDate(getDate()).compareTo(Utils.getDate(o.getDate()));
    }
}