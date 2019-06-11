package com.seagate.ashareral;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class PollAdminFragment extends Fragment {

    EditText pollQuestion,choice1,choice2,choice3,passwordEditText;
    NavController navController;
    private Bundle bundle;

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

        pollQuestion=view.findViewById(R.id.pollQuestion);
        choice1=view.findViewById(R.id.choice1);
        choice2=view.findViewById(R.id.choice2);
        choice3=view.findViewById(R.id.choice3);
        navController= Navigation.findNavController(view);
        passwordEditText=view.findViewById(R.id.passwordEcitText);

        bundle=getArguments();

        if (bundle.getString(Utils.POLL_ACTION).equals(Utils.POLL_EDIT)){
            Poll poll= (Poll) bundle.getSerializable(Utils.POLL_KEY);
            choice1.setText(poll.getChoice1());
            choice2.setText(poll.getChoice2());
            choice3.setText(poll.getChoice3());
            pollQuestion.setText(poll.getQuestion());
            passwordEditText.setText(poll.getPassword());
        }

        view.findViewById(R.id.openPoll).setOnClickListener(v -> {
            if (!pollQuestion.getText().toString().isEmpty()&&!choice1.getText().toString().isEmpty()&&!choice2.getText().toString().isEmpty()&&!choice3.getText().toString().isEmpty()&&!passwordEditText.getText().toString().isEmpty()){
                uploadPoll();
            }else {

                Toast.makeText(getContext(),"All Field must Be Filled !",Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void uploadPoll() {
        long timestamp=-1;
        if (bundle.getString(Utils.POLL_ACTION).equals(Utils.POLL_EDIT)){
            timestamp=((Poll)bundle.getSerializable(Utils.POLL_KEY)).getTimestamp();
        }else if (bundle.getString(Utils.POLL_ACTION).equals(Utils.POLL_OPEN)) {
            timestamp = System.currentTimeMillis();
        }
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        Poll poll=new Poll(pollQuestion.getText().toString(),choice1.getText().toString(),
                choice2.getText().toString(),choice3.getText().toString(),
                passwordEditText.getText().toString(),
                timestamp,true);
        db.collection("polls").document(String.valueOf(timestamp)).set(poll).addOnCompleteListener(task -> {
            Toast.makeText(getContext(),"Poll opened !",Toast.LENGTH_SHORT).show();
            navController.navigateUp();
        });

    }
}
