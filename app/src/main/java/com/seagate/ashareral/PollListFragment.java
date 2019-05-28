package com.seagate.ashareral;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class PollListFragment extends Fragment {


    private String name;
    private NavController navController;

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

        ArrayList<Poll> polls = new ArrayList<>();
        ArrayList<String> questions = new ArrayList<>();
        ListView listView = view.findViewById(R.id.pollListView);
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, questions);
        listView.setAdapter(adapter);

        navController = Navigation.findNavController(view);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("polls").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                for (QueryDocumentSnapshot document : task.getResult()) {
                    polls.add(document.toObject(Poll.class));
                    questions.add(document.toObject(Poll.class).getQuestion());

                }
                adapter.notifyDataSetChanged();

            }
        });


        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Bundle bundle = getArguments();
            if (bundle.getString(Utils.POLL_ACTION).equals(Utils.POLL_CLOSE)) {
                Poll poll=polls.get(position);
                poll.setItActive(false);
                db.collection("polls").document(String.valueOf(poll.getTimestamp())).set(poll).addOnSuccessListener(aVoid ->navController.navigateUp() );
            } else {
                SharedPreferences preferences =
                        getActivity().getSharedPreferences(Utils.POLL_PREFRENCES_KEY, Context.MODE_PRIVATE);

                if (preferences.getBoolean(String.valueOf(polls.get(position).getTimestamp()), false) || !polls.get(position).isItActive()) {
                    // navigate to vote details
                     bundle = new Bundle();
                    bundle.putSerializable(Utils.POLL_KEY,
                            polls.get(position));
                    navController.navigate(R.id.toPollDetailsFragment, bundle);
                } else {
                    //navigate to poll fragment
                    showDialogue(polls.get(position));


                }
            }

        });


    }


    private void showDialogue(Poll poll) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setTitle("Insert Your Name");
        View dialogView = getLayoutInflater().inflate(R.layout.poll_alert_dialog, null);
        EditText nameEditText = dialogView.findViewById(R.id.nameEditText);
        Button button = dialogView.findViewById(R.id.dialogOk);
        button.setOnClickListener(v -> {
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
        });

        alertDialog.setView(dialogView);
        alertDialog.show();
    }
}
