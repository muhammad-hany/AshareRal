package com.seagate.ashareral;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class MainFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    CardView cardView;
    TextView lastNewsTitle;
    private News lastNews;

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
        cardView=view.findViewById(R.id.lastNews);
        lastNewsTitle =view.findViewById(R.id.lastNewsTitle);

       // getLastNews();

        setToolBar();


        view.findViewById(R.id.btn_news).setOnClickListener(this);
        view.findViewById(R.id.btn_events).setOnClickListener(this);
        view.findViewById(R.id.btn_admin).setOnClickListener(this);
        view.findViewById(R.id.btn_gtc).setOnClickListener(this);
        view.findViewById(R.id.btn_chapters).setOnClickListener(this);
        view.findViewById(R.id.btn_committee).setOnClickListener(this);
        view.findViewById(R.id.btn_officers).setOnClickListener(this);
        view.findViewById(R.id.btn_dls).setOnClickListener(this);
        view.findViewById(R.id.btn_poll).setOnClickListener(this);
        view.findViewById(R.id.lastNews).setOnClickListener(this);



    }

    private void setToolBar() {

        AppBarLayout layout=getActivity().findViewById(R.id.appBarLayout);
        layout.setExpanded(true);
        Toolbar toolbar=getActivity().findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout=
                getActivity().findViewById(R.id.collapsing_toolbar_layout);

        AppBarLayout.LayoutParams params= (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();

        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL| AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        collapsingToolbarLayout.setLayoutParams(params);
        toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));



    }


    @Override
    public void onClick(View v) {
        Bundle bundle=new Bundle();
        switch (v.getId()){
            case R.id.lastNews:
                bundle.putString(Utils.NEWS_TITLE_KEY, lastNews.getTitle());
                bundle.putString(Utils.NEWS_BODY_KEY, lastNews.getBody());
                bundle.putLong(Utils.NEWS_IMAGE_TIME_KEY, lastNews.getImageTimestamp());
                bundle.putString(Utils.NEWS_IMAGE_URL, lastNews.getImageUri());
                navController.navigate(R.id.toNewsDetails,bundle);


                break;
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
                navController.navigate(R.id.toGTCFragment,bundle);
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
                navController.navigate(R.id.toCommitteeFragment,bundle);
                break;
            case R.id.btn_dls:
                bundle.putString(Utils.RECYCLER_ADAPTER_TYPE,Utils.DLS_KEY);
                navController.navigate(R.id.toDlsFragment,bundle);
                break;
            case R.id.btn_officers:
                bundle.putString(Utils.RECYCLER_ADAPTER_TYPE,Utils.OFFICERS_KEY);
                navController.navigate(R.id.toOfficerFragment,bundle);
                break;
            case R.id.btn_poll:
                bundle.putString(Utils.POLL_ACTION,Utils.POLL_OPEN);
                navController.navigate(R.id.toPollListFragment,bundle);
                break;
        }
    }

    private void getLastNews() {
        ArrayList<News> news=new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("news").get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    news.add(document.toObject(News.class));

                }
                lastNews=news.get(news.size()-1);
                cardView.setVisibility(View.VISIBLE);
                lastNewsTitle.setText(lastNews.getTitle());

            }

        });
    }
}


