package com.seagate.ashareral;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {



    private ArrayList<News> news;
    private View.OnClickListener listener;
    private boolean isItMainFragment=false;

    public NewsAdapter(ArrayList<News> news,View.OnClickListener listener) {
        this.news = news;
        this.listener=listener;
    }

    public NewsAdapter(View.OnClickListener listener) {
        isItMainFragment=true;
        this.listener=listener;
    }


    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int res;
        if (isItMainFragment) {
            res=R.layout.main_fragment_list_item;
        }else {
            res=R.layout.new_list_item;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(res, parent, false);
        NewsViewHolder holder=new NewsViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        if (isItMainFragment){
            holder.imageView.setImageResource(Utils.mainFragmentImages[position]);
            holder.title.setText(Utils.mainFragmentTitles[position]);
        }else {
            Picasso.get().load(news.get(position).getImageUri()).placeholder(R.drawable.placeholder).into(holder.imageView);
            holder.title.setText(news.get(position).getTitle());
        }


    }

    @Override
    public int getItemCount() {
        if (isItMainFragment){
            return Utils.mainFragmentTitles.length;
        }else {
            return news.size();
        }
    }



    public class NewsViewHolder extends RecyclerView.ViewHolder  {
        ImageView imageView;
        TextView title;


        public NewsViewHolder(@NonNull View convertView) {
            super(convertView);

            imageView=convertView.findViewById(R.id.coverImage);
            title=convertView.findViewById(R.id.title);

            convertView.setTag(this);
            convertView.setOnClickListener(listener);


        }



    }





























 /* ImageView imageView;
  TextView position;
  Activity activity;
  ArrayList<news> news;

  public NewsAdapter(Activity activity, ArrayList<news> news) {
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
      position=convertView.findViewById(R.id.position);

    }

    Picasso.get().load(news.get(position).getImageUri()).into(imageView);
    position.setText(news.get(position).getTitle());

    return convertView;
  }*/
}