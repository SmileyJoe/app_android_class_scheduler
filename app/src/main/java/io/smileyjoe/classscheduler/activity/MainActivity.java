package io.smileyjoe.classscheduler.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.app.ActivityOptions;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
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

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.databinding.ActivityClassDetailsBinding;
import io.smileyjoe.classscheduler.databinding.ActivityMainBinding;
import io.smileyjoe.classscheduler.fragment.AboutFragment;
import io.smileyjoe.classscheduler.fragment.ClassFragment;
import io.smileyjoe.classscheduler.object.Schedule;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ClassFragment.Listener {

    private ActivityMainBinding mView;
    private AboutFragment mFragmentAbout;
    private ClassFragment mFragmentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mView.getRoot();
        setContentView(view);

        setSupportActionBar(mView.toolbar);
        setupFragments(savedInstanceState);
        setupBottomNav();
        setProfileImage();

    }

    private void setProfileImage(){
//        ImageView imageProfile = findViewById(R.id.toolbar_profile_icon);
//
//        imageProfile.setImageResource(R.drawable.image_account_circle);


//        Glide.with(this)
//                .asDrawable()
//                .circleCrop()
//                .load(R.drawable.ic_account)
//                .placeholder(R.drawable.ic_account)
//                .error(R.drawable.ic_account)
//                .into();
    }

    private void setupFragments(Bundle savedInstanceState){
        if(savedInstanceState == null){
            mFragmentAbout = new AboutFragment();
            mFragmentClass = new ClassFragment();
        }
    }

    private void setupBottomNav(){
        mView.bottomNavigationMain.setOnNavigationItemSelectedListener(this);
        mView.bottomNavigationMain.setSelectedItemId(R.id.menu_classes);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Log.d("ImageThings", "Clicked nav");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    @Override
    public void onScheduleClicked(Schedule schedule, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
                    Pair.create(view, "icon"),
                    Pair.create(view, "description"),
                    Pair.create(view, "time"),
                    Pair.create(view, "name"));
            startActivity(ClassDetailsActivity.getIntent(getBaseContext(), schedule), options.toBundle());
        } else {
            startActivity(ClassDetailsActivity.getIntent(getBaseContext(), schedule));
        }

    }
}