package com.seagate.ashareral;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class PollAdminFragment extends Fragment implements View.OnClickListener {

    EditText pollQuestion, choice1, choice2, passwordEditText;
    NavController navController;
    LinearLayout linearLayout;
    private List<EditText> choicesViews;
    private Bundle bundle;
    private ProgressBar progressBar;
    boolean multiChoice;
    private RadioGroup radioGroup;

    public PollAdminFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_poll_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        choicesViews = new ArrayList<>();
        pollQuestion = view.findViewById(R.id.pollQuestion);
        choice1 = view.findViewById(R.id.choice1);
        choice2 = view.findViewById(R.id.choice2);
        choicesViews.add(choice1);
        choicesViews.add(choice2);
        navController = Navigation.findNavController(view);
        passwordEditText = view.findViewById(R.id.passwordEcitText);

        linearLayout = view.findViewById(R.id.container);
        progressBar=view.findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);

        bundle = getArguments();

        view.findViewById(R.id.openPoll).setOnClickListener(this);
        view.findViewById(R.id.addChoice).setOnClickListener(this);

        radioGroup=view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.singleChoice:
                    multiChoice =false;
                    break;
                case R.id.multiChoice:
                    multiChoice =true;
            }
        });

        if (bundle.getString(Utils.POLL_ACTION).equals(Utils.POLL_EDIT)) {
            Poll poll = (Poll) bundle.getSerializable(Utils.POLL_KEY);
            choice1.setVisibility(View.GONE);
            choice2.setVisibility(View.GONE);
            choicesViews.clear();
            int i=0;
            for (String choice:poll.getChoices()){
                choicesViews.get(i).setText(choice);
                i++;
            }
            pollQuestion.setText(poll.getQuestion());
            passwordEditText.setText(poll.getPassword());
            if (poll.isMultiChoice()){
                radioGroup.check(R.id.multiChoice);
            }else {
                radioGroup.check(R.id.singleChoice);
            }
        }




    }

    private void uploadPoll() {
        progressBar.setVisibility(View.VISIBLE);
        long timestamp = -1;
        if (bundle.getString(Utils.POLL_ACTION).equals(Utils.POLL_EDIT)) {
            timestamp = ((Poll) bundle.getSerializable(Utils.POLL_KEY)).getTimestamp();
        } else if (bundle.getString(Utils.POLL_ACTION).equals(Utils.POLL_OPEN)) {
            timestamp = System.currentTimeMillis();
        }
        List<String> strings=new ArrayList<>();
        for (EditText editText: choicesViews){
            strings.add(editText.getText().toString());
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Poll poll = new Poll(pollQuestion.getText().toString(),
                passwordEditText.getText().toString(),
                timestamp, true,strings,multiChoice,false);
        db.collection("polls").document(String.valueOf(timestamp)).set(poll).addOnCompleteListener(task -> {
            Toast.makeText(getContext(), "Poll opened !", Toast.LENGTH_SHORT).show();

            navController.navigateUp();
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addChoice:
                makeNewChoiceText();
                break;

            case R.id.openPoll:
                if (checkAllFields()) {
                    uploadPoll();
                } else {
                    Toast.makeText(getContext(), "All Field must Be Filled !", Toast.LENGTH_SHORT).show();
                }

        }
    }

    private EditText makeNewChoiceText(){
        EditText editText = new EditText(getContext());

        editText.setHint("Choice" + (choicesViews.size() + 1));
        choicesViews.add(editText);
        linearLayout.addView(editText);
        return editText;
    }

    private boolean checkAllFields() {
        if (!pollQuestion.getText().toString().isEmpty() && !choice1.getText().toString().isEmpty() && !choice2.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty()&&radioGroup.getCheckedRadioButtonId()!=-1) {
            return true;
        }else {
            Toast.makeText(getContext(),"You must check all fields",Toast.LENGTH_SHORT).show();
            return false;
        }

    }
}
