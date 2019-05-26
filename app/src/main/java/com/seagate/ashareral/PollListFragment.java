package com.seagate.ashareral;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class PollListFragment extends Fragment {


    public PollListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_poll_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<Poll> polls=new ArrayList<>();
        ArrayList<String> questions=new ArrayList<>();
        ListView listView=view.findViewById(R.id.pollListView);
        ArrayAdapter adapter=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,questions);
        listView.setAdapter(adapter);

        NavController navController= Navigation.findNavController(view);

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("polls").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                for (QueryDocumentSnapshot document: task.getResult()) {
                    polls.add(document.toObject(Poll.class));
                    questions.add(document.toObject(Poll.class).getQuestion());

                }
                adapter.notifyDataSetChanged();

            }
        });
        Bundle bundle=new Bundle();

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            bundle.putSerializable(Utils.POLL_KEY,polls.get(position));
            navController.navigate(R.id.toPollFragment,bundle);
        });



    }
}
