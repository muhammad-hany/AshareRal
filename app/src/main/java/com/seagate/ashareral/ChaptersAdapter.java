package com.seagate.ashareral;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChaptersAdapter extends RecyclerView.Adapter<ChaptersAdapter.MyViewHolder> {
    ArrayList<Object> objects;
    String type;


    public ChaptersAdapter(ArrayList<Object> objects,String type ) {
        this.objects =objects;
        this.type=type;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int resid;

        if (type.equals(Utils.CHAPTER_KEY)){
            resid=R.layout.chapters_list_item;
        }else {
            resid=R.layout.person_list_item;
        }

        View view= LayoutInflater.from(parent.getContext()).inflate(resid,
                parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view,type);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (type.equals(Utils.CHAPTER_KEY)){
            Chapter chapter= (Chapter) objects.get(position);
            if (chapter.getWeb().equals("")){
                holder.webTextView.setVisibility(View.GONE);
                holder.webStatic.setVisibility(View.GONE);
            }else {
                holder.webTextView.setVisibility(View.VISIBLE);
                holder.webStatic.setVisibility(View.VISIBLE);
            }
            holder.countryTextView.setText(chapter.getCountry());
            holder.chapterNumberTextView.setText(String.valueOf(chapter.getChapterNumber()));
            holder.locationTextView.setText(chapter.getLocation());
            holder.webTextView.setText(chapter.getWeb());
            holder.personTextView.setText(chapter.getPerson());
            holder.emailTextView.setText(chapter.getEmail());
            holder.phoneTextView.setText(chapter.getPhone());
            Picasso.get().load(chapter.getImageDownloadUrl()).into(holder.coverImage);
        }else if (type.equals(Utils.OFFICERS_KEY)){
            Person person= (Person) objects.get(position);
            holder.personName.setText(person.getName());
            holder.personTitle.setText(person.getTitle());
            holder.personBio.setText(person.getBio());
            holder.personImage.setImageResource(Utils.officerRes[position]);
            holder.personCommitteeCourse.setVisibility(View.GONE);
            holder.personCommitteeCourse2.setVisibility(View.GONE);
            holder.personBio.setPaintFlags(0);
        }else {
            Person person= (Person) objects.get(position);
            holder.personName.setText(person.getName());
            holder.personTitle.setText(person.getTitle());
            holder.personBio.setText(person.getBio());
            holder.personCommitteeCourse.setText(person.getCommitteeCourse());
            holder.personBio.setPaintFlags(holder.personBio.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
            holder.personBio.setPaintFlags(0);
            if (type.equals(Utils.COMMITTEE_KEY)){
                holder.personCommitteeCourse2.setText(Utils.COMMITTEE_KEY+" : ");
                holder.personImage.setImageResource(Utils.committeesRes[position]);
            }else {
                holder.personCommitteeCourse2.setText("Course Tought : ");
                holder.personImage.setImageResource(Utils.dlsRes[position]);
                if (person.getCommitteeCourse().equals("")) {
                    holder.personCommitteeCourse2.setVisibility(View.GONE);
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView countryTextView,chapterNumberTextView,locationTextView,webTextView,
        personTextView,emailTextView,phoneTextView,webStatic;
        TextView personName,personTitle,personBio,personCommitteeCourse,personCommitteeCourse2;
        ImageView coverImage,personImage;
        public MyViewHolder(@NonNull View convertView,String type) {
            super(convertView);
            if (type.equals(Utils.CHAPTER_KEY)) {
                webStatic = convertView.findViewById(R.id.person_committee_course);
                countryTextView = convertView.findViewById(R.id.person_name);
                chapterNumberTextView = convertView.findViewById(R.id.chapter_no_text);
                locationTextView = convertView.findViewById(R.id.location_text);
                webTextView = convertView.findViewById(R.id.web_text);
                personTextView = convertView.findViewById(R.id.person_text);
                emailTextView = convertView.findViewById(R.id.email_text);
                phoneTextView = convertView.findViewById(R.id.phone_text);
                coverImage = convertView.findViewById(R.id.imageView);
            }else {
                personName=convertView.findViewById(R.id.person_name);
                personBio=convertView.findViewById(R.id.person_bio);

                personTitle=convertView.findViewById(R.id.person_title);
                personCommitteeCourse=convertView.findViewById(R.id.person_committee_course);
                personCommitteeCourse2=convertView.findViewById(R.id.person_committee_course2);
                personImage=convertView.findViewById(R.id.imageView);
            }
        }
    }







}

































    /*ArrayList<Chapter> objects;
    Activity activity;
    TextView countryTextView,chapterNumberTextView,locationTextView,webTextView,personTextView,
            emailTextView,phoneTextView;
    ImageView coverImage;
    public ChaptersAdapter(ArrayList<Chapter> objects,Activity activity) {
        this.objects=objects;
        this.activity=activity;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
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
        Chapter chapter=objects.get(position);
        countryTextView.setText(chapter.getCountry());
        chapterNumberTextView.setText(String.valueOf(chapter.getChapterNumber()));
        locationTextView.setText(chapter.getLocation());
        webTextView.setText(chapter.getWeb());
        personTextView.setText(chapter.getPerson());
        emailTextView.setText(chapter.getEmail());
        phoneTextView.setText(chapter.getPhone());
        Picasso.get().load(chapter.getImageDownloadUrl()).into(coverImage);





        return convertView;
    }*/

