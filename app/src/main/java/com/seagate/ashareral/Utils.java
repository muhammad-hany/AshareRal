package com.seagate.ashareral;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static final String NEWS_BODY_KEY = "body";
    public static final String NEWS_TITLE_KEY = "title";
    public static final String NEWS_IMAGE_TIME_KEY = "image_timestamp";
    public static final String NEWS_IMAGE_URL = "image_url";
    public static final String NEWS_COLLECTION_NAME = "news";

    public static final String EVENT_DATE_KEY = "event_date";
    public static final String EVENT_TITLE_KEY = "event_title";
    public static final String EVENT_DETAILS_KEY = "event_details";
    public static final String EVENT_TIMESTAMP_KEY = "event_timestamp";
    public static final String EVENT_COLLECTION_NAME = "events";

    public static final String EVENT_KEY = "event";
    public static final String NEWS_KEY = "news";
    public static final String ACTION_ADD = "add";
    public static final String ACTION_EDIT = "edit";
    public static final String ACTION_DELETE = "delete";
    public static final String ACTION_VIEW = "view";
    public static final String ADMIN_ACTION_KEY = "action";
    public static final String CALENDAR_KEY = "calendar_action";
    public static final String GTC_KEY = "global_training_center";


    public static final String CHAPTER_COUNTRY = "country";
    public static final String CHAPTER_NUMBER = "chapter #";
    public static final String CHAPTER_SUBREGION = "Sub Region";
    public static final String CHAPTER_LOCATION = "location";
    public static final String CHAPTER_WEB = "web";
    public static final String CHAPTER_PERSON = "Person";
    public static final String CHAPTER_EMAIL = "Email";
    public static final String CHAPTER_PHONE = "phone";


    public static final String RECYCLER_ADAPTER_TYPE = "recycler_adapter_type";
    public static final String CHAPTER_KEY = "chapter";
    public static final String COMMITTEE_KEY = "committees";
    public static final String DLS_KEY = "dls";
    public static final String OFFICERS_KEY = "officers";

    public static final String PERSON_NAME = "name";
    public static final String PERSON_BIO = "bio";
    public static final String PERSON_EMAIL = "email";
    public static final String PERSON_TITLE = "title";
    public static final String PERSON_COMMITTEE = "Committee";
    public static final String PERSON_COURSE = "Course Taught";
    public static final int[] committeesRes = {R.drawable.committee1, R.drawable.committee2,
            R.drawable.committee3, R.drawable.committee4, R.drawable.committee5,
            R.drawable.committee6, R.drawable.committee7, R.drawable.committee8};
    public static final int[] dlsRes = {R.drawable.dls1, R.drawable.dls2, R.drawable.dls3,
            R.drawable.dls4, R.drawable.dls5, R.drawable.dls6};
    public static final int[] officerRes = {R.drawable.officer1, R.drawable.officer2,
            R.drawable.officer3, R.drawable.officer4, R.drawable.officer5, R.drawable.officer6,
            R.drawable.officer7, R.drawable.officer8, R.drawable.officer9, R.drawable.officer10,
            R.drawable.officer11, R.drawable.officer12, R.drawable.officer13, R.drawable.officer14,
            R.drawable.officer15, R.drawable.officer16};

    public static final int[] chaptersRes = {R.drawable.chapter1, R.drawable.chapter2,
            R.drawable.chapter3, R.drawable.chapter4, R.drawable.chapter5, R.drawable.chapter6,
            R.drawable.chapter7, R.drawable.chapter8, R.drawable.chapter9, R.drawable.chapter10,
            R.drawable.chapter11, R.drawable.chapter12, R.drawable.chapter13, R.drawable.chapter14, R.drawable.chapter15, R.drawable.chapter16, R.drawable.chapter17, R.drawable.chapter18, R.drawable.chapter19, R.drawable.chapter20, R.drawable.chapter21, R.drawable.chapter22, R.drawable.chapter23,
            R.drawable.chapter24, R.drawable.chapter25};

    public static final String POLL_KEY = "poll";
    public static final String POLL_PREFRENCES_KEY = "ral_app_polls";
    public static final String VOTE_NAME = "vote_name";
    public static final String POLL_ACTION = "poll_action";
    public static final String POLL_OPEN = "poll_open";
    public static final String POLL_CLOSE = "poll_close";
    public static final String POLL_EDIT = "poll_edit";
    public static final String POLL_DELETE = "poll_delete";

    public static final String[] mainFragmentTitles = {"News", "Events", "CRC", "Polls", "Chapters", "Committees", "Distinguished Lecturers", "officers"};

    public static int[] mainFragmentImages = {R.drawable.news, R.drawable.events, R.drawable.crc,
            R.drawable.poll, R.drawable.chapters, R.drawable.committees, R.drawable.dls,
            R.drawable.officers};


    public static final String YOUTUBR_LINK = "https://www.youtube.com/channel/UCv553Ek9cTelUDd7prNGUrA";
    public static final String FACEBOOK_LINK = "https://www.facebook.com/ashraeral";
    public static final String TWITTER_LINK = "https://twitter.com/ASHRAE_RAL";
    public static final String LINKEDIN = "https://www.linkedin.com/groups/10393084/";
    public static final String WEB = "http://www.ashraeral.org/";

    public static long getDate(String stringDate) {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date date = dateFormat.parse(stringDate);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return -1;


    }

}
