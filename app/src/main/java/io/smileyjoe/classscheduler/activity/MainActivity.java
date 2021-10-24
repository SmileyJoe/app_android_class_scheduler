package io.smileyjoe.classscheduler.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavArgument;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
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
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.databinding.ActivityClassDetailsBinding;
import io.smileyjoe.classscheduler.databinding.ActivityMainBinding;
import io.smileyjoe.classscheduler.databinding.ListRowScheduleBinding;
import io.smileyjoe.classscheduler.fragment.AboutFragment;
import io.smileyjoe.classscheduler.fragment.AccountFragment;
import io.smileyjoe.classscheduler.fragment.ClassFragment;
import io.smileyjoe.classscheduler.object.Schedule;
import io.smileyjoe.classscheduler.utils.NavigationUtil;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements NavigationBarView.OnItemSelectedListener, ClassFragment.Listener, AccountFragment.Listener {

    protected NavController mNavController;

    private ActivityResultLauncher<Intent> mLoginLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setExitSharedElementCallback(new MaterialContainerTransformSharedElementCallback());

        getWindow().setSharedElementsUseOverlay(false);

        super.onCreate(savedInstanceState);
        setSupportActionBar(getView().toolbar);

        setupActivityResults();
        setupBottomNav();
    }

    private void setupActivityResults(){
        mLoginLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // TODO: this should load to the item that redirected to the login, not hard coded to this //
                        getView().bottomNavigationMain.setSelectedItemId(R.id.account);
                    }
                });
    }

    @Override
    protected ActivityMainBinding inflate() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    private void setupBottomNav(){
        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_main);
        mNavController = host.getNavController();

        NavigationUtil.setupWithNavController(getView().bottomNavigationMain, mNavController, this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        NavDestination destination = mNavController.getGraph().findNode(item.getItemId());

        if(destination != null) {
            Map<String, NavArgument> args = destination.getArguments();

            if (args.containsKey("requires_login")) {
                NavArgument arg = args.get("requires_login");
                boolean requiresLogin = (boolean) arg.getDefaultValue();

                if (requiresLogin && (FirebaseAuth.getInstance().getCurrentUser() == null)) {
                    mLoginLauncher.launch(LoginActivity.getIntent(getBaseContext()));
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void onLogout() {
        mNavController.navigate(R.id.classes);
    }

    @Override
    public void onScheduleClicked(Schedule schedule, ListRowScheduleBinding view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, view.findViewById(R.id.icon_main), "shared_element_container");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
                    Pair.create(view.iconMain, "icon"),
                    Pair.create(view.textDescription, "description_content"),
                    Pair.create(view.textTime, "time_content"));
            startActivity(ClassDetailsActivity.getIntent(getBaseContext(), schedule), options.toBundle());
        } else {
            startActivity(ClassDetailsActivity.getIntent(getBaseContext(), schedule));
        }
    }
}