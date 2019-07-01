package com.seagate.ashareral;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ChaptersFragment extends Fragment {

    private ArrayList<Object> objects;
    private ChaptersAdapter adapter;
    private RecyclerView recyclerView;
    private Bundle bundle;
    private NavController navController;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chapters, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppBarLayout) getActivity().findViewById(R.id.appBarLayout)).setExpanded(true);

        progressBar=view.findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);

        bundle = getArguments();


        objects = new ArrayList<>();

        recyclerView = view.findViewById(R.id.progressBar4);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        if (bundle.getString(Utils.ADMIN_ACTION_KEY).equals(Utils.ACTION_VIEW) ) {

            adapter = new ChaptersAdapter(objects, bundle.getString(Utils.RECYCLER_ADAPTER_TYPE));

        } else if (bundle.getString(Utils.ADMIN_ACTION_KEY).equals(Utils.ACTION_EDIT)|| bundle.getString(Utils.ADMIN_ACTION_KEY).equals(Utils.ACTION_DELETE)) {
            adapter = new ChaptersAdapter(objects, bundle.getString(Utils.RECYCLER_ADAPTER_TYPE), v -> {
                ChaptersAdapter.MyViewHolder holder = (ChaptersAdapter.MyViewHolder) v.getTag();
                onRecyclerClick(holder.getAdapterPosition());
            });
        }

        recyclerView.setAdapter(adapter);
        navController= Navigation.findNavController(view);



        // getDataFromJson(bundle.getString(Utils.RECYCLER_ADAPTER_TYPE));
        getData(bundle.getString(Utils.RECYCLER_ADAPTER_TYPE));
        adjustCoverImage();


    }

    private void onRecyclerClick(int position) {

        switch (bundle.getString(Utils.RECYCLER_ADAPTER_TYPE)){
            case Utils.CHAPTER_KEY:
                chapterOnClick(position);
                break;
            case Utils.COMMITTEE_KEY:
                committeeOnClick(position);
                break;
            case Utils.OFFICERS_KEY:
                officerOnClick(position);
                break;
            case Utils.DLS_KEY:
                dlsOnClick(position);
                break;

        }


    }

    private void dlsOnClick(int position) {
        if (bundle.getString(Utils.ADMIN_ACTION_KEY).equals(Utils.ACTION_EDIT)) {
            Bundle bundle = new Bundle();
            bundle.putString(Utils.ADMIN_TYPE,Utils.DLS_KEY);
            bundle.putString(Utils.ADMIN_ACTION_KEY, Utils.ACTION_EDIT);
            bundle.putSerializable(Utils.DLS_KEY, (Dls) objects.get(position));
            navController.navigate(R.id.toChapterAdminFragment, bundle);
        }else if (bundle.getString(Utils.ADMIN_ACTION_KEY).equals(Utils.ACTION_DELETE)){

            Dls dls= (Dls) objects.get(position);
            FirebaseDatabase.getInstance().getReference().child(Utils.DLS_KEY).child(String.valueOf(dls.getTimestamo())).removeValue((databaseError, databaseReference) -> {
                Toast.makeText(getContext(),"Officer removed",Toast.LENGTH_SHORT).show();
                navController.navigateUp();
            });
        }
    }

    private void officerOnClick(int position) {
        if (bundle.getString(Utils.ADMIN_ACTION_KEY).equals(Utils.ACTION_EDIT)) {
            Bundle bundle = new Bundle();
            bundle.putString(Utils.ADMIN_TYPE,Utils.OFFICERS_KEY);
            bundle.putString(Utils.ADMIN_ACTION_KEY, Utils.ACTION_EDIT);
            bundle.putSerializable(Utils.OFFICERS_KEY, (Officer) objects.get(position));
            navController.navigate(R.id.toChapterAdminFragment, bundle);
        }else if (bundle.getString(Utils.ADMIN_ACTION_KEY).equals(Utils.ACTION_DELETE)){
            Officer officer= (Officer) objects.get(position);
            FirebaseDatabase.getInstance().getReference().child(Utils.OFFICERS_KEY).child(String.valueOf(officer.getTimestamp())).removeValue((databaseError, databaseReference) -> {
                Toast.makeText(getContext(),"Officer removed",Toast.LENGTH_SHORT).show();
                navController.navigateUp();
            });
        }
    }

    private void chapterOnClick(int position) {
        if (bundle.getString(Utils.ADMIN_ACTION_KEY).equals(Utils.ACTION_EDIT)) {
            Bundle bundle = new Bundle();
            bundle.putString(Utils.ADMIN_TYPE,Utils.CHAPTER_KEY);
            bundle.putString(Utils.ADMIN_ACTION_KEY, Utils.ACTION_EDIT);
            bundle.putSerializable(Utils.CHAPTER_KEY, (Chapter) objects.get(position));
            navController.navigate(R.id.toChapterAdminFragment, bundle);
        }else if (bundle.getString(Utils.ADMIN_ACTION_KEY).equals(Utils.ACTION_DELETE)){
            Chapter chapter= (Chapter) objects.get(position);
            FirebaseDatabase.getInstance().getReference().child(Utils.CHAPTERS_KEY).child(String.valueOf(chapter.getTimestamp())).removeValue((databaseError, databaseReference) -> {
                Toast.makeText(getContext(),"Chapter removed",Toast.LENGTH_SHORT).show();
                navController.navigateUp();
            });
        }
    }

    private void committeeOnClick(int position) {
        if (bundle.getString(Utils.ADMIN_ACTION_KEY).equals(Utils.ACTION_EDIT)) {
            Bundle bundle = new Bundle();
            bundle.putString(Utils.ADMIN_TYPE,Utils.COMMITTEE_KEY);
            bundle.putString(Utils.ADMIN_ACTION_KEY, Utils.ACTION_EDIT);
            bundle.putSerializable(Utils.COMMITTEE_KEY, (Committee) objects.get(position));
            navController.navigate(R.id.toChapterAdminFragment, bundle);
        }else if (bundle.getString(Utils.ADMIN_ACTION_KEY).equals(Utils.ACTION_DELETE)){
            Committee committee= (Committee) objects.get(position);
            FirebaseDatabase.getInstance().getReference().child(Utils.COMMITTEE_KEY).child(String.valueOf(committee.getTimestamp())).removeValue((databaseError, databaseReference) -> {
                Toast.makeText(getContext(),"Committee removed",Toast.LENGTH_SHORT).show();
                navController.navigateUp();
            });
        }
    }

    private void adjustCoverImage() {
        if (bundle.getString(Utils.RECYCLER_ADAPTER_TYPE).equals(Utils.CHAPTER_KEY)) {
            ((ImageView) getActivity().findViewById(R.id.expandedImage)).setImageResource(R.drawable.chapters);

        } else if (bundle.getString(Utils.RECYCLER_ADAPTER_TYPE).equals(Utils.COMMITTEE_KEY)) {

            ((ImageView) getActivity().findViewById(R.id.expandedImage)).setImageResource(R.drawable.committees);
        } else if (bundle.getString(Utils.RECYCLER_ADAPTER_TYPE).equals(Utils.OFFICERS_KEY)) {

            ((ImageView) getActivity().findViewById(R.id.expandedImage)).setImageResource(R.drawable.officers);
        } else {

            ((ImageView) getActivity().findViewById(R.id.expandedImage)).setImageResource(R.drawable.dls);
        }
    }

    private void getData(String type) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        switch (type) {
            case Utils.CHAPTER_KEY:
                firebaseDatabase.getReference().child(Utils.CHAPTERS_KEY).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Chapter chapter = data.getValue(Chapter.class);
                            objects.add(chapter);
                        }
                        progressBar.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
            case Utils.COMMITTEE_KEY:
                firebaseDatabase.getReference().child(Utils.COMMITTEE_KEY).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Committee committee = data.getValue(Committee.class);
                            objects.add(committee);
                        }
                        progressBar.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
            case Utils.OFFICERS_KEY:
                firebaseDatabase.getReference().child(Utils.OFFICERS_KEY).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Officer officer=data.getValue(Officer.class);

                            objects.add(officer);
                        }
                        progressBar.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
            case Utils.DLS_KEY:
                firebaseDatabase.getReference().child(Utils.DLS_KEY).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Dls dls=data.getValue(Dls.class);

                            objects.add(dls);
                        }
                        progressBar.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
        }


    }

    private void getDataFromJson(String type) {

        try {
            InputStream inputStream = getActivity().getAssets().open(type + ".json");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            String json = new String(buffer, "UTF-8");

            JSONArray jsonArray = new JSONArray(json);

            Person person;
            Chapter chapter;
            Map<String, String> map = new HashMap<>();
            switch (type) {
                case Utils.CHAPTER_KEY:

                    objects.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject obj = jsonArray.getJSONObject(i);
                        chapter = new Chapter(obj.getString(Utils.CHAPTER_COUNTRY),
                                obj.getString(Utils.CHAPTER_LOCATION), obj.getString(Utils.CHAPTER_WEB),
                                obj.getString(Utils.CHAPTER_PERSON), obj.getString(Utils.CHAPTER_EMAIL),
                                obj.getString(Utils.CHAPTER_PHONE), null,
                                obj.getInt(Utils.CHAPTER_NUMBER), System.currentTimeMillis(), null);
                        objects.add(chapter);

                    }
                    break;
                case Utils.COMMITTEE_KEY:

                    objects.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject obj = jsonArray.getJSONObject(i);

                        person = new Person(obj.getString(Utils.PERSON_COMMITTEE),
                                obj.getString(Utils.PERSON_NAME),
                                obj.getString(Utils.PERSON_TITLE),
                                obj.getString(Utils.PERSON_BIO), obj.getString(Utils.PERSON_EMAIL));
                        objects.add(person);

                    }
                    break;
                case Utils.OFFICERS_KEY:
                    objects.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        person = new Person(obj.getString(Utils.PERSON_EMAIL),
                                obj.getString(Utils.PERSON_NAME),
                                obj.getString(Utils.PERSON_TITLE),
                                obj.getString(Utils.PERSON_BIO));
                        objects.add(person);

                    }
                    break;

                case Utils.DLS_KEY:
                    objects.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        person = new Person(obj.getString(Utils.PERSON_COURSE),
                                obj.getString(Utils.PERSON_NAME),
                                obj.getString(Utils.PERSON_TITLE), obj.getString(Utils.PERSON_BIO));
                        objects.add(person);
                    }
                    break;
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {

            adapter = new ChaptersAdapter(objects, type);
            recyclerView.setAdapter(adapter);
        }
    }
}
