package com.seagate.ashareral;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavController navController;
    private DrawerLayout drawer;
    private int chapterCounter;
    private int officersCounter = 1;
    private int dlsCounter = 1, committeeCounter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUi();
        setNotificationService();

      //  organizeDatabase();


    }

    private void organizeDatabase() {

        ArrayList<Object> chapters = getDataFromJson(Utils.DLS_KEY);
        uploadChaptersMedia(0, chapters);

    }

    private void uploadChaptersMedia(int index, ArrayList<Object> chapters) {
        if (index < chapters.size()) {
            /*Uri uri =
                    Uri.parse("android.resource://com.seagate.ashareral/drawable/" + Utils.dlsRes[index]);*/
            Uri uri = null;

            StorageReference reference =
                    FirebaseStorage.getInstance().getReference(Utils.DLS_KEY).child(String.valueOf(System.currentTimeMillis()));

            reference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                reference.getDownloadUrl().addOnSuccessListener(uri1 -> {
                    Dls chapter = (Dls) chapters.get(index);
                    chapter.setDownload_link(uri1.toString());
                    uploadChaptersMedia(index + 1, chapters);
                });


            });
        } else {
            uploadChapterObject(0,chapters);
        }
    }

    private void uploadChapterObject(int index, ArrayList<Object> chapters) {
        if (index<chapters.size()) {
            Dls chapter = (Dls) chapters.get(index);

            FirebaseDatabase.getInstance().getReference().child(Utils.DLS_KEY).child(String.valueOf(chapter.getTimestamo())).setValue(chapter).addOnSuccessListener(aVoid -> {
                uploadChapterObject(index + 1, chapters);
            });
        }
    }

    private void organizeCommittee() {
        FirebaseStorage.getInstance().getReference().child("committees").child(committeeCounter +
                ".png").getDownloadUrl().addOnSuccessListener(uri -> {

            FirebaseDatabase.getInstance().getReference().child("committees").child(String.valueOf(committeeCounter - 1)).child("download_link").setValue(uri.toString()).addOnSuccessListener(aVoid -> {
                committeeCounter++;
                if (committeeCounter <= 6) organizeCommittee();
            }).addOnFailureListener(e -> {
                Log.v("TAG", e.getMessage());
            });
        }).addOnFailureListener(e -> {
            Log.v("TAG", e.getMessage());
        });
    }

    private void organizeDls() {
        FirebaseStorage.getInstance().getReference().child("dls").child(dlsCounter +
                ".png").getDownloadUrl().addOnSuccessListener(uri -> {

            FirebaseDatabase.getInstance().getReference().child("dls").child(String.valueOf(dlsCounter - 1)).child("download_link").setValue(uri.toString()).addOnSuccessListener(aVoid -> {
                dlsCounter++;
                if (dlsCounter <= 6) organizeDls();
            }).addOnFailureListener(e -> {
                Log.v("TAG", e.getMessage());
            });
        }).addOnFailureListener(e -> {
            Log.v("TAG", e.getMessage());
        });
    }

    private void organizeOfficers() {
        FirebaseStorage.getInstance().getReference().child("officers").child(officersCounter +
                ".png").getDownloadUrl().addOnSuccessListener(uri -> {

            FirebaseDatabase.getInstance().getReference().child("officers").child(String.valueOf(officersCounter - 1)).child("download_link").setValue(uri.toString()).addOnSuccessListener(aVoid -> {
                officersCounter++;
                if (officersCounter <= 16) organizeOfficers();
            }).addOnFailureListener(e -> {
                Log.v("TAG", e.getMessage());
            });
        }).addOnFailureListener(e -> {
            Log.v("TAG", e.getMessage());
        });
    }

    private void organizeChapters() {
        FirebaseStorage.getInstance().getReference().child("chapters").child(chapterCounter + ".jpg").getDownloadUrl().addOnSuccessListener(uri -> {

            FirebaseDatabase.getInstance().getReference().child("chapters").child(String.valueOf(chapterCounter - 1)).child("download_link").setValue(uri.toString()).addOnSuccessListener(aVoid -> {
                FirebaseDatabase.getInstance().getReference().child(Utils.CHAPTERS_KEY).child(String.valueOf(chapterCounter - 1)).child("timestamp").setValue(System.currentTimeMillis()).addOnSuccessListener(aVoid1 -> {
                    chapterCounter++;
                    if (chapterCounter <= 25) organizeChapters();
                });

            }).addOnFailureListener(e -> {
                Log.v("TAG", e.getMessage());
            });
        }).addOnFailureListener(e -> {
            Log.v("TAG", e.getMessage());
        });
    }

    private void handleNotificationOpen() {
        Intent intent = getIntent();
        if (intent != null) {

            if (intent.getStringExtra(Utils.NOTIFICATION_KEY) != null) {
                switch (intent.getStringExtra(Utils.NOTIFICATION_KEY)) {
                    case Utils.NEWS_KEY:
                        News news = (News) intent.getSerializableExtra(Utils.NEWS_KEY);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Utils.NEWS_KEY, news);
                        navController.navigate(R.id.action_mainFragment_to_newsDetails2, bundle);
                        break;

                }
            }
        }
    }

    private void setNotificationService() {
        Intent intent = new Intent(getApplicationContext(), NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 12345, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        /*alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+5000,pendingIntent);*/

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5000, pendingIntent);


    }

    private void setUi() {
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.open_navigation_drawer, R.string.close_navigation_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navController = Navigation.findNavController(this, R.id.host_fragment);
        NavGraph navGraph = navController.getGraph();

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        NavigationUI.setupWithNavController(collapsingToolbarLayout, toolbar, navController, drawer);


        collapsingToolbarLayout.setVisibility(View.GONE);

        navController.navigate(R.id.toSplashFragment);


        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setCheckable(false);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        navigationHelper(menuItem.getItemId());


        drawer.closeDrawer(Gravity.LEFT);

        return true;
    }


    protected void navigationHelper(int id) {
        Bundle bundle = new Bundle();
        switch (id) {
            case R.id.nav_news:

                navController.navigate(R.id.toNewsFragment);
                break;
            case R.id.nav_Events:
                bundle.putString(Utils.CALENDAR_KEY, Utils.EVENT_KEY);
                bundle.putString(Utils.ADMIN_ACTION_KEY, Utils.ACTION_VIEW);
                navController.navigate(R.id.toEventsFragment, bundle);
                break;
            case R.id.nav_poll:
                bundle.putString(Utils.POLL_ACTION, Utils.POLL_OPEN);
                navController.navigate(R.id.toPollListFragment, bundle);
                break;
            case R.id.nav_admin:
                navController.navigate(R.id.toAdminMainFragment);
                break;
            case R.id.nav_crc:
                bundle.putString(Utils.ADMIN_ACTION_KEY, Utils.ACTION_VIEW);
                bundle.putString(Utils.CALENDAR_KEY, Utils.GTC_KEY);
                navController.navigate(R.id.toCRCListFragment, bundle);
                break;
            case R.id.nav_chapter:
                bundle.putString(Utils.RECYCLER_ADAPTER_TYPE, Utils.CHAPTER_KEY);
                navController.navigate(R.id.toChaptersFragment, bundle);
                break;
            case R.id.nav_committees:
                bundle.putString(Utils.RECYCLER_ADAPTER_TYPE, Utils.COMMITTEE_KEY);
                navController.navigate(R.id.toCommitteeFragment, bundle);
                break;
            case R.id.nav_dls:
                bundle.putString(Utils.RECYCLER_ADAPTER_TYPE, Utils.DLS_KEY);
                navController.navigate(R.id.toDlsFragment, bundle);
                break;
            case R.id.nav_officers:
                bundle.putString(Utils.RECYCLER_ADAPTER_TYPE, Utils.OFFICERS_KEY);
                navController.navigate(R.id.toOfficerFragment, bundle);
                break;

        }
    }


    private ArrayList<Object> getDataFromJson(String type) {

        ArrayList<Object> objects = new ArrayList<>();

        try {
            InputStream inputStream = getAssets().open(type + ".json");
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
                                obj.getInt(Utils.CHAPTER_NUMBER), System.currentTimeMillis()+i,
                                obj.getString(Utils.CHAPTER_SUBREGION));

                        objects.add(chapter);

                    }


                    break;
                case Utils.COMMITTEE_KEY:

                    objects.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject obj = jsonArray.getJSONObject(i);
                        Committee committee=new Committee(obj.getString(Utils.PERSON_COMMITTEE),
                                obj.getString(Utils.PERSON_NAME),
                                obj.getString(Utils.COMMITTEE_TITLE),
                                obj.getString(Utils.PERSON_EMAIL),obj.getString(Utils.PERSON_BIO)
                                ,null,System.currentTimeMillis()+i);
                        objects.add(committee);

                    }
                    break;
                case Utils.OFFICERS_KEY:
                    objects.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        Officer officer=new Officer(obj.getString(Utils.PERSON_NAME),
                                obj.getString(Utils.COMMITTEE_TITLE),
                                obj.getString(Utils.PERSON_EMAIL),obj.getString(Utils.PERSON_BIO)
                                ,null,System.currentTimeMillis()+i);

                        objects.add(officer);

                    }
                    break;

                case Utils.DLS_KEY:
                    objects.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        Dls dls=new Dls(obj.getString(Utils.PERSON_NAME),
                                obj.getString(Utils.COMMITTEE_TITLE),
                                obj.getString(Utils.PERSON_BIO),null,
                                obj.getString(Utils.PERSON_COURSE),System.currentTimeMillis()+i);

                        objects.add(dls);
                    }
                    break;
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {

            return objects;
        }
    }
}
