package com.seagate.ashareral;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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


public class ChaptersFragment extends Fragment {

    String [] urls=new String[25];
    private ArrayList<Chapter> chapters;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chapters, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AtomicInteger j= new AtomicInteger(1);
        for (int i=0;i<25;i++){
            FirebaseStorage.getInstance().getReference().child("chapters/"+(i+1)+".jpg").getDownloadUrl().addOnSuccessListener(uri -> {
                int position=Integer.valueOf(uri.toString().subSequence(uri.toString().indexOf("F")+1,uri.toString().indexOf("jpg")-1).toString());
                urls[position-1]=uri.toString();
                if (j.get() ==25){
                    getDataFromJson(view);
                }
                j.getAndIncrement();
            });
        }







    }

    private void getDataFromJson(View view) {
        chapters=new ArrayList<>();
        try {
            InputStream inputStream=getActivity().getAssets().open("data.json");
            byte [] buffer=new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            String json=new String(buffer, "UTF-8");

            JSONArray jsonArray=new JSONArray(json);
            Chapter chapter;
            for (int i=0;i<jsonArray.length();i++){
                JSONObject obj=jsonArray.getJSONObject(i);

                chapter=new Chapter(obj.getString(Utils.CHAPTER_COUNTRY),
                        obj.getString(Utils.CHAPTER_LOCATION),obj.getString(Utils.CHAPTER_WEB),
                        obj.getString(Utils.CHAPTER_PERSON),obj.getString(Utils.CHAPTER_EMAIL),
                        obj.getString(Utils.CHAPTER_PHONE),obj.getInt(Utils.CHAPTER_NUMBER),
                        urls[i]);
                chapters.add(chapter);

            }

            ListView listView=view.findViewById(R.id.listView);
            ChaptersAdapter adapter=new ChaptersAdapter(chapters,getActivity());
            listView.setAdapter(adapter);





        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {

        }
    }
}
