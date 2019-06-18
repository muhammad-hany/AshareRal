package com.seagate.ashareral;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUi();
        setNotificationService();



    }

    private void handleNotificationOpen() {
        Intent intent=getIntent();
        if (intent!=null) {

            if (intent.getStringExtra(Utils.NOTIFICATION_KEY)!=null) {
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
        Intent intent=new Intent(getApplicationContext(),NotificationService.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,12345,intent,0);
        AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
        /*alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+5000,pendingIntent);*/

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),5000,pendingIntent);


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
        NavGraph navGraph=navController.getGraph();

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
                bundle.putString(Utils.ADMIN_ACTION_KEY,Utils.ACTION_VIEW);
                bundle.putString(Utils.CALENDAR_KEY,Utils.GTC_KEY);
                navController.navigate(R.id.toGTCFragment,bundle);
                break;
            case R.id.nav_chapter:
                bundle.putString(Utils.RECYCLER_ADAPTER_TYPE,Utils.CHAPTER_KEY);
                navController.navigate(R.id.toChaptersFragment,bundle);
                break;
            case R.id.nav_committees:
                bundle.putString(Utils.RECYCLER_ADAPTER_TYPE,Utils.COMMITTEE_KEY);
                navController.navigate(R.id.toCommitteeFragment,bundle);
                break;
            case R.id.nav_dls:
                bundle.putString(Utils.RECYCLER_ADAPTER_TYPE,Utils.DLS_KEY);
                navController.navigate(R.id.toDlsFragment,bundle);
                break;
            case R.id.nav_officers:
                bundle.putString(Utils.RECYCLER_ADAPTER_TYPE,Utils.OFFICERS_KEY);
                navController.navigate(R.id.toOfficerFragment,bundle);
                break;

        }
    }
}
