package com.seagate.ashareral;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


public class CRCListFragment extends Fragment {



    public CRCListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crc_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*((CollapsingToolbarLayout)getActivity().findViewById(R.id.collapsing_toolbar_layout)).setExpandedTitleColor(Color.TRANSPARENT);*/
        ((ImageView)getActivity().findViewById(R.id.expandedImage)).setImageResource(R.drawable.crc);
        ((AppBarLayout)getActivity().findViewById(R.id.appBarLayout)).setExpanded(true);


        ViewPager viewPager=view.findViewById(R.id.view_pager);
        TabLayout tabLayout=view.findViewById(R.id.tabl_layout);
        Bundle bundle=getArguments();


        TabAdapter adapter=new TabAdapter(getChildFragmentManager(),bundle.getString(Utils.ADMIN_ACTION_KEY));



        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

    }


}
