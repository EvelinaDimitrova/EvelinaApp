package com.fmi.evelina.unimobileapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.activity.Calendar.CalendarActivity;
import com.fmi.evelina.unimobileapp.activity.Contacts.ContactsActivity;
import com.fmi.evelina.unimobileapp.activity.ElectionCampaign.ElectionCampaignActivity;
import com.fmi.evelina.unimobileapp.activity.News.NewsActivity;
import com.fmi.evelina.unimobileapp.controller.ApplicationController;

public class DrawerBaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_base);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        enableButtons(navigationView);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent home = new Intent(this, HomeActivity.class);
            startActivity(home);
        } else if (id == R.id.nav_news) {
            Intent news = new Intent(this, NewsActivity.class);
            startActivity(news);
        } else if (id == R.id.nav_login) {
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
        } else if (id == R.id.nav_logout) {
            finish();
            ApplicationController.getInstance().setLoggedUser(null);

            Intent home = new Intent(this, HomeActivity.class);
            //Clear the back stack
            home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(home);
        } else if (id == R.id.nav_calendar) {
            Intent calendar = new Intent(this, CalendarActivity.class);
            startActivity(calendar);
        } else if (id == R.id.nav_student_plan) {
            Intent studentPlan = new Intent(this, StudentPlanActivity.class);
            startActivity(studentPlan);
        } else if (id == R.id.nav_election_campaign) {
            Intent electionCampaign = new Intent(this, ElectionCampaignActivity.class);
            startActivity(electionCampaign);
        } else if (id == R.id.nav_contacts) {
            Intent contacts = new Intent(this, ContactsActivity.class);
            startActivity(contacts);
        }
        else if (id == R.id.nav_settings) {
            Intent contacts = new Intent(this, SettingsActivity.class);
            startActivity(contacts);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setDrawerContentView(int layoutResID) {
        FrameLayout actContent = (FrameLayout) findViewById(R.id.frame_container);
        getLayoutInflater().inflate(layoutResID, actContent, true);
    }

    private void enableButtons(NavigationView navigationView){
        boolean loggedIn = ApplicationController.getInstance().isLoggedIn();
        navigationView.getMenu().findItem(R.id.nav_login).setVisible(!loggedIn);
        navigationView.getMenu().findItem(R.id.nav_logout).setVisible(loggedIn);

        navigationView.getMenu().findItem(R.id.learning_section).setVisible(loggedIn);

        if (loggedIn) {
            switch (ApplicationController.getLoggedUser().Role){
                case STUD: {
                    navigationView.getMenu().findItem(R.id.learning_section).setTitle(R.string.drawer_learning_header);

                    navigationView.getMenu().findItem(R.id.nav_student_plan).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_election_campaign).setVisible(true);
                    break;
                }
                case LECT: {
                    navigationView.getMenu().findItem(R.id.learning_section).setTitle(R.string.drawer_teaching_header);
                    break;
                }
                case ADMN: {
                    navigationView.getMenu().findItem(R.id.learning_section).setTitle(R.string.drawer_administration_header);
                }
            }
        }


    }

    //    public void setContentView(int layoutResID) {
//        DrawerLayout mDrawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base, null);
//        FrameLayout actContent = (FrameLayout) mDrawerLayout.findViewById(R.id.frame_container);
//        // set the drawer layout as main content view of Activity.
//        setContentView(mDrawerLayout);
//        // add layout of BaseActivities inside framelayout.i.e. frame_container
//        getLayoutInflater().inflate(layoutResID, actContent, true);
//    }

}
