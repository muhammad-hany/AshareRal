package com.seagate.ashareral;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;


public class PollDetailsFragment extends Fragment {

    TextView question, c1, c2, c3, progressText1, progressText2, progressText3, result1, result2,
            result3, stateTextView, totalVotes;
    ProgressBar progressBar1, progressBar2, progressBar3, loadingProgressBar;
    ArrayList<Vote> votes;
    CardView cardView;
    ConstraintLayout constraintLayout;
    LinearLayout linearLayout;
    private Poll poll;
    private int[] solutions;
    private String[] voters;

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
        ((ImageView) getActivity().findViewById(R.id.expandedImage)).setImageResource(R.drawable.poll);
        constraintLayout = view.findViewById(R.id.root);
        constraintLayout.setVisibility(View.GONE);
        loadingProgressBar = view.findViewById(R.id.progressBar4);


        question = view.findViewById(R.id.questionTextView);





        /*c1 = view.findViewById(R.id.c1);
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
        cardView=view.findViewById(R.id.card_view);
        totalVotes=view.findViewById(R.id.totalnofvotes);*/

        cardView = view.findViewById(R.id.card_view);
        totalVotes = view.findViewById(R.id.totalnofvotes);

        poll = (Poll) getArguments().getSerializable(Utils.POLL_KEY);
        question.setText(poll.getQuestion());

        /*for (String choice:poll.getChoices()){
            linearLayout.addView(itemView);
            c1 = view.findViewById(R.id.c1);
            progressText1 = itemView.findViewById(R.id.progressText1);
            progressBar1 = itemView.findViewById(R.id.progressBar1);
            result1=itemView.findViewById(R.id.result1);

            c1.setText(choice);

        }*/
        /*c1.setText(poll.getChoice1());
        c2.setText(poll.getChoice2());
        c3.setText(poll.getChoice3());*/
        stateTextView = view.findViewById(R.id.state);
        String state = poll.isItActive() ? "Active" : "Closed";
        int colorRes = poll.isItActive() ? R.color.poll_opened : R.color.poll_closed;
        cardView.setCardBackgroundColor(getActivity().getResources().getColor(colorRes));
        stateTextView.setText(state);
        votes = new ArrayList<>();




        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("polls").document(String.valueOf(poll.getTimestamp())).collection("votes").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    votes.add(document.toObject(Vote.class));

                }
                makeChoicesList(view);
                showResult();
            }
        });
    }

    private void makeChoicesList(View view) {
        getResults();
        LinearLayout linearLayout = view.findViewById(R.id.container);
        LayoutInflater inflater =
                (LayoutInflater) getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < poll.getChoices().size(); i++) {
            View itemView = inflater.inflate(R.layout.poll_details_item, null);
            ((TextView) itemView.findViewById(R.id.c1)).setText(poll.getChoices().get(i));
            ((TextView) itemView.findViewById(R.id.progressText1)).setText(String.valueOf(solutions[i]));
            ((TextView) itemView.findViewById(R.id.result1)).setText(voters[i]);

            ProgressBar progressBar =  itemView.findViewById(R.id.progressBar1);
            progressBar.setMax(votes.size());
            progressBar.setProgress(solutions[i]);
            linearLayout.addView(itemView);
        }

    }

    private void getResults() {
        solutions=new int[poll.getChoices().size()];
        voters=new String[poll.getChoices().size()];
        int i=0;

        for (Vote vote : votes) {

            solutions[vote.getAnswerId()]++;
            String name=vote.getVoterName()+" , ";
            if (voters[vote.getAnswerId()]==null){
                voters[vote.getAnswerId()]=name;
            }else {
                voters[vote.getAnswerId()] += name;
            }
            i++;
        }
    }

    private void showResult() {
        constraintLayout.setVisibility(View.VISIBLE);
        loadingProgressBar.setVisibility(View.GONE);
        if (votes.size() > 2) {
            totalVotes.setText("Total Votes : " + votes.size() + " Votes");
        } else {
            totalVotes.setText("Total Votes : " + votes.size() + " Vote");
        }

        /*int[] solutions = new int[3];
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
        result3.setText(results[2]);*/


    }
}
