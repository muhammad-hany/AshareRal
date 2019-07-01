package com.seagate.ashareral;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class ChapterAdminFragment extends Fragment implements View.OnClickListener {

    EditText number, location, web, president, email, phone, country, subRegion;
    EditText committeeName, name, position, bio,courseTought;



    Button chooseImage, upload;
    Bundle bundle;
    Uri imageUri;
    ImageView imageView;
    NavController navController;
    long timestamp;

    public ChapterAdminFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bundle = getArguments();
        int layout=-1;
        switch (bundle.getString(Utils.ADMIN_TYPE)){
            case Utils.COMMITTEE_KEY:
                layout=R.layout.fragment_committees_admin;
                break;
            case Utils.CHAPTER_KEY:
                layout=R.layout.fragment_chapter_admin;
                break;
            case Utils.OFFICERS_KEY:
                layout=R.layout.fragment_officers_admin;
                break;
            case Utils.DLS_KEY:
                layout=R.layout.fragment_dls_admin;
                break;

        }
        return inflater.inflate(layout, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        defineViews(view);


        navController = Navigation.findNavController(view);


    }

    private void defineViews(View view) {


        switch (bundle.getString(Utils.ADMIN_TYPE)) {
            case Utils.CHAPTER_KEY:
                setupChaptersView(view);
                break;
            case Utils.COMMITTEE_KEY:
                setupCommitteesView(view);
                break;
            case Utils.OFFICERS_KEY:
                setupOfficersView(view);
                break;
            case Utils.DLS_KEY:
                setupDLsView(view);
                break;

        }


    }

    private void setupDLsView(View view) {
        name = view.findViewById(R.id.name);
        position = view.findViewById(R.id.position);
        bio = view.findViewById(R.id.bio);
        chooseImage = view.findViewById(R.id.chooseImage2);
        upload = view.findViewById(R.id.upload2);
        chooseImage.setOnClickListener(this);
        upload.setOnClickListener(this);
        imageView = view.findViewById(R.id.chapterCover);
        courseTought=view.findViewById(R.id.courseTought);

        if (bundle.getString(Utils.ADMIN_ACTION_KEY).equals(Utils.ACTION_EDIT)) {

            Dls dls= (Dls) bundle.getSerializable(Utils.DLS_KEY);
            courseTought.setText(dls.getCouseTought());
            name.setText(dls.getName());
            position.setText(dls.getPosition());
            bio.setText(dls.getBio());
            timestamp = dls.getTimestamo();
            Picasso.get().load(dls.getDownload_link()).placeholder(R.drawable.placeholder).into(imageView);
        } else {
            timestamp = System.currentTimeMillis();
        }
    }

    private void setupOfficersView(View view) {

        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        position = view.findViewById(R.id.position);
        bio = view.findViewById(R.id.bio);
        chooseImage = view.findViewById(R.id.chooseImage2);
        upload = view.findViewById(R.id.upload2);
        chooseImage.setOnClickListener(this);
        upload.setOnClickListener(this);
        imageView = view.findViewById(R.id.chapterCover);

        if (bundle.getString(Utils.ADMIN_ACTION_KEY).equals(Utils.ACTION_EDIT)) {

            Officer officer= (Officer) bundle.getSerializable(Utils.OFFICERS_KEY);

            name.setText(officer.getName());
            email.setText(officer.getEmail());
            position.setText(officer.getPosition());
            bio.setText(officer.getBio());
            timestamp = officer.getTimestamp();
            Picasso.get().load(officer.getDownload_link()).placeholder(R.drawable.placeholder).into(imageView);
        } else {
            timestamp = System.currentTimeMillis();
        }
    }

    private void setupCommitteesView(View view) {
        committeeName = view.findViewById(R.id.committee);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        position = view.findViewById(R.id.position);
        bio = view.findViewById(R.id.bio);
        chooseImage = view.findViewById(R.id.chooseImage2);
        upload = view.findViewById(R.id.upload2);
        chooseImage.setOnClickListener(this);
        upload.setOnClickListener(this);
        imageView = view.findViewById(R.id.chapterCover);

        if (bundle.getString(Utils.ADMIN_ACTION_KEY).equals(Utils.ACTION_EDIT)) {
            Committee committee = (Committee) bundle.getSerializable(Utils.COMMITTEE_KEY);
            committeeName.setText(committee.getCommittee());
            name.setText(committee.getName());
            email.setText(committee.getEmail());
            position.setText(committee.getPosition());
            bio.setText(committee.getBio());
            timestamp = committee.getTimestamp();
            Picasso.get().load(committee.getDownload_link()).placeholder(R.drawable.placeholder).into(imageView);
        } else {
            timestamp = System.currentTimeMillis();
        }
    }

    private void setupChaptersView(View view) {
        number = view.findViewById(R.id.name);
        location = view.findViewById(R.id.position);
        web = view.findViewById(R.id.bio);
        president = view.findViewById(R.id.president);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);
        chooseImage = view.findViewById(R.id.chooseImage2);
        upload = view.findViewById(R.id.upload2);
        imageView = view.findViewById(R.id.chapterCover);
        country = view.findViewById(R.id.committee);
        subRegion = view.findViewById(R.id.subRegion);
        chooseImage.setOnClickListener(this);
        upload.setOnClickListener(this);


        if (bundle.getString(Utils.ADMIN_ACTION_KEY).equals(Utils.ACTION_EDIT)) {

            Chapter chapter = (Chapter) bundle.getSerializable(Utils.CHAPTER_KEY);


            number.setText(String.valueOf(chapter.getChapterNumber()));
            location.setText(chapter.getLocation());
            country.setText(chapter.getCountry());
            subRegion.setText(chapter.getSubRegion());
            phone.setText(chapter.getPhone());
            email.setText(chapter.getEmail());
            web.setText(chapter.getWeb());
            president.setText(chapter.getPerson());
            Picasso.get().load(chapter.getDownload_link()).placeholder(R.drawable.placeholder).into(imageView);
            timestamp = chapter.getTimestamp();

        } else {
            timestamp = System.currentTimeMillis();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chooseImage2:
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i.createChooser(i, "choose photo"), 500);
                break;
            case R.id.upload2:
                boolean allFieldsFilled;
                switch (bundle.getString(Utils.ADMIN_TYPE)) {
                    case Utils.CHAPTER_KEY:
                        allFieldsFilled = !(number.getText().toString().isEmpty() &&
                                location.getText().toString().isEmpty() &&
                                president.getText().toString().isEmpty() &&
                                email.getText().toString().isEmpty() &&
                                country.getText().toString().isEmpty() &&
                                phone.getText().toString().isEmpty() && imageUri == null);
                        if (allFieldsFilled) {
                            uploadChapterContent();
                        }else {
                            Toast.makeText(getContext(),"you must fill all fields",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case Utils.COMMITTEE_KEY:
                        allFieldsFilled = !(committeeName.getText().toString().isEmpty() &&
                                name.getText().toString().isEmpty() &&
                                position.getText().toString().isEmpty() &&
                                email.getText().toString().isEmpty() &&
                                bio.getText().toString().isEmpty() && imageUri == null);
                        if (allFieldsFilled) {
                            uploadCommitteeContent();
                        }else {
                            Toast.makeText(getContext(),"you must fill all fields",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case Utils.OFFICERS_KEY:
                        allFieldsFilled = !(name.getText().toString().isEmpty() &&
                                position.getText().toString().isEmpty() &&
                                email.getText().toString().isEmpty() &&
                                bio.getText().toString().isEmpty() && imageUri == null);
                        if (allFieldsFilled) {
                            uploadOfficerContent();
                        }else {
                            Toast.makeText(getContext(),"you must fill all fields",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case Utils.DLS_KEY:
                        allFieldsFilled = !(name.getText().toString().isEmpty() &&
                                position.getText().toString().isEmpty()&&
                                bio.getText().toString().isEmpty() && imageUri == null);
                        if (allFieldsFilled) {
                            uploadDlsContent();
                        }else {
                            Toast.makeText(getContext(),"you must fill all fields",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                break;

        }
    }

    private void uploadDlsContent() {
        if (imageUri == null && bundle.get(Utils.ADMIN_ACTION_KEY).equals(Utils.ACTION_EDIT)) {
            Dls dls = (Dls) bundle.getSerializable(Utils.DLS_KEY);
            uploadObject(dls.getDownload_link());
        } else {
            if (imageUri!=null) {
                StorageReference officerReference =
                        FirebaseStorage.getInstance().getReference().child(Utils.DLS_KEY).child(String.valueOf(timestamp));

                officerReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                    officerReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        uploadObject(uri.toString());
                    });
                });
            }else {
                Toast.makeText(getContext(),"you must choose image",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadOfficerContent() {
        if (imageUri == null && bundle.get(Utils.ADMIN_ACTION_KEY).equals(Utils.ACTION_EDIT)) {
            Officer officer = (Officer) bundle.getSerializable(Utils.OFFICERS_KEY);
            uploadObject(officer.getDownload_link());
        } else {
            if (imageUri!=null) {
                StorageReference officerReference =
                        FirebaseStorage.getInstance().getReference().child(Utils.OFFICERS_KEY).child(String.valueOf(timestamp));

                officerReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                    officerReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        uploadObject(uri.toString());
                    });
                });
            }else {
                Toast.makeText(getContext(),"you must choose image",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadCommitteeContent() {
        if (imageUri == null && bundle.get(Utils.ADMIN_ACTION_KEY).equals(Utils.ACTION_EDIT)) {
            Committee committee = (Committee) bundle.getSerializable(Utils.COMMITTEE_KEY);
            uploadObject(committee.getDownload_link());
        } else {
            if (imageUri!=null) {
                StorageReference committeeReference =
                        FirebaseStorage.getInstance().getReference().child(Utils.COMMITTEE_KEY).child(String.valueOf(timestamp));

                committeeReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                    committeeReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        uploadObject(uri.toString());
                    });
                });
            }else {
                Toast.makeText(getContext(),"you must choose image",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadChapterContent() {

        if (imageUri == null && bundle.get(Utils.ADMIN_ACTION_KEY).equals(Utils.ACTION_EDIT)) {
            Chapter chapter = (Chapter) bundle.getSerializable(Utils.CHAPTER_KEY);
            uploadObject(chapter.getDownload_link());
        } else {
            if (imageUri!=null) {
                StorageReference chapterReference = FirebaseStorage.getInstance().getReference().child("chapters").child(String.valueOf(timestamp));

                chapterReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                    chapterReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        uploadObject(uri.toString());
                    });
                });
            }else {
                Toast.makeText(getContext(),"you must choose image",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void uploadObject(String uri) {
        Object object = null;
        String type = null;

        if (bundle.getString(Utils.ADMIN_TYPE).equals(Utils.CHAPTER_KEY)) {
            type = Utils.CHAPTERS_KEY;
            object = new Chapter(country.getText().toString(), location.getText().toString(),
                    web.getText().toString(), president.getText().toString(),
                    email.getText().toString(), phone.getText().toString(), uri,
                    Integer.parseInt(number.getText().toString()), timestamp, subRegion.getText().toString());

        } else if (bundle.getString(Utils.ADMIN_TYPE).equals(Utils.COMMITTEE_KEY)) {
            type = Utils.COMMITTEE_KEY;
            object = new Committee(committeeName.getText().toString(), name.getText().toString(),
                    position.getText().toString(), email.getText().toString(),
                    bio.getText().toString(), uri, timestamp);

        }else if (bundle.getString(Utils.ADMIN_TYPE).equals(Utils.OFFICERS_KEY)){
            type=Utils.OFFICERS_KEY;
            object=new Officer(name.getText().toString(),position.getText().toString(),
                    email.getText().toString(),bio.getText().toString(),uri,timestamp);

        }else if (bundle.getString(Utils.ADMIN_TYPE).equals(Utils.DLS_KEY)){
            type=Utils.DLS_KEY;
            object=new Dls(name.getText().toString(),position.getText().toString(),
                    bio.getText().toString(),uri,courseTought.getText().toString(),timestamp);
        }


        FirebaseDatabase.getInstance().getReference().child(type).child(String.valueOf(timestamp)).setValue(object).addOnSuccessListener(aVoid -> {
            Toast.makeText(getContext(), bundle.getString(Utils.ADMIN_TYPE) + " Uploaded" +
                    " ", Toast.LENGTH_SHORT);
            navController.navigateUp();

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 500 && resultCode == Activity.RESULT_OK) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}
