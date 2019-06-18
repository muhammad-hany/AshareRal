package com.seagate.ashareral;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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
        /*cardView=view.findViewById(R.id.lastNews);
        lastNewsTitle =view.findViewById(R.id.lastNewsTitle);*/

       // getLastNews();

        setToolBar();
        RecyclerView recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        NewsAdapter adapter=new NewsAdapter(v -> {
            NewsAdapter.NewsViewHolder holder= (NewsAdapter.NewsViewHolder) v.getTag();
            onRecyclerClick(holder.getAdapterPosition());
        });

        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.facebook).setOnClickListener(this);
        view.findViewById(R.id.youtube).setOnClickListener(this);
        view.findViewById(R.id.twitter).setOnClickListener(this);
        view.findViewById(R.id.linkedin).setOnClickListener(this);
        view.findViewById(R.id.web).setOnClickListener(this);




    }

    private void setToolBar() {


        Toolbar toolbar=getActivity().findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        ImageView imageView=getActivity().findViewById(R.id.expandedImage);
        imageView.setImageResource(R.drawable.cover);



    }





    public void onRecyclerClick(int i) {
        Bundle bundle=new Bundle();
        switch (i){
            case 0:
                navController.navigate(R.id.toNewsFragment);
                break;
            case 1:
                bundle.putString(Utils.CALENDAR_KEY,Utils.EVENT_KEY);
                bundle.putString(Utils.ADMIN_ACTION_KEY,Utils.ACTION_VIEW);
                navController.navigate(R.id.toEventsFragment,bundle);
                break;
            case 2:
                bundle.putString(Utils.ADMIN_ACTION_KEY,Utils.ACTION_VIEW);
                bundle.putString(Utils.CALENDAR_KEY,Utils.GTC_KEY);
                navController.navigate(R.id.toGTCFragment,bundle);
                break;


            case 4:
                bundle.putString(Utils.RECYCLER_ADAPTER_TYPE,Utils.CHAPTER_KEY);
                navController.navigate(R.id.toChaptersFragment,bundle);
                break;
            case 5:
                bundle.putString(Utils.RECYCLER_ADAPTER_TYPE,Utils.COMMITTEE_KEY);
                navController.navigate(R.id.toCommitteeFragment,bundle);
                break;
            case 6:
                bundle.putString(Utils.RECYCLER_ADAPTER_TYPE,Utils.DLS_KEY);
                navController.navigate(R.id.toDlsFragment,bundle);
                break;
            case 7:
                bundle.putString(Utils.RECYCLER_ADAPTER_TYPE,Utils.OFFICERS_KEY);
                navController.navigate(R.id.toOfficerFragment,bundle);
                break;
            case 3:
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

    @Override
    public void onClick(View v) {
        String url="";
        switch (v.getId()){
            case R.id.facebook:
                url=Utils.FACEBOOK_LINK;
                break;
            case R.id.youtube:
                url=Utils.YOUTUBR_LINK;
                break;
            case R.id.linkedin:
                url=Utils.LINKEDIN;
                break;
            case R.id.web:
                url=Utils.WEB;
                break;
            case R.id.twitter:
                url=Utils.TWITTER_LINK;
                break;

        }

        Intent i =new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}


