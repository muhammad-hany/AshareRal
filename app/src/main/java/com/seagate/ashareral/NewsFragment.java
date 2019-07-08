package com.seagate.ashareral;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class NewsFragment extends Fragment {
    private ArrayList<News> news;
    NavController navController;
    private Bundle bundle2;
    private NewsAdapter adapter;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        setToolBar();
        progressBar=view.findViewById(R.id.progress);
        progressBar.setIndeterminate(true);

        RecyclerView recyclerView = view.findViewById(R.id.new_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        news = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        adapter = new NewsAdapter(news, v -> {
            NewsAdapter.NewsViewHolder holder= (NewsAdapter.NewsViewHolder) v.getTag();

            recyclerClickAction(holder.getAdapterPosition());
        });


        recyclerView.setAdapter(adapter);

        db.collection("news").get().addOnCompleteListener(task -> {

            progressBar.setVisibility(View.INVISIBLE);
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    news.add(document.toObject(News.class));


                }

                Collections.sort(news );
                Collections.reverse(news );
                adapter.notifyDataSetChanged();
            }


        });
        navController = Navigation.findNavController(getActivity(), R.id.host_fragment);
        bundle2 = getArguments();






    }

    private void setToolBar() {
        ((ImageView)getActivity().findViewById(R.id.expandedImage)).setImageResource(R.drawable.news);

    }

    public void recyclerClickAction(int position){
        Bundle bundle = new Bundle();
        /*bundle.putString(Utils.NEWS_TITLE_KEY, news.get(position).getTitle());
        bundle.putString(Utils.NEWS_BODY_KEY, news.get(position).getBody());
        bundle.putLong(Utils.NEWS_IMAGE_TIME_KEY, news.get(position).getImageTimestamp());
        bundle.putString(Utils.NEWS_IMAGE_URL, news.get(position).getImageUri());*/

        bundle.putSerializable(Utils.NEWS_KEY,news.get(position));

        if (bundle2!=null && bundle2.getString(Utils.ADMIN_ACTION_KEY).equals(Utils.ACTION_EDIT)) {
            bundle.putString(Utils.ADMIN_ACTION_KEY,Utils.ACTION_EDIT);
            navController.navigate(R.id.toAdminNewsFragment, bundle);
        }else if (bundle2!=null && bundle2.getString(Utils.ADMIN_ACTION_KEY).equals(Utils.ACTION_DELETE)){


            FirebaseFirestore.getInstance().collection(Utils.NEWS_COLLECTION_NAME).document(String.valueOf(news.get(position).getImageTimestamp())).delete().addOnSuccessListener(aVoid ->{
                FirebaseStorage.getInstance().getReference().child("news").child(String.valueOf(news.get(position).getImageTimestamp())).delete().addOnSuccessListener(aVoid2 -> {
                    news.remove(position);
                    adapter.notifyDataSetChanged();

                });

            } );




        }else {
            navController.navigate(R.id.toNewsDetails, bundle);
        }
    }


}
