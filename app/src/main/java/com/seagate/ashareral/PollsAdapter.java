package com.seagate.ashareral;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;

public class PollsAdapter extends BaseAdapter{

    private ArrayList<Poll> polls;
    private Activity activity;

    TextView pollTitle,pollState;
    CardView cardView;


    public PollsAdapter(ArrayList<Poll> polls, Activity activity) {
        this.polls = polls;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return polls.size();
    }

    @Override
    public Object getItem(int position) {
        return polls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null){
            convertView=activity.getLayoutInflater().inflate(R.layout.polls_item_list,parent,false);
            pollState=convertView.findViewById(R.id.pollState);
            pollTitle=convertView.findViewById(R.id.pollTitle);
            cardView=convertView.findViewById(R.id.card_view);
        }
        pollTitle.setText(polls.get(position).getQuestion());
        String state=polls.get(position).isItActive() ? "Opened" : "Closed" ;
        pollState.setText(state);

        int color=polls.get(position).isItActive() ?
                activity.getResources().getColor(R.color.poll_opened) :
                 activity.getResources().getColor(R.color.poll_closed);
        cardView.setCardBackgroundColor(color);

        return convertView;
    }
}
