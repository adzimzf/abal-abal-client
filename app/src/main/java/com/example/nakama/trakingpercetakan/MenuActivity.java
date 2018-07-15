package com.example.nakama.trakingpercetakan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button logout = (Button) findViewById(R.id.logoutbtn);
        logout.setOnClickListener(logoutClick);

        Button tracking  = (Button) findViewById(R.id.tracking);
        tracking.setOnClickListener(toTracking);


    }

    private View.OnClickListener logoutClick = new View.OnClickListener() {
        public void onClick(View v) {

            // remove is login
            // go to login
            SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            finish();
            Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener toTracking = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(MenuActivity.this, MainActivity.class);
            startActivity(intent);
        }
    };

}
