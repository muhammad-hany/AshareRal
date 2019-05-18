package com.seagate.ashareral;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

        ImageView imageView=view.findViewById(R.id.imageView);
        TextView title=view.findViewById(R.id.title);
        TextView body=view.findViewById(R.id.body);



        Bundle bundle=getArguments();
        Picasso.get().load(bundle.getString(Utils.NEWS_IMAGE_URL)).into(imageView);

        title.setText(bundle.getString(Utils.NEWS_TITLE_KEY));
        body.setText(bundle.getString(Utils.NEWS_BODY_KEY));
    }
}