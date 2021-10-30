package io.smileyjoe.classscheduler.activity;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.database.DbUser;
import io.smileyjoe.classscheduler.databinding.ActivityClassDetailsBinding;
import io.smileyjoe.classscheduler.object.Schedule;
import io.smileyjoe.classscheduler.object.User;
import io.smileyjoe.classscheduler.utils.Communication;
import io.smileyjoe.icons.Icon;

public class ClassDetailsActivity extends BaseActivity<ActivityClassDetailsBinding> implements Communication.Listener {

    private static final String EXTRA_SCHEDULE = "schedule";

    private Schedule mSchedule;
    private User mUser;

    public static Intent getIntent(Context context, Schedule schedule){
        Intent intent = new Intent(context, ClassDetailsActivity.class);

        intent.putExtra(EXTRA_SCHEDULE, schedule);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        findViewById(android.R.id.content).setTransitionName("shared_element_container");
//        setEnterSharedElementCallback(new MaterialContainerTransformSharedElementCallback());
//
//        MaterialContainerTransform transform = new MaterialContainerTransform();
//        transform.addTarget(android.R.id.content);
//        transform.setDuration(300L);
//
//        TypedValue a = new TypedValue();
//        getTheme().resolveAttribute(android.R.attr.colorBackground, a, true);
//        transform.setAllContainerColors(a.data);
//
//        getWindow().setSharedElementEnterTransition(transform);
//        getWindow().setSharedElementReturnTransition(transform);
//        getWindow().setSharedElementsUseOverlay(false);

        super.onCreate(savedInstanceState);
        handleExtras();
        setupToolbar();
        populate();

        if(DbUser.isLoggedIn()) {
            DbUser.getDbReference(user -> {
                mUser = user;
                handleRegisterButton();
            });
        } else {
            getView().buttonWrapper.setVisibility(View.GONE);
        }
    }

    @Override
    protected ActivityClassDetailsBinding inflate() {
        return ActivityClassDetailsBinding.inflate(getLayoutInflater());
    }

    private void setupToolbar(){
        Toolbar toolbar = getView().toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        getView().toolbarCollapsing.setCollapsedTitleTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorOnPrimary));
    }

    private void handleExtras(){
        Bundle extras = getIntent().getExtras();

        mSchedule = extras.getParcelable(EXTRA_SCHEDULE);
    }

    private void populate(){
        if(!TextUtils.isEmpty(mSchedule.getIconName())){
            getView().layoutToolbarContent.setVisibility(View.VISIBLE);
            setAppBarHeight(R.dimen.class_details_toolbar_height);
            getView().toolbarCollapsing.setExpandedTitleGravity(Gravity.CENTER|Gravity.TOP);
            getView().imageIcon.setIcon(mSchedule.getIconName());
        } else {
            getView().layoutToolbarContent.setVisibility(View.GONE);
            setAppBarHeight(R.dimen.class_details_toolbar_small_height);
            getView().toolbarCollapsing.setExpandedTitleGravity(Gravity.CENTER);
        }

        getSupportActionBar().setTitle(mSchedule.getName());
        getView().detailDescription.setContent(mSchedule.getDescription());
        getView().detailTime.setContent(mSchedule.getTimeFormatted(getBaseContext()));
        getView().detailDetails.setContent(mSchedule.getDetails());
        getView().detailDay.setContent(mSchedule.getDay().getTitle(getBaseContext()));
    }

    private void setAppBarHeight(@DimenRes int dimension){
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) getView().appBar.getLayoutParams();
        params.height = getResources().getDimensionPixelOffset(dimension);
        getView().appBar.setLayoutParams(params);
    }

    private void handleRegisterButton(){
        if(mUser.isRegistered(mSchedule.getId())){
            getView().buttonRegister.setText(R.string.text_unregister);
            getView().buttonRegister.setOnClickListener(v -> DbUser.unregister(mUser, mSchedule.getId(), new UpdateComplete()));
        } else {
            getView().buttonRegister.setText(R.string.text_register);
            getView().buttonRegister.setOnClickListener(v -> DbUser.register(mUser, mSchedule.getId(), new UpdateComplete()));
        }
    }

    @Override
    public void success(int messageResId) {
        Communication.success(getView().getRoot(), messageResId);
    }

    @Override
    public void error(int messageResId) {
        Communication.success(getView().getRoot(), messageResId);
    }

    @Override
    public void error(String message) {
        Communication.success(getView().getRoot(), message);
    }

    private class UpdateComplete implements DatabaseReference.CompletionListener{
        @Override
        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
            if(error == null){
                success(R.string.text_success);
            } else {
                error(R.string.error_generic);
            }
        }
    }
}