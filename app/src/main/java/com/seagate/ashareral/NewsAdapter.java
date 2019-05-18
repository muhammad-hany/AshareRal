package com.seagate.ashareral;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends BaseAdapter {

  ImageView imageView;
  TextView title;
  Activity activity;
  ArrayList<News> news;

  public NewsAdapter(Activity activity, ArrayList<News> news) {
    this.activity = activity;
    this.news = news;
  }

  @Override
  public int getCount() {
    return news.size();
  }

  @Override
  public Object getItem(int position) {
    return news.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView==null){
      convertView=activity.getLayoutInflater().inflate(R.layout.new_list_item,parent,false);
      imageView=convertView.findViewById(R.id.news_image_list_item);
      title=convertView.findViewById(R.id.title);

    }

    Picasso.get().load(news.get(position).getImageUri()).into(imageView);
    title.setText(news.get(position).getTitle());

    return convertView;
  }
}