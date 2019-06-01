package com.seagate.ashareral;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class NewsFragment extends Fragment {
    private ArrayList<News> news;
    NavController navController;
    private AlertDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppBarLayout layout=getActivity().findViewById(R.id.appBarLayout);
        layout.setExpanded(false);

        ListView listView = view.findViewById(R.id.new_list);
        news = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final NewsAdapter adapter = new NewsAdapter(getActivity(), news);
        listView.setAdapter(adapter);
        dialog = new AlertDialog.Builder(getContext()).setMessage("Loading ..").create();
        dialog.show();
        db.collection("news").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                dialog.hide();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        news.add(document.toObject(News.class));


                    }

                    adapter.notifyDataSetChanged();
                }


            }
        });
        navController = Navigation.findNavController(getActivity(), R.id.host_fragment);
        final Bundle bundle2 = getArguments();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString(Utils.NEWS_TITLE_KEY, news.get(position).getTitle());
                bundle.putString(Utils.NEWS_BODY_KEY, news.get(position).getBody());
                bundle.putLong(Utils.NEWS_IMAGE_TIME_KEY, news.get(position).getImageTimestamp());
                bundle.putString(Utils.NEWS_IMAGE_URL, news.get(position).getImageUri());

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
        });


    }


}
