package com.seagate.ashareral;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TabAdapter extends FragmentStatePagerAdapter {

    String adminAction;

    public TabAdapter(@NonNull FragmentManager fm,String adminAction) {
        super(fm);
        this.adminAction=adminAction;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new CRCTabFragment(true,adminAction);

            case 1:
                return new CRCTabFragment(false,adminAction);


        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }



    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Upcoming";
            case 1:
                return "Past";

        }
        return null;
    }
}
