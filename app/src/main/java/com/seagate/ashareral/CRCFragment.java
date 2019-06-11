package com.seagate.ashareral;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;



public class CRCFragment extends Fragment {


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

        ((ImageView)getActivity().findViewById(R.id.expandedImage)).setImageResource(R.drawable.crc_cover);

        ((CollapsingToolbarLayout)getActivity().findViewById(R.id.collapsing_toolbar_layout)).setExpandedTitleColor(Color.TRANSPARENT);
        ((AppBarLayout)getActivity().findViewById(R.id.appBarLayout)).setExpanded(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((CollapsingToolbarLayout)getActivity().findViewById(R.id.collapsing_toolbar_layout)).setExpandedTitleColor(Color.WHITE);
    }
}
