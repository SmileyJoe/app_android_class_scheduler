package io.smileyjoe.classscheduler.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import io.smileyjoe.classscheduler.databinding.FragmentAboutBinding;
import io.smileyjoe.classscheduler.databinding.FragmentStyleBinding;

public class StyleFragment extends Fragment {

    private FragmentStyleBinding mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = FragmentStyleBinding.inflate(inflater, container, false);
        View view = mView.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mView.buttonDark.setOnClickListener(v -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES));
        mView.buttonLight.setOnClickListener(v -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO));
        mView.buttonDefault.setOnClickListener(v -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM));
    }
}
