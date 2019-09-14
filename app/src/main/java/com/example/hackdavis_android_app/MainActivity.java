package com.example.hackdavis_android_app;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    final Fragment scheduleFragment = new ScheduleFragment();
    final Fragment mapFragment = new MapFragment();
    final Fragment mentorsFragment = new MentorsFragment();
    final Fragment qrFragment = new QRFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = scheduleFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_schedule:
                    fm.beginTransaction().hide(active).show(scheduleFragment).commit();
                    active = scheduleFragment;
                    return true;
                case R.id.navigation_map:
                    fm.beginTransaction().hide(active).show(mapFragment).commit();
                    active = mapFragment;
                    return true;
                case R.id.navigation_mentors:
                    fm.beginTransaction().hide(active).show(mentorsFragment).commit();
                    active = mentorsFragment;
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.main_container, mapFragment, "4").hide(mapFragment).commit();
        fm.beginTransaction().add(R.id.main_container, mentorsFragment, "3").hide(mentorsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, qrFragment, "2").hide(qrFragment).commit();
        fm.beginTransaction().add(R.id.main_container, active, "1").commit();
    }

}
