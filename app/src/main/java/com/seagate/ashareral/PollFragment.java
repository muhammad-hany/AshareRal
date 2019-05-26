package com.seagate.ashareral;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class PollFragment extends Fragment {


    private String name,answer;
    private int answerId;
    private NavController navController;

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
        showDialogue();
        navController= Navigation.findNavController(view);
        Poll poll= (Poll) getArguments().getSerializable(Utils.POLL_KEY);
        TextView question=view.findViewById(R.id.questionText);
        RadioGroup radioGroup=view.findViewById(R.id.radioGroup);
        RadioButton choice1=view.findViewById(R.id.choice1);
        RadioButton choice2=view.findViewById(R.id.choice2);
        RadioButton choice3=view.findViewById(R.id.choice3);

        choice1.setText(poll.getChoice1());
        choice2.setText(poll.getChoice2());
        choice3.setText(poll.getChoice3());
        question.setText(poll.getQuestion());

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {



            switch (checkedId){

                case R.id.choice1:
                    answer=choice1.getText().toString();
                    answerId=0;
                    //choice1
                    break;
                case R.id.choice2:
                    //choice2
                    answer=choice2.getText().toString();
                    answerId=1;
                    break;
                case R.id.choice3:
                    //choice3
                    answer=choice3.getText().toString();
                    answerId=2;
                    break;
            }
        });
        view.findViewById(R.id.submit).setOnClickListener(v -> {
            if (radioGroup.getCheckedRadioButtonId()!=-1){
                //upload vote
                Vote vote=new Vote(name,answer,System.currentTimeMillis(),poll.getTimestamp(),
                        answerId);

                uploadVote(vote);
            }else {
                Toast.makeText(getContext(),"Pick selection first !",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void uploadVote(Vote vote) {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("votes").document(String.valueOf(vote.getTimestamp())).set(vote).addOnSuccessListener(aVoid -> {
            navController.navigateUp();
        });
    }

    private void showDialogue() {
        AlertDialog alertDialog=new AlertDialog.Builder(getContext()).create();
        alertDialog.setCanceledOnTouchOutside(false);
        View dialogView=getLayoutInflater().inflate(R.layout.poll_alert_dialog,null);
        EditText nameEditText=dialogView.findViewById(R.id.nameEditText);
        Button button=dialogView.findViewById(R.id.dialogOk);
        button.setOnClickListener(v -> {
            if (!nameEditText.getText().toString().isEmpty()){
                name=nameEditText.getText().toString();
                alertDialog.dismiss();
            }else {
                Toast.makeText(getContext(),"Type your name first !",Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.setView(dialogView);
        alertDialog.show();
    }
}