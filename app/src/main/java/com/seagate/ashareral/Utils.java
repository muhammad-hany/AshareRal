package com.seagate.ashareral;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static final String NEWS_BODY_KEY="body";
    public static final String NEWS_TITLE_KEY="title";
    public static final String NEWS_IMAGE_TIME_KEY="image_timestamp";
    public static final String NEWS_IMAGE_URL="image_url";
    public static final String NEWS_COLLECTION_NAME="news";

    public static final String EVENT_DATE_KEY="event_date";
    public static final String EVENT_TITLE_KEY="event_title";
    public static final String EVENT_DETAILS_KEY="event_details";
    public static final String EVENT_TIMESTAMP_KEY="event_timestamp";
    public static final String EVENT_COLLECTION_NAME="events";

    public static final String EVENT_KEY="event";
    public static final String NEWS_KEY="news";
    public static final String ACTION_ADD="add";
    public static final String ACTION_EDIT="edit";
    public static final String ACTION_DELETE="delete";
    public static final String ACTION_VIEW="view";
    public static final String ADMIN_ACTION_KEY="action";
    public static final String CALENDAR_KEY = "calendar_action";
    public static final String GTC_KEY="global_training_center";


    public static final String CHAPTER_COUNTRY="country";
    public static final String CHAPTER_NUMBER="chapter #";
    public static final String CHAPTER_SUBREGION="Sub Region";
    public static final String CHAPTER_LOCATION="location";
    public static final String CHAPTER_WEB="web";
    public static final String CHAPTER_PERSON="Person";
    public static final String CHAPTER_EMAIL="Email";
    public static final String CHAPTER_PHONE="phone";
    public static final String [] chaptersUrls={"https://firebasestorage.googleapis" +
            ".com/v0/b/ashare-ral.appspot.com/o/chapters%2F1" +
            ".jpg?alt=media&token=1a988cfb-db61-4ef9-bdb4-bda0058369c9","https://firebasestorage" +
            ".googleapis.com/v0/b/ashare-ral.appspot.com/o/chapters%2F2" +
            ".jpg?alt=media&token=1c4f5ccf-ab3d-4307-bd95-6263a6a3ea69","https://firebasestorage" +
            ".googleapis.com/v0/b/ashare-ral.appspot.com/o/chapters%2F3" +
            ".jpg?alt=media&token=2842f277-d2e2-4afd-aac8-104356a98381","https://firebasestorage" +
            ".googleapis.com/v0/b/ashare-ral.appspot.com/o/chapters%2F4" +
            ".jpg?alt=media&token=2e901631-7c4e-485e-a719-982d57f12211","https://firebasestorage" +
            ".googleapis.com/v0/b/ashare-ral.appspot.com/o/chapters%2F5.jpg?alt=media&token=eb5ff38e-91ff-4513-863e-b26278994b72","https://firebasestorage.googleapis.com/v0/b/ashare-ral.appspot.com/o/chapters%2F6.jpg?alt=media&token=d066e296-fed4-460e-9c85-90d837d0c27a","https://firebasestorage.googleapis.com/v0/b/ashare-ral.appspot.com/o/chapters%2F7.jpg?alt=media&token=744dd1a9-5e8d-4d85-9fa2-5bc50947bef8","https://firebasestorage.googleapis.com/v0/b/ashare-ral.appspot.com/o/chapters%2F8.jpg?alt=media&token=f3f37c48-1b9e-4628-9732-b96698db40eb","https://firebasestorage.googleapis.com/v0/b/ashare-ral.appspot.com/o/chapters%2F9.jpg?alt=media&token=a17e4b1b-0baa-448b-9b1e-625812331c0e",};


    public static long getDate(String stringDate){

        DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date date=dateFormat.parse(stringDate);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return -1;


    }

}
