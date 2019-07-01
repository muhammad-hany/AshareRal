package com.seagate.ashareral;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class PollFragment extends Fragment {


    private String name, answer;
    private int answerId;
    private NavController navController;
    private List<View> boxes;
    private Poll poll;
    private ArrayList<Vote> votes;
    int uploadCounter = 0;
    int checkedButtons = 0;
    int lastCheckedId = -1;
    private LinearLayout checkboxGroup;
    RadioGroup radioGroup;

    public PollFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_poll, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boxes = new ArrayList<>();
        ((ImageView) getActivity().findViewById(R.id.expandedImage)).setImageResource(R.drawable.poll);
        ((AppBarLayout) getActivity().findViewById(R.id.appBarLayout)).setExpanded(true);

        navController = Navigation.findNavController(view);
        poll = (Poll) getArguments().getSerializable(Utils.POLL_KEY);
        name = getArguments().getString(Utils.VOTE_NAME);
        TextView question = view.findViewById(R.id.questionText);
        checkboxGroup = view.findViewById(R.id.checkboxGroup);
        radioGroup = view.findViewById(R.id.radioGroup);
        votes = new ArrayList<>();


        if (poll.isMultiChoice()) {
            radioGroup.setVisibility(View.GONE);

            int id = 0;
            for (String choice : poll.getChoices()) {
                CheckBox checkBox = new CheckBox(getContext());
                checkboxGroup.addView(checkBox);
                checkBox.setText(choice);
                checkBox.setId(id);
                boxes.add(checkBox);
                id++;
            }

        } else {
            //single choice
            int id = 0;
            radioGroup.setVisibility(View.VISIBLE);
            for (String choice : poll.getChoices()) {

                RadioButton radioButton = new RadioButton(getContext());
                radioGroup.addView(radioButton);
                radioButton.setText(choice);
                radioButton.setId(id);
                boxes.add(radioButton);
                id++;
            }

            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            });
        }


        question.setText(poll.getQuestion());
        view.findViewById(R.id.submit).setOnClickListener(v -> {
            if (poll.isMultiChoice()) {
                int i = 0;
                for (View x : boxes) {
                    CheckBox checkBox = (CheckBox) x;
                    if (checkBox.isChecked()) {
                        votes.add(new Vote(name, checkBox.getText().toString(),
                                System.currentTimeMillis()+i, poll.getTimestamp(), i));

                    }
                    i++;
                }

            } else {
                //single choice
                int y = 0;
                for (View x : boxes) {
                    RadioButton radioButton = (RadioButton) x;
                    if (radioButton.isChecked()) {
                        votes.add(new Vote(name, radioButton.getText().toString(),
                                System.currentTimeMillis()+y, poll.getTimestamp(), y));
                    }
                    y++;
                }

            }
            uploadVote();
        });


    }

    private void uploadVote() {
        if (uploadCounter < votes.size()) {
            SharedPreferences preferences = getActivity().getSharedPreferences(Utils.POLL_PREFRENCES_KEY, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(String.valueOf(poll.getTimestamp()), true);
            editor.commit();

            FirebaseFirestore db = FirebaseFirestore.getInstance();


            db.collection("polls").document(String.valueOf(votes.get(uploadCounter).getPollId())).collection(
                    "votes").document(String.valueOf(votes.get(uploadCounter).getTimestamp())).set(votes.get(uploadCounter)).addOnSuccessListener(aVoid -> {

                uploadCounter++;
                uploadVote();
            });

        } else {
            navController.navigateUp();
        }


    }


}

