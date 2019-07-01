package com.seagate.ashareral;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class PollDetailsAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<Vote> votes;
    Poll poll;
    int[] solutions;
    String[] voters;

    TextView choice;
    TextView progressText;
    ProgressBar progressBar;
    TextView result;

    public PollDetailsAdapter(Activity activity, ArrayList<Vote> votes,Poll poll) {
        this.activity = activity;
        this.votes = votes;
        this.poll=poll;
        getResults();
    }

    private void getResults() {
        solutions=new int[poll.getChoices().size()];
        voters=new String[poll.getChoices().size()];
        int i=0;
        for (Vote vote : votes) {

            solutions[vote.getAnswerId()]++;
            String name;
            if (i==votes.size()-1){
                name=vote.getVoterName();
            }else {
                name=vote.getVoterName()+" , ";
            }
            voters[vote.getAnswerId()]+=name;
            i++;
        }
    }

    @Override
    public int getCount() {
        return poll.getChoices().size();
    }

    @Override
    public Object getItem(int position) {
        return poll.getChoices().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.poll_details_item,
                    parent, false);
            choice = view.findViewById(R.id.c1);
            progressText = view.findViewById(R.id.progressText1);
            progressBar = view.findViewById(R.id.progressBar1);
            result = view.findViewById(R.id.result1);

        }

        choice.setText(poll.getChoices().get(position));
        progressText.setText(String.valueOf(solutions[position]));
        progressBar.setMax(votes.size());
        progressBar.setProgress(solutions[position]);
        result.setText(voters[position]);


        return view;
    }
}
