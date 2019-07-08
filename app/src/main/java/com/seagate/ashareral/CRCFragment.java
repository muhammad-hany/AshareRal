package com.seagate.ashareral;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;



public class CRCFragment extends Fragment {


    private CRC crc;

    public CRCFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crc, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ImageView coverImage=getActivity().findViewById(R.id.expandedImage);

        ((CollapsingToolbarLayout)getActivity().findViewById(R.id.collapsing_toolbar_layout)).setExpandedTitleColor(Color.TRANSPARENT);
        ((AppBarLayout)getActivity().findViewById(R.id.appBarLayout)).setExpanded(true);




        Bundle bundle=getArguments();
        crc= (CRC) bundle.getSerializable(Utils.CRC_KEY);
        Picasso.get().load(crc.getCoverimageDownloadLink()).placeholder(R.drawable.placeholder).into(coverImage);

        setupViews(view);





    }

    private void setupViews(View view) {
        TextView title = view.findViewById(R.id.title);
        TextView describtion = view.findViewById(R.id.describtion);
        LinearLayout sponsorContainer = view.findViewById(R.id.sponsorcontainer);

        title.setText(crc.getTitle());
        describtion.setText(crc.describtion);
        if (crc.getSponser_download_links() != null) {
            for (String link : crc.getSponser_download_links()) {
                ImageView imageView = new ImageView(getContext());
                imageView.setAdjustViewBounds(true);

                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);

                params.setMargins(0, 40, 0, 40);
                imageView.setLayoutParams(params);
                sponsorContainer.addView(imageView);
                Picasso.get().load(link).into(imageView);
            }
        }else {
            TextView sponsorLabel=view.findViewById(R.id.sponsorLabel);
            sponsorLabel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((CollapsingToolbarLayout)getActivity().findViewById(R.id.collapsing_toolbar_layout)).setExpandedTitleColor(Color.WHITE);
    }




}
