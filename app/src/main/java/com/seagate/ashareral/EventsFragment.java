package com.seagate.ashareral;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class EventsFragment extends Fragment {

    AlertDialog dialog;
    boolean isItNewEvent = true;
    private Bundle bundle;
    private NavController navController;
    private ArrayList<EventDay> eventDays;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppBarLayout layout=getActivity().findViewById(R.id.appBarLayout);
        layout.setExpanded(false);

        final CalendarView calendarView = view.findViewById(R.id.calendarView);


        navController = Navigation.findNavController(getActivity(), R.id.host_fragment);
        bundle = getArguments();
        if (bundle != null && bundle.getString(Utils.ADMIN_ACTION_KEY).equals(Utils.ACTION_EDIT)) {
            isItNewEvent = false;
        }

        dialog = new AlertDialog.Builder(getContext()).setMessage("Loading ..").create();
        dialog.show();
        final TextView eventDetailsText = view.findViewById(R.id.eventDetailsText);
        final TextView eventTitleText = view.findViewById(R.id.eventTitleText);
        final ArrayList<Long> markedDates = new ArrayList<>();
        final ArrayList<Long> timestamps = new ArrayList<>();
        final ArrayList<String> eventsDetails = new ArrayList<>();
        final ArrayList<String> eventTitles = new ArrayList<>();
        String collectionName = bundle.getString(Utils.CALENDAR_KEY);


        FirebaseFirestore.getInstance().collection(collectionName).get().addOnCompleteListener(task -> {
            dialog.hide();
            eventDays = new ArrayList<>();
            for (QueryDocumentSnapshot document : task.getResult()) {
                long millsecond = (Long) document.getData().get(Utils.EVENT_DATE_KEY);
                Date date = new Date(millsecond);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);


                eventDays.add(new EventDay(calendar, R.drawable.ic_events));
                //   calendarView.markDate(year, month, day);
                markedDates.add(millsecond);
                eventsDetails.add((String) document.getData().get(Utils.EVENT_DETAILS_KEY));
                eventTitles.add((String) document.getData().get(Utils.EVENT_TITLE_KEY));
                timestamps.add((Long) document.getData().get(Utils.EVENT_TIMESTAMP_KEY));

            }
            calendarView.setEvents(eventDays);


        });

        calendarView.setOnDayClickListener(eventDay -> {


            int i = 0;
            for (long markedDate : markedDates) {


                long selectedDate = eventDay.getCalendar().getTimeInMillis();

                if (selectedDate == markedDate) {

                    switch (bundle.getString(Utils.ADMIN_ACTION_KEY)) {

                        case Utils.ACTION_EDIT:
                            bundle.putString(Utils.ADMIN_ACTION_KEY, Utils.ACTION_EDIT);
                            bundle.putLong(Utils.EVENT_TIMESTAMP_KEY, timestamps.get(i));
                            bundle.putLong(Utils.EVENT_DATE_KEY, selectedDate);
                            bundle.putString(Utils.EVENT_TITLE_KEY, eventTitles.get(i));
                            bundle.putString(Utils.EVENT_DETAILS_KEY, eventsDetails.get(i));
                            navController.navigate(R.id.action_admin_event, bundle);
                            break;
                        case Utils.ACTION_DELETE:
                            FirebaseFirestore.getInstance().collection(bundle.getString(Utils.CALENDAR_KEY)).document(String.valueOf(timestamps.get(i))).delete().addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "delete success", Toast.LENGTH_SHORT).show();
                            }).addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "delete failed", Toast.LENGTH_SHORT).show();
                            });
                            eventDays.remove(i);
                            calendarView.setEvents(eventDays);
                            break;
                        case Utils.ACTION_VIEW:
                            eventDetailsText.setText(eventsDetails.get(i));
                            eventDetailsText.setVisibility(View.VISIBLE);

                            eventTitleText.setText(eventTitles.get(i));
                            eventTitleText.setVisibility(View.VISIBLE);
                            break;

                    }


                    //TODO :: handle long text in event body to be scrollable
                    //TODO :: handle thumbnail images in mainFragment "change the resolution !
                    //TODO :: make all upload buttons overflow buttons


                }
                i++;
            }


        });


    }
}
