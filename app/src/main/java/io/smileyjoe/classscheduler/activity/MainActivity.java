package io.smileyjoe.classscheduler.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.app.ActivityOptions;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.databinding.ActivityClassDetailsBinding;
import io.smileyjoe.classscheduler.databinding.ActivityMainBinding;
import io.smileyjoe.classscheduler.fragment.AboutFragment;
import io.smileyjoe.classscheduler.fragment.ClassFragment;
import io.smileyjoe.classscheduler.object.Schedule;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements BottomNavigationView.OnNavigationItemSelectedListener, ClassFragment.Listener {

    protected static final String FRAGMENT_CLASS = "classes";
    protected static final String FRAGMENT_ABOUT = "about";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setExitSharedElementCallback(new MaterialContainerTransformSharedElementCallback());

        getWindow().setSharedElementsUseOverlay(false);

        super.onCreate(savedInstanceState);
        setSupportActionBar(getView().toolbar);

        setupBottomNav();

        if(savedInstanceState == null) {
            getView().bottomNavigationMain.setSelectedItemId(R.id.menu_classes);
        }
    }

    @Override
    protected ActivityMainBinding inflate() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    private void setupBottomNav(){
        getView().bottomNavigationMain.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_about:
                changeFragment(FRAGMENT_ABOUT);
                return true;
            case R.id.menu_classes:
                changeFragment(FRAGMENT_CLASS);
                return true;
            default:
                return false;
        }
    }

    protected Fragment getNewFragment(String tag){
        switch (tag){
            case FRAGMENT_ABOUT:
                return new AboutFragment();
            case FRAGMENT_CLASS:
                return new ClassFragment();
            default:
                return null;
        }
    }

    protected void changeFragment(String tag){
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);

        if(fragment == null){
            fragment = getNewFragment(tag);
        }

        if(fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container_main, fragment, tag)
                    .commit();

            getSupportFragmentManager().executePendingTransactions();
        }
    }

    @Override
    public void onScheduleClicked(Schedule schedule, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, view, "shared_element_container");
            startActivity(ClassDetailsActivity.getIntent(getBaseContext(), schedule), options.toBundle());
        } else {
            startActivity(ClassDetailsActivity.getIntent(getBaseContext(), schedule));
        }

    }
}