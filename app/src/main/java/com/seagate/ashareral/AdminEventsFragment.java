package com.seagate.ashareral;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import static android.widget.Toast.LENGTH_LONG;

public class AdminEventsFragment extends Fragment implements View.OnClickListener {


    private DatePicker datePicker;
    private EditText eventDetailsEditText, eventTitleEditText;
    private AlertDialog dialog;
    private NavController navController;
    private boolean isItNewEvent=true;
    private Bundle bundle;

    public AdminEventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_events, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.eventUpload).setOnClickListener(this);
        datePicker = view.findViewById(R.id.datePicker);
        eventDetailsEditText = view.findViewById(R.id.eventTitleText);
        eventTitleEditText = view.findViewById(R.id.eventTitle);

        bundle = getArguments();
        if (bundle != null && bundle.getString(Utils.ADMIN_ACTION_KEY).equals(Utils.ACTION_EDIT)) {
            isItNewEvent=false;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(bundle.getLong(Utils.EVENT_DATE_KEY)));
            datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);

            eventDetailsEditText.setText(bundle.getString(Utils.EVENT_DETAILS_KEY));
            eventTitleEditText.setText(bundle.getString(Utils.EVENT_TITLE_KEY));

        }


        dialog = new AlertDialog.Builder(getContext()).setMessage("Loading ..").create();
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.eventUpload:
                dialog.show();

                String date =
                        datePicker.getDayOfMonth() + "/" + (datePicker.getMonth() + 1) + "/" + datePicker.getYear();

                final String eventDetails = this.eventDetailsEditText.getText().toString();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                long timestamp = 0;
                if (isItNewEvent) {
                     timestamp = System.currentTimeMillis();
                }else {
                    timestamp=bundle.getLong(Utils.EVENT_TIMESTAMP_KEY);
                }
                Map<String, Object> eventData = new HashMap<>();
                eventData.put(Utils.EVENT_TIMESTAMP_KEY,timestamp);
                eventData.put(Utils.EVENT_DATE_KEY, Utils.getDate(date));
                eventData.put(Utils.EVENT_DETAILS_KEY, eventDetails);
                eventData.put(Utils.EVENT_TITLE_KEY, eventTitleEditText.getText().toString());

                db.collection(bundle.getString(Utils.CALENDAR_KEY)).document(String.valueOf(timestamp)).set(eventData).addOnSuccessListener(aVoid -> {
                    dialog.hide();
                    Toast.makeText(getContext(), "event uploaded !", LENGTH_LONG).show();
                    /*Calendar calendar=Calendar.getInstance();
                    datePicker.updateDate(calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                    eventDetailsEditText.setText("");
                    eventTitleEditText.setText("");*/
                    navController.navigateUp();

                });
                break;

        }
    }
}
