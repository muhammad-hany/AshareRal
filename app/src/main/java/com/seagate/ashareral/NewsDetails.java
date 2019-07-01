package com.seagate.ashareral;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class NewsDetails extends Fragment {


    public NewsDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_details, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppBarLayout layout=getActivity().findViewById(R.id.appBarLayout);
        layout.setExpanded(false);

        ImageView imageView=view.findViewById(R.id.news_image_list_item);
        TextView title=view.findViewById(R.id.title);
        TextView body=view.findViewById(R.id.body);
        TextView date=view.findViewById(R.id.date);



        Bundle bundle=getArguments();
        News news= (News) bundle.getSerializable(Utils.NEWS_KEY);
        Picasso.get().load(news.getImageUri()).into(imageView);

        title.setText(news.getTitle());
        body.setText(news.getBody());
        date.setText(news.getDate());
    }
}
