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
