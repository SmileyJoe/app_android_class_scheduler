package io.smileyjoe.classscheduler.activity;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
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
import io.smileyjoe.classscheduler.database.DbSchedule;
import io.smileyjoe.classscheduler.database.DbUser;
import io.smileyjoe.classscheduler.databinding.ActivityClassDetailsBinding;
import io.smileyjoe.classscheduler.dialog.ScheduleUserBottomSheet;
import io.smileyjoe.classscheduler.object.Schedule;
import io.smileyjoe.classscheduler.object.User;
import io.smileyjoe.classscheduler.utils.Communication;
import io.smileyjoe.icons.Icon;

public class ClassDetailsActivity extends BaseActivity<ActivityClassDetailsBinding> implements Communication.Listener, ValueEventListener {

    private static final String EXTRA_SCHEDULE = "schedule";

    private Schedule mSchedule;
    private User mUser;
    private ScheduleUserBottomSheet mSheetAttending;
    private ScheduleUserBottomSheet mSheetRegistered;
    private ValueEventListener mUserChanged = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            mUser = DbUser.parse(snapshot);
            handleRegisterButton();
            handleAttendingButton();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

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
        setupViews();
        populate();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(DbUser.isLoggedIn()) {
            DbUser.getDbReference().addValueEventListener(mUserChanged);
        } else {
            getView().buttonWrapper.setVisibility(View.GONE);
        }

        DbSchedule.getDbReference(mSchedule.getId()).addValueEventListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(DbUser.isLoggedIn()) {
            DbUser.getDbReference().removeEventListener(mUserChanged);
        }
        DbSchedule.getDbReference(mSchedule.getId()).removeEventListener(this);
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

    private void setupViews(){
        mSheetAttending = new ScheduleUserBottomSheet(mSchedule.getId(), ScheduleUserBottomSheet.Type.ATTENDING);
        mSheetRegistered = new ScheduleUserBottomSheet(mSchedule.getId(), ScheduleUserBottomSheet.Type.REGISTERED);

        getView().imageAttending.setOnClickListener(view -> {
            if(mSchedule.getAttendingUsers().size() > 0) {
                mSheetAttending.show(getSupportFragmentManager(), ScheduleUserBottomSheet.Type.ATTENDING.name());
            } else {
                error(R.string.error_no_attending_users);
            }
        });
        getView().imageRegistered.setOnClickListener(view -> {
            if(mSchedule.getAttendingUsers().size() > 0) {
                mSheetRegistered.show(getSupportFragmentManager(), ScheduleUserBottomSheet.Type.REGISTERED.name());
            } else {
                error(R.string.error_no_registered_users);
            }
        });
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

        getView().imageAttending.setTitle(getView().imageAttending.getTitleDefault() + String.format(" (%d)", mSchedule.getAttendingUsers().size()));
        getView().imageRegistered.setTitle(getView().imageRegistered.getTitleDefault() + String.format(" (%d)", mSchedule.getRegisteredUsers().size()));
    }

    private void setAppBarHeight(@DimenRes int dimension){
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) getView().appBar.getLayoutParams();
        params.height = getResources().getDimensionPixelOffset(dimension);
        getView().appBar.setLayoutParams(params);
    }

    private void handleRegisterButton(){
        if(mUser.isRegistered(mSchedule.getId())){
            getView().buttonRegister.setText(R.string.text_unregister);
            getView().buttonRegister.setOnClickListener(v -> DbUser.unregister(mUser, mSchedule.getId(), new UpdateComplete(R.string.success_unregister)));
        } else {
            getView().buttonRegister.setText(R.string.text_register);
            getView().buttonRegister.setOnClickListener(v -> DbUser.register(mUser, mSchedule.getId(), new UpdateComplete(R.string.success_register)));
        }
    }

    private void handleAttendingButton(){
        if(mUser.isAttending(mSchedule.getId())){
            getView().buttonAttending.setText(R.string.text_cancel);
            getView().buttonAttending.setOnClickListener(v -> DbUser.cancel(mUser, mSchedule.getId(), new UpdateComplete(R.string.success_cancel_attending)));
        } else {
            getView().buttonAttending.setText(R.string.text_attend);
            getView().buttonAttending.setOnClickListener(v -> DbUser.attend(mUser, mSchedule.getId(), new UpdateComplete(R.string.success_attending)));
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        mSchedule = DbSchedule.parse(snapshot);
        populate();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    public void success(int messageResId) {
        Communication.success(getView().getRoot(), messageResId, false);
    }

    @Override
    public void error(int messageResId) {
        error(getString(messageResId));
    }

    @Override
    public void error(String message) {
        Communication.success(getView().getRoot(), message, false);
    }

    private class UpdateComplete implements DatabaseReference.CompletionListener{
        @StringRes int mMessageSuccess;

        public UpdateComplete(@StringRes int messageSuccess) {
            mMessageSuccess = messageSuccess;
        }

        @Override
        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
            if(error == null){
                success(mMessageSuccess);
            } else {
                error(R.string.error_generic);
            }
        }
    }
}