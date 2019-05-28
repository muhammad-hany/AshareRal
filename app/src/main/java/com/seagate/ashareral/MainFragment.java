package com.seagate.ashareral;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class MainFragment extends Fragment implements View.OnClickListener {

    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);

        view.findViewById(R.id.btn_news).setOnClickListener(this);
        view.findViewById(R.id.btn_events).setOnClickListener(this);
        view.findViewById(R.id.btn_admin).setOnClickListener(this);
        view.findViewById(R.id.btn_gtc).setOnClickListener(this);
        view.findViewById(R.id.btn_chapters).setOnClickListener(this);
        view.findViewById(R.id.btn_committee).setOnClickListener(this);
        view.findViewById(R.id.btn_officers).setOnClickListener(this);
        view.findViewById(R.id.btn_dls).setOnClickListener(this);
        view.findViewById(R.id.btn_poll).setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        Bundle bundle=new Bundle();
        switch (v.getId()){
            case R.id.btn_news:
                navController.navigate(R.id.toNewsFragment);
                break;
            case R.id.btn_events:
                bundle.putString(Utils.CALENDAR_KEY,Utils.EVENT_KEY);
                bundle.putString(Utils.ADMIN_ACTION_KEY,Utils.ACTION_VIEW);
                navController.navigate(R.id.toEventsFragment,bundle);
                break;
            case R.id.btn_gtc:
                bundle.putString(Utils.ADMIN_ACTION_KEY,Utils.ACTION_VIEW);
                bundle.putString(Utils.CALENDAR_KEY,Utils.GTC_KEY);
                navController.navigate(R.id.toEventsFragment,bundle);
                break;
            case R.id.btn_admin:
                navController.navigate(R.id.toAdminMainFragment);
                break;

            case R.id.btn_chapters:
                bundle.putString(Utils.RECYCLER_ADAPTER_TYPE,Utils.CHAPTER_KEY);
                navController.navigate(R.id.toChaptersFragment,bundle);
                break;
            case R.id.btn_committee:
                bundle.putString(Utils.RECYCLER_ADAPTER_TYPE,Utils.COMMITTEE_KEY);
                navController.navigate(R.id.toChaptersFragment,bundle);
                break;
            case R.id.btn_dls:
                bundle.putString(Utils.RECYCLER_ADAPTER_TYPE,Utils.DLS_KEY);
                navController.navigate(R.id.toChaptersFragment,bundle);
                break;
            case R.id.btn_officers:
                bundle.putString(Utils.RECYCLER_ADAPTER_TYPE,Utils.OFFICERS_KEY);
                navController.navigate(R.id.toChaptersFragment,bundle);
                break;
            case R.id.btn_poll:
                bundle.putString(Utils.POLL_ACTION,Utils.POLL_OPEN);
                navController.navigate(R.id.toPollListFragment,bundle);
                break;
        }
    }
}


