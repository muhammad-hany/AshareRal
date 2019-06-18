package com.seagate.ashareral;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class EventsFragment extends Fragment {

    AlertDialog dialog;
    boolean isItNewEvent = true;
    private Bundle bundle;
    private NavController navController;
    private ArrayList<EventDay> eventDays;
    private ArrayList<String> eventTitles;
    private ArrayList<String> markedDates;
    private ArrayList<Long> timestamps;
    private ArrayList<String> eventsDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((ImageView) getActivity().findViewById(R.id.expandedImage)).setImageResource(R.drawable.events);

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
        markedDates = new ArrayList<>();
        timestamps = new ArrayList<>();
        eventsDetails = new ArrayList<>();
        eventTitles = new ArrayList<>();
        String collectionName = bundle.getString(Utils.CALENDAR_KEY);


        FirebaseFirestore.getInstance().collection(collectionName).get().addOnCompleteListener(task -> {
            dialog.hide();
            eventDays = new ArrayList<>();
            for (QueryDocumentSnapshot document : task.getResult()) {
                String stringDate = (String) document.getData().get(Utils.EVENT_DATE_KEY);
                int[] date=Utils.StringToDateArray(stringDate);

                Calendar calendar = Calendar.getInstance();
                calendar.set(date[2],date[1]-1,date[0]);



                eventDays.add(new EventDay(calendar, R.drawable.ic_events_drawer));
                //   calendarView.markDate(year, month, day);
                markedDates.add(stringDate);
                eventsDetails.add((String) document.getData().get(Utils.EVENT_DETAILS_KEY));
                eventTitles.add((String) document.getData().get(Utils.EVENT_TITLE_KEY));
                timestamps.add((Long) document.getData().get(Utils.EVENT_TIMESTAMP_KEY));

            }
            calendarView.setEvents(eventDays);
            String eventDate=bundle.getString(Utils.EVENT_TIMESTAMP_KEY);

            if (eventDate!=null){
                ((AppBarLayout) getActivity().findViewById(R.id.appBarLayout)).setExpanded(false);

                int i=markedDates.indexOf(eventDate);
                int[] date=Utils.StringToDateArray(eventDate);
                Calendar calendar=Calendar.getInstance();
                calendar.set(date[2],date[1]-1,date[0]);

                try {
                    calendarView.setDate(calendar);
                } catch (OutOfDateRangeException e) {
                    e.printStackTrace();
                }

                eventDetailsText.setText(eventsDetails.get(i));
                eventDetailsText.setVisibility(View.VISIBLE);

                eventTitleText.setText(eventTitles.get(i));
                eventTitleText.setVisibility(View.VISIBLE);


                NestedScrollView nestedScrollView = view.findViewById(R.id.eventScroll);

                nestedScrollView.post(() -> nestedScrollView.fullScroll(View.FOCUS_DOWN));
            }



        });



        calendarView.setOnDayClickListener(eventDay -> {
            int day=eventDay.getCalendar().get(Calendar.DAY_OF_MONTH);
            int month=eventDay.getCalendar().get(Calendar.MONTH)+1;
            int year=eventDay.getCalendar().get(Calendar.YEAR);

            eventTitleText.setVisibility(View.GONE);
            eventDetailsText.setVisibility(View.GONE);

            int i = 0;
            for (String markedDate : markedDates) {

                int[] dateOnMark=Utils.StringToDateArray(markedDate);
                Log.v("TAG","dfdfd");
                if (day == dateOnMark[0]&& month==dateOnMark[1]&&year==dateOnMark[2]) {

                    switch (bundle.getString(Utils.ADMIN_ACTION_KEY)) {

                        case Utils.ACTION_EDIT:
                            bundle.putString(Utils.ADMIN_ACTION_KEY, Utils.ACTION_EDIT);
                            bundle.putLong(Utils.EVENT_TIMESTAMP_KEY, timestamps.get(i));
                            bundle.putString(Utils.EVENT_DATE_KEY, markedDate);
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
                            ((AppBarLayout) getActivity().findViewById(R.id.appBarLayout)).setExpanded(false);


                            eventDetailsText.setText(eventsDetails.get(i));
                            eventDetailsText.setVisibility(View.VISIBLE);

                            eventTitleText.setText(eventTitles.get(i));
                            eventTitleText.setVisibility(View.VISIBLE);


                            NestedScrollView nestedScrollView = view.findViewById(R.id.eventScroll);

                            nestedScrollView.post(() -> nestedScrollView.fullScroll(View.FOCUS_DOWN));


                            break;

                    }


                    //TODO :: handle long text in event body to be scrollable
                    //TODO :: handle thumbnail images in mainFragment "change the resolution !
                    //TODO :: make all upload buttons overflow buttons


                }
                {


                }
                i++;
            }


        });


    }
}
