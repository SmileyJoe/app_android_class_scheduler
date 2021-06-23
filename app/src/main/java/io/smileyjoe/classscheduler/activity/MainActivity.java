package io.smileyjoe.classscheduler.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.fragment.AboutFragment;
import io.smileyjoe.classscheduler.fragment.ClassFragment;

public class MainActivity extends FragmentActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private AboutFragment mFragmentAbout;
    private ClassFragment mFragmentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupFragments(savedInstanceState);
        setupBottomNav();
    }

    private void setupFragments(Bundle savedInstanceState){
        if(savedInstanceState == null){
            mFragmentAbout = new AboutFragment();
            mFragmentClass = new ClassFragment();
        }
    }

    private void setupBottomNav(){
        BottomNavigationView bottomNavigationMain = findViewById(R.id.bottom_navigation_main);
        bottomNavigationMain.setOnNavigationItemSelectedListener(this);
        bottomNavigationMain.setSelectedItemId(R.id.menu_classes);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_about:
                changeFragment(mFragmentAbout);
                return true;
            case R.id.menu_classes:
                changeFragment(mFragmentClass);
                return true;
            default:
                return false;
        }
    }

    private void changeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container_main, fragment, null)
                .commit();
    }
}