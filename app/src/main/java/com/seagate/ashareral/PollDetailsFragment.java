package com.seagate.ashareral;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class PollDetailsFragment extends Fragment {

    TextView question, c1, c2, c3, progressText1, progressText2, progressText3,result1,result2,
            result3, stateTextView;
    ProgressBar progressBar1, progressBar2, progressBar3;
    ArrayList<Vote> votes;

    public PollDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_poll_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppBarLayout layout=getActivity().findViewById(R.id.appBarLayout);
        layout.setExpanded(false);

        question = view.findViewById(R.id.questionTextView);
        c1 = view.findViewById(R.id.c1);
        c2 = view.findViewById(R.id.c2);
        c3 = view.findViewById(R.id.c3);
        progressText1 = view.findViewById(R.id.progressText1);
        progressText2 = view.findViewById(R.id.progressText2);
        progressText3 = view.findViewById(R.id.progressText3);
        progressBar1 = view.findViewById(R.id.progressBar1);
        progressBar2 = view.findViewById(R.id.progressBar2);
        progressBar3 = view.findViewById(R.id.progressBar3);
        result1=view.findViewById(R.id.result1);
        result2=view.findViewById(R.id.result2);
        result3=view.findViewById(R.id.result3);

        Poll poll = (Poll) getArguments().getSerializable(Utils.POLL_KEY);
        question.setText(poll.getQuestion());
        c1.setText(poll.getChoice1());
        c2.setText(poll.getChoice2());
        c3.setText(poll.getChoice3());
        stateTextView =view.findViewById(R.id.state);
        String state=poll.isItActive() ? "Active" : "Closed";
        stateTextView.setText(state);
        votes = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("polls").document(String.valueOf(poll.getTimestamp())).collection("votes").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    votes.add(document.toObject(Vote.class));
                }
                showResult();
            }
        });
    }

    private void showResult() {
        int[] solutions = new int[3];
        String [] results=new String[]{"","",""};

        for (Vote vote : votes) {
            solutions[vote.getAnswerId()]++;
            results[vote.getAnswerId()]+=vote.getVoterName()+" , ";

        }
        progressText1.setText(String.valueOf(solutions[0]));
        progressText2.setText(String.valueOf(solutions[1]));
        progressText3.setText(String.valueOf(solutions[2]));
        progressBar1.setMax(votes.size());
        progressBar2.setMax(votes.size());
        progressBar3.setMax(votes.size());
        progressBar1.setProgress(solutions[0]);
        progressBar2.setProgress(solutions[1]);
        progressBar3.setProgress(solutions[2]);

        for(int i=0;i<results.length;i++){
            results[i].trim();
            if (!results[i].equals("")) {
                if (results[i].charAt(results[i].length() - 2) == ',') {
                    StringBuilder builder = new StringBuilder(results[i]);
                    builder.deleteCharAt(results[i].length() - 2);
                    results[i] = builder.toString();
                }
            }
        }

        result1.setText(results[0]);
        result2.setText(results[1]);
        result3.setText(results[2]);


    }
}
