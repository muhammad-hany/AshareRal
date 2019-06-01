package com.seagate.ashareral;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ChaptersFragment extends Fragment {

    String[] urls = new String[25];
    private ArrayList<Object> objects;
    private ChaptersAdapter adapter;
    private RecyclerView recyclerView;
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chapters, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppBarLayout layout=getActivity().findViewById(R.id.appBarLayout);
        layout.setExpanded(false);


        bundle = getArguments();




        objects = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        AtomicInteger j = new AtomicInteger(1);

        if (bundle.getString(Utils.RECYCLER_ADAPTER_TYPE).equals(Utils.CHAPTER_KEY)){
            for (int i = 0; i < 25; i++) {
                FirebaseStorage.getInstance().getReference().child("chapters/" + (i + 1) + ".jpg").getDownloadUrl().addOnSuccessListener(uri -> {
                    int position = Integer.valueOf(uri.toString().subSequence(uri.toString().indexOf("F") + 1, uri.toString().indexOf("jpg") - 1).toString());
                    urls[position - 1] = uri.toString();
                    if (j.get() == 25) {
                        getDataFromJson(Utils.CHAPTER_KEY);
                    }
                    j.getAndIncrement();
                });
            }
        }else {
            getDataFromJson(bundle.getString(Utils.RECYCLER_ADAPTER_TYPE));
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
            switch (type) {
                case Utils.CHAPTER_KEY:

                    objects.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        chapter = new Chapter(obj.getString(Utils.CHAPTER_COUNTRY),
                                obj.getString(Utils.CHAPTER_LOCATION), obj.getString(Utils.CHAPTER_WEB),
                                obj.getString(Utils.CHAPTER_PERSON), obj.getString(Utils.CHAPTER_EMAIL),
                                obj.getString(Utils.CHAPTER_PHONE), obj.getInt(Utils.CHAPTER_NUMBER),
                                urls[i]);
                        objects.add(chapter);

                    }
                    break;
                case Utils.COMMITTEE_KEY:

                    objects.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        person = new Person(obj.getString(Utils.PERSON_COMMITTEE),
                                obj.getString(Utils.PERSON_NAME),
                                obj.getString(Utils.PERSON_TITLE), obj.getString(Utils.PERSON_BIO));
                        objects.add(person);

                    }
                    break;
                case Utils.OFFICERS_KEY:
                    objects.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        person = new Person(obj.getString(Utils.PERSON_NAME),
                                obj.getString(Utils.PERSON_TITLE), obj.getString(Utils.PERSON_BIO));
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

            adapter = new ChaptersAdapter(objects,type);
            recyclerView.setAdapter(adapter);
        }
    }
}
