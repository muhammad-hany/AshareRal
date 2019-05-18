package com.seagate.ashareral;


import android.app.Activity;
import android.app.AlertDialog;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class AdminNewsFragment extends Fragment implements View.OnClickListener {


    private EditText title;
    private EditText body;
    private Uri imageUri;
    private boolean isImageTaskFinsihed = false;
    private boolean isNewsTaskFinsihed = false;
    private ImageView imageView;
    private NavController navController;
    private AlertDialog dialog;
    private boolean isItNewNews = true;
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.editTextTitle);
        body = view.findViewById(R.id.editTexctBody);
        view.findViewById(R.id.chooseImage).setOnClickListener(this);
        Button uploadBtn = view.findViewById(R.id.upload);
        uploadBtn.setOnClickListener(this);
        imageView = getView().findViewById(R.id.uploadImage);
        navController = Navigation.findNavController(view);
        dialog = new AlertDialog.Builder(getContext()).setMessage("Loading ..").create();

        bundle = getArguments();
        if (bundle != null && bundle.getString(Utils.ADMIN_ACTION_KEY).equals(Utils.ACTION_EDIT)) {
            isItNewNews = false;
            title.setText(bundle.getString(Utils.NEWS_TITLE_KEY));
            body.setText(bundle.getString(Utils.NEWS_BODY_KEY));
            Picasso.get().load(bundle.getString(Utils.NEWS_IMAGE_URL)).into(imageView);
            uploadBtn.setText("Update News");
        } else {
            isItNewNews = true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chooseImage:
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i.createChooser(i, "choose photo"), 1);
                break;
            case R.id.upload:

                checkAllFields();


                dialog.show();
        }
    }


    private void checkAllFields() {
        if (title.getText().toString().isEmpty()) title.setError("this field is required !");
        if (body.getText().toString().isEmpty()) body.setError("this field is required !");
        if (imageUri == null) {
            if (isItNewNews) {
                Toast.makeText(getContext(), "you must choose cover photo !",
                        Toast.LENGTH_LONG).show();
            }

        }

        if (isItNewNews) {
            if (!title.getText().toString().isEmpty() && !body.getText().toString().isEmpty() && imageUri != null) {
                uploadContent();
            }
        } else {
            if (!title.getText().toString().isEmpty() && !body.getText().toString().isEmpty()) {
                if (imageUri != null) {
                    uploadContent();
                } else {
                    Uri uri = Uri.parse(bundle.getString(Utils.NEWS_IMAGE_URL));
                    long timestamp = bundle.getLong(Utils.NEWS_IMAGE_TIME_KEY);
                    uploadObject(uri, timestamp);
                }


            }
        }

    }

    private void uploadContent() {
        long timestamp = 0;
        if (isItNewNews) {
            timestamp = System.currentTimeMillis();
        } else {
            timestamp = bundle.getLong(Utils.NEWS_IMAGE_TIME_KEY);
        }

        StorageReference reference = FirebaseStorage.getInstance().getReference();
        final StorageReference newsReference = reference.child("news").child(String.valueOf(timestamp));

        final UploadTask uploadTask = newsReference.putFile(imageUri);
        final long finalTimestamp = timestamp;
        uploadTask
                .addOnFailureListener(e -> Toast.makeText(getContext(), "upload failed", Toast.LENGTH_LONG).show())
                .addOnSuccessListener(taskSnapshot -> {
                    dialog.hide();
                    Toast.makeText(getContext(), "upload success", Toast.LENGTH_LONG).show();
        newsReference.getDownloadUrl().addOnSuccessListener(uri -> uploadObject(uri, finalTimestamp));


                });


    }

    private void uploadObject(Uri uri, long timestamp) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        News news = new News(body.getText().toString(), title.getText().toString(), timestamp, uri.toString());

        db.collection("news").document(String.valueOf(timestamp)).set(news).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialog.hide();
                navController.navigateUp();


            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            imageUri = data.getData();

            imageView.setImageURI(imageUri);
        }
    }

    //TODO  ;; handle images dimensions in all fragments
    //TODO ;; put placeholder image or loading image
    //TODO :: handel views after uploading content
    //TODO :: show loading window
}
