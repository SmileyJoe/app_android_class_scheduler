package io.smileyjoe.classscheduler.activity;

import androidx.annotation.DimenRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

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

import java.lang.reflect.Field;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.databinding.ActivityClassDetailsBinding;
import io.smileyjoe.classscheduler.object.Schedule;
import io.smileyjoe.icons.Icon;

public class ClassDetailsActivity extends BaseActivity<ActivityClassDetailsBinding> {

    private static final String EXTRA_SCHEDULE = "schedule";

    private Schedule mSchedule;

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
}