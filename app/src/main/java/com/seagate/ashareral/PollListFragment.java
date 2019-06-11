package com.seagate.ashareral;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class PollListFragment extends Fragment {


    private String name;
    private NavController navController;
    ProgressBar progressBar;
    private Bundle bundle;
    private FirebaseFirestore db;

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

        ((ImageView)getActivity().findViewById(R.id.expandedImage)).setImageResource(R.drawable.poll);
        progressBar=view.findViewById(R.id.progressBar);
        bundle = getArguments();
        ArrayList<Poll> polls = new ArrayList<>();
        ArrayList<String> questions = new ArrayList<>();
        ListView listView = view.findViewById(R.id.pollListView);
        PollsAdapter adapter=new PollsAdapter(polls,getActivity());

        listView.setAdapter(adapter);

        navController = Navigation.findNavController(view);


        db = FirebaseFirestore.getInstance();
        db.collection("polls").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                for (QueryDocumentSnapshot document : task.getResult()) {
                    Poll poll=document.toObject(Poll.class);
                    if (bundle.getString(Utils.POLL_ACTION).equals(Utils.POLL_EDIT)&& !poll.isItActive()){
                        continue;
                    }
                    polls.add(poll);
                    questions.add(document.toObject(Poll.class).getQuestion());

                }
                Collections.reverse(polls);
                Collections.reverse(questions);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

            }
        });


        listView.setOnItemClickListener((parent, view1, position, id) -> {

            if (bundle.getString(Utils.POLL_ACTION).equals(Utils.POLL_CLOSE)) {
                Poll poll=polls.get(position);
                poll.setItActive(false);
                db.collection("polls").document(String.valueOf(poll.getTimestamp())).set(poll).addOnSuccessListener(aVoid ->navController.navigateUp() );
            } else if (bundle.getString(Utils.POLL_ACTION).equals(Utils.POLL_OPEN)){
                SharedPreferences preferences = getActivity().getSharedPreferences(Utils.POLL_PREFRENCES_KEY,
                        Context.MODE_PRIVATE);

                if (preferences.getBoolean(String.valueOf(polls.get(position).getTimestamp()), false) || !polls.get(position).isItActive()) {
                    // navigate to vote details

                    bundle.putSerializable(Utils.POLL_KEY,
                            polls.get(position));
                    navController.navigate(R.id.toPollDetailsFragment, bundle);
                } else {
                    //navigate to poll fragment
                    showDialogue(polls.get(position));


                }
            }else if (bundle.getString(Utils.POLL_ACTION).equals(Utils.POLL_EDIT)){


                bundle.putSerializable(Utils.POLL_KEY,polls.get(position));
                navController.navigate(R.id.toPollAdminFragment, bundle);
            }else if (bundle.getString(Utils.POLL_ACTION).equals(Utils.POLL_DELETE)){
                Poll poll=polls.get(position);
                db.collection("polls").document(String.valueOf(poll.getTimestamp())).delete().addOnSuccessListener(aVoid -> {
                    polls.remove(position);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(),"Poll Deleted !", Toast.LENGTH_SHORT);
                }).addOnFailureListener(e -> {
                    Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT);
                }
                );
            }

        });


    }


    private void showDialogue(Poll poll) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setTitle("Insert Your Name");
        View dialogView = getLayoutInflater().inflate(R.layout.poll_alert_dialog, null);
        EditText nameEditText = dialogView.findViewById(R.id.nameEditText);
        EditText passwordEditText=dialogView.findViewById(R.id.passwordEditText);
        Button button = dialogView.findViewById(R.id.dialogOk);
        button.setOnClickListener(v -> {
            if (passwordEditText.getText().toString().equals(poll.getPassword())) {
                if (!nameEditText.getText().toString().isEmpty()) {
                    name = nameEditText.getText().toString();
                    alertDialog.dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Utils.POLL_KEY, poll);
                    bundle.putString(Utils.VOTE_NAME, name);
                    navController.navigate(R.id.toPollFragment, bundle);
                } else {
                    Toast.makeText(getContext(), "Type your name first !", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getContext(), "Wrong Password !", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.setView(dialogView);
        alertDialog.show();
    }
}
