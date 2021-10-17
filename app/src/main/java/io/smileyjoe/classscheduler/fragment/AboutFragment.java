package io.smileyjoe.classscheduler.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.adapter.HeaderItemDecoration;
import io.smileyjoe.classscheduler.adapter.ScheduleAdapter;
import io.smileyjoe.classscheduler.databinding.FragmentAboutBinding;
import io.smileyjoe.classscheduler.object.About;
import io.smileyjoe.classscheduler.object.Schedule;
import io.smileyjoe.classscheduler.object.ScheduleComparator;

public class AboutFragment extends BaseFirebaseFragment<FragmentAboutBinding> {

    @Override
    protected FragmentAboutBinding inflate(LayoutInflater inflater, ViewGroup container, boolean savedInstanceState) {
        return FragmentAboutBinding.inflate(inflater, container, savedInstanceState);
    }

    @Override
    protected String getDbName() {
        return About.DB_NAME;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        About about = new About(snapshot);

        FragmentAboutBinding root = getRoot();

        root.textDescription.setText(about.getDescription());
        root.textName.setText(about.getName());
        root.textEmail.setText(about.getEmail());
        root.textPhone.setText(about.getPhone());
        root.textWebsite.setText(about.getWebsite());
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.w("DbThings", "Failed to read value.", error.toException());
    }
}
