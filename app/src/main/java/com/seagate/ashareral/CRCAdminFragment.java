package com.seagate.ashareral;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class CRCAdminFragment extends Fragment implements View.OnClickListener {

    private static final int SPONSOR_CODE = 1;
    private static final int COVER_IMAGE_CODE = 2;
    EditText title, describtion, packages, program, dateEdit, perioud, location;
    EditText[] editTexts;
    List<Uri> sponsorUris;
    Uri coverImageUri;

    List<String> sponsorsDownloadLink;
    String coverImageDownloadLink;

    Button upload, chooseSponser, chooseCoverImage;
    LinearLayout sponserContainer;
    TextView coverImageUriText;

    NavController navController;
    private DatePickerDialog.OnDateSetListener listener;
    private Calendar calendar;

    public CRCAdminFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crc_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViews(view);

    }

    private void setupViews(View view) {
        title = view.findViewById(R.id.title);
        describtion = view.findViewById(R.id.describtion);
        packages = view.findViewById(R.id.packages);
        program = view.findViewById(R.id.program);
        dateEdit = view.findViewById(R.id.date);
        perioud = view.findViewById(R.id.perioud);
        location = view.findViewById(R.id.location);
        upload = view.findViewById(R.id.upload);
        chooseSponser = view.findViewById(R.id.sponserButton);
        sponserContainer = view.findViewById(R.id.imagesUriContainer);
        coverImageUriText = view.findViewById(R.id.coverUri);
        chooseCoverImage = view.findViewById(R.id.chooseCoverimage);

        editTexts = new EditText[]{title, describtion, packages, program, dateEdit, perioud, location};

        chooseSponser.setOnClickListener(this);
        chooseCoverImage.setOnClickListener(this);
        upload.setOnClickListener(this);
        dateEdit.setOnClickListener(this);

        sponsorUris = new ArrayList<>();
        sponsorsDownloadLink=new ArrayList<>();

        navController= Navigation.findNavController(view);

        calendar=Calendar.getInstance();
         listener=
                (view1, year, month, dayOfMonth) -> {
                    calendar.set(year,month,dayOfMonth);
                    dateEdit.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                };

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sponserButton:
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i.createChooser(i, "choose photo"), SPONSOR_CODE);
                break;
            case R.id.chooseCoverimage:
                i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i.createChooser(i, "choose photo"), COVER_IMAGE_CODE);
                break;
            case R.id.upload:
                if (isAllFieldsFilled()) {
                    uploadImages(0);

                }
                break;
            case R.id.date:
                new DatePickerDialog(getContext(),listener,calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }

    private void uploadImages(int index) {

        if (index<sponsorUris.size()) {
            StorageReference reference = FirebaseStorage.getInstance().getReference().child(Utils.CRC_KEY).child(String.valueOf(System.currentTimeMillis()) + index);
            reference.putFile(sponsorUris.get(index)).addOnSuccessListener(taskSnapshot -> {
                reference.getDownloadUrl().addOnSuccessListener(uri -> {
                    sponsorsDownloadLink.add(uri.toString());
                    uploadImages(index + 1);
                });
            });
        }else {
            if (coverImageUri!=null){
                StorageReference reference = FirebaseStorage.getInstance().getReference().child(Utils.CRC_KEY).child(String.valueOf(System.currentTimeMillis()) + index);
                reference.child(Utils.CRC_KEY).child(String.valueOf(System.currentTimeMillis()+index)).putFile(coverImageUri).addOnSuccessListener(taskSnapshot -> {
                    reference.getDownloadUrl().addOnSuccessListener(uri -> {
                        coverImageDownloadLink=uri.toString();
                        uploadContent();
                    });
                });
            }

        }
    }

    private void uploadContent() {
        long timestamp=System.currentTimeMillis();
        String [] download_link=new String[sponsorsDownloadLink.size()];
        CRC crc=new CRC(title.getText().toString(),dateEdit.getText().toString(),
                describtion.getText().toString(),location.getText().toString(),
                packages.getText().toString(),program.getText().toString(),coverImageDownloadLink
                ,timestamp,Integer.parseInt(perioud.getText().toString()),
                sponsorsDownloadLink.toArray(download_link));
        FirebaseFirestore.getInstance().collection(Utils.CRC_KEY).document(String.valueOf(timestamp)).set(crc).addOnSuccessListener(aVoid -> {
            Toast.makeText(getContext(),"CRC Uploaded !",Toast.LENGTH_SHORT).show();
            navController.navigateUp();
        });
    }

    private boolean isAllFieldsFilled() {
        for (EditText editText : editTexts) {
            if (editText.getText().toString().isEmpty()) {
                editText.setError("this field is required !");
                return false;
            }
        }
        if (sponsorUris.isEmpty() || coverImageUri == null) {
            return false;
        } else {
            return true;
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == COVER_IMAGE_CODE && resultCode == Activity.RESULT_OK) {
            coverImageUri = data.getData();

            coverImageUriText.setText(getImageName(coverImageUri));
        } else if (requestCode == SPONSOR_CODE && resultCode == Activity.RESULT_OK) {
            sponsorUris.add(data.getData());
            TextView textView=new TextView(getContext());
            textView.setText(getImageName(data.getData()));
            sponserContainer.addView(textView);
        }
    }

    private String getImageName(Uri uri) {
        Cursor returnCursor = getContext().
                getContentResolver().query(uri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        return returnCursor.getString(nameIndex);
    }
}
