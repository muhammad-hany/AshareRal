package com.seagate.ashareral;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class AdminMainFragment extends Fragment implements View.OnClickListener {


    NavController navController;

    public AdminMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.news).setOnClickListener(this);
        view.findViewById(R.id.events).setOnClickListener(this);
        view.findViewById(R.id.gtc).setOnClickListener(this);
        view.findViewById(R.id.poll).setOnClickListener(this);
        navController = Navigation.findNavController(view);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.news:
                makeAlertDialogue(Utils.NEWS_KEY);
                break;
            case R.id.gtc:
                makeAlertDialogue(Utils.GTC_KEY);
                break;
            case R.id.events:
                makeAlertDialogue(Utils.EVENT_KEY);
                break;
            case R.id.poll:
                makePollAlertDialogue();
                break;
        }
    }

    private void makePollAlertDialogue() {
        new AlertDialog.Builder(getContext()).setTitle("Choose Action").setItems(new String [] {
                "Open Poll","Close Poll"}, (dialog, which) -> {
            switch (which){
                case 0:
                    navController.navigate(R.id.toPollAdminFragment);
                    break;

            }
        }).create().show();
    }

    public void makeAlertDialogue(final String arg) {
        new AlertDialog.Builder(getContext()).setTitle("Choose Action").setItems(new String[]{
                "Add", "Edit", "Delete"}, (dialog, which) -> {
            Bundle bundle = new Bundle();
            switch (which) {
                case 0:
                    bundle.putString(Utils.ADMIN_ACTION_KEY, Utils.ACTION_ADD);
                    if (arg.equals(Utils.NEWS_KEY)) {
                        navController.navigate(R.id.toAdminNewsFragment, bundle);

                    } else if (arg.equals(Utils.EVENT_KEY)) {
                        bundle.putString(Utils.CALENDAR_KEY, Utils.EVENT_KEY);
                        navController.navigate(R.id.toAdminEventFragment, bundle);
                    } else if (arg.equals(Utils.GTC_KEY)) {
                        bundle.putString(Utils.CALENDAR_KEY, Utils.GTC_KEY);
                        navController.navigate(R.id.toAdminEventFragment, bundle);
                    }

                    break;
                case 1:
                    bundle.putString(Utils.ADMIN_ACTION_KEY, Utils.ACTION_EDIT);

                    if (arg.equals(Utils.NEWS_KEY)) {
                        navController.navigate(R.id.toNewsFragment, bundle);

                    } else if (arg.equals(Utils.EVENT_KEY)) {
                        bundle.putString(Utils.CALENDAR_KEY, Utils.EVENT_KEY);
                        navController.navigate(R.id.toEventsFragment, bundle);
                    }else if (arg.equals(Utils.GTC_KEY)){
                        bundle.putString(Utils.CALENDAR_KEY, Utils.GTC_KEY);
                        navController.navigate(R.id.toEventsFragment, bundle);
                }
                    break;
                case 2:

                    bundle.putString(Utils.ADMIN_ACTION_KEY, Utils.ACTION_DELETE);

                    if (arg.equals(Utils.NEWS_KEY)) {
                        navController.navigate(R.id.toNewsFragment, bundle);

                    } else if (arg.equals(Utils.EVENT_KEY)){
                        bundle.putString(Utils.CALENDAR_KEY, Utils.EVENT_KEY);
                        navController.navigate(R.id.toEventsFragment, bundle);
                    }else if (arg.equals(Utils.GTC_KEY)){
                        bundle.putString(Utils.CALENDAR_KEY, Utils.GTC_KEY);
                        navController.navigate(R.id.toEventsFragment, bundle);
                    }
                    break;

            }


        }).create().show();
    }
}
