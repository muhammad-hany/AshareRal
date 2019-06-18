package com.seagate.ashareral;

import java.io.Serializable;

public class News implements Serializable {
  String title,body,imageUri;
  long imageTimestamp;

    public News(String body,String title, long imageTimestamp,String imageUri) {
        this.title = title;
        this.body = body;
        this.imageUri = imageUri;
        this.imageTimestamp = imageTimestamp;
    }

    public News() {
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
}