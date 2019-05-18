package com.seagate.ashareral;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChaptersAdapter extends BaseAdapter {
    ArrayList<Chapter> chapters;
    Activity activity;
    TextView countryTextView,chapterNumberTextView,locationTextView,webTextView,personTextView,
            emailTextView,phoneTextView;
    ImageView coverImage;
    public ChaptersAdapter(ArrayList<Chapter> chapters,Activity activity) {
        this.chapters=chapters;
        this.activity=activity;
    }

    @Override
    public int getCount() {
        return chapters.size();
    }

    @Override
    public Object getItem(int position) {
        return chapters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.chapters_list_item, parent, false);
            countryTextView= convertView.findViewById(R.id.country_text);
            chapterNumberTextView=convertView.findViewById(R.id.chapter_no_text);
            locationTextView=convertView.findViewById(R.id.location_text);
            webTextView=convertView.findViewById(R.id.web_text);
            personTextView=convertView.findViewById(R.id.person_text);
            emailTextView=convertView.findViewById(R.id.email_text);
            phoneTextView=convertView.findViewById(R.id.phone_text);
            coverImage=convertView.findViewById(R.id.imageView);

        }
        Chapter chapter=chapters.get(position);
        countryTextView.setText(chapter.getCountry());
        chapterNumberTextView.setText(chapter.getChapterNumber());
        locationTextView.setText(chapter.getLocation());
        webTextView.setText(chapter.getWeb());
        personTextView.setText(chapter.getPerson());
        emailTextView.setText(chapter.getEmail());
        phoneTextView.setText(chapter.getPhone());




        return convertView;
    }
}
