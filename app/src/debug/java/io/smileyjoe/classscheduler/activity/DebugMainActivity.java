package io.smileyjoe.classscheduler.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.fragment.StyleFragment;

public class DebugMainActivity extends MainActivity {

    protected static final String FRAGMENT_STYLE = "style";

    @Override
    protected void onNightModeChanged(int mode) {
        super.onNightModeChanged(mode);
        getView().bottomNavigationMain.setSelectedItemId(R.id.menu_style);
    }

    @Override
    protected Fragment getNewFragment(String tag) {
        switch (tag){
            case FRAGMENT_STYLE:
                return new StyleFragment();
            default:
                return super.getNewFragment(tag);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_style:
                changeFragment(FRAGMENT_STYLE);
                return true;
            default:
                return super.onNavigationItemSelected(item);
        }
    }
}
