package io.smileyjoe.classscheduler.activity;

import androidx.annotation.DimenRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.databinding.ActivityClassDetailsBinding;
import io.smileyjoe.classscheduler.object.Schedule;
import io.smileyjoe.icons.Icon;

public class ClassDetailsActivity extends AppCompatActivity {

    private static final String EXTRA_SCHEDULE = "schedule";

    private Schedule mSchedule;
    private ActivityClassDetailsBinding mView;

    public static Intent getIntent(Context context, Schedule schedule){
        Intent intent = new Intent(context, ClassDetailsActivity.class);

        intent.putExtra(EXTRA_SCHEDULE, schedule);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = ActivityClassDetailsBinding.inflate(getLayoutInflater());
        View view = mView.getRoot();
        setContentView(view);
        handleExtras();
        setupToolbar();
        populate();
    }

    private void setupToolbar(){
        setSupportActionBar(mView.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mView.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        mView.toolbarCollapsing.setCollapsedTitleTextColor(ContextCompat.getColor(getBaseContext(), R.color.color_on_primary));
    }

    private void handleExtras(){
        Bundle extras = getIntent().getExtras();

        mSchedule = (Schedule) extras.getParcelable(EXTRA_SCHEDULE);
    }

    private void populate(){
        if(!TextUtils.isEmpty(mSchedule.getIconName())){
            mView.layoutToolbarContent.setVisibility(View.VISIBLE);
            setAppBarHeight(R.dimen.class_details_toolbar_height);
            mView.toolbarCollapsing.setExpandedTitleGravity(Gravity.CENTER|Gravity.TOP);
            Icon.load(getBaseContext(), mSchedule.getIconName(), icon -> mView.imageIcon.setImageDrawable(icon));
        } else {
            mView.layoutToolbarContent.setVisibility(View.GONE);
            setAppBarHeight(R.dimen.class_details_toolbar_small_height);
            mView.toolbarCollapsing.setExpandedTitleGravity(Gravity.CENTER);
        }

        getSupportActionBar().setTitle(mSchedule.getName());
        mView.detailDescription.setContent(mSchedule.getDescription());
        mView.detailTime.setContent(mSchedule.getTimeFormatted(getBaseContext()));
        mView.detailDetails.setContent(mSchedule.getDetails());
        mView.detailDay.setContent(mSchedule.getDay().getTitle(getBaseContext()));
    }

    private void setAppBarHeight(@DimenRes int dimension){
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mView.appBar.getLayoutParams();
        params.height = getResources().getDimensionPixelOffset(dimension);
        mView.appBar.setLayoutParams(params);
    }
}