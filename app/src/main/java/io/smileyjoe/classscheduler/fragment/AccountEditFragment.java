package io.smileyjoe.classscheduler.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.navigation.Navigation;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.database.DbUser;
import io.smileyjoe.classscheduler.databinding.FragmentAccountEditBinding;
import io.smileyjoe.classscheduler.object.User;
import io.smileyjoe.classscheduler.utils.Communication;

public class AccountEditFragment extends BaseFirebaseFragment<FragmentAccountEditBinding> implements DatabaseReference.CompletionListener {

    private User mUser;
    private ActivityResultLauncher<CropImageContractOptions> mCropLauncher;

    @Override
    protected FragmentAccountEditBinding inflate(LayoutInflater inflater, ViewGroup container, boolean savedInstanceState) {
        return FragmentAccountEditBinding.inflate(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupActivityResults();
        getRoot().buttonSave.setOnClickListener(v -> save());
        getRoot().imageProfile.setOnClickListener(v -> {
            CropImageContractOptions options = new CropImageContractOptions(null, new CropImageOptions())
                    .setScaleType(CropImageView.ScaleType.CENTER)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .setMaxZoom(8)
                    .setAutoZoomEnabled(true)
                    .setAllowFlipping(false)
                    .setActivityTitle(getString(R.string.menu_profile_image))
                    .setActivityMenuIconColor(ContextCompat.getColor(getContext(), R.color.colorPrimary))
                    .setCropMenuCropButtonIcon(R.drawable.ic_check)
                    .setAllowRotation(false)
                    .setFixAspectRatio(true);

            mCropLauncher.launch(options);

        });
    }

    private void setupActivityResults(){
        mCropLauncher = registerForActivityResult(
                new CropImageContract(),
                result -> {
                    if (result.isSuccessful()) {
                        getRoot().imageProfile.setScaleType(ImageView.ScaleType.CENTER);
                        getRoot().imageProfile.setImageTintList(null);
                        getRoot().imageProfile.setImageURI(result.getUriContent());
                    } else {
                        error(R.string.error_image_select);
                    }
                }
        );
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        mUser = DbUser.parse(snapshot);

        if(getRoot() != null) {
            getRoot().inputUsername.getEditText().setText(mUser.getUsername());
            getRoot().inputPhoneNumber.getEditText().setText(mUser.getPhoneNumber());
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    protected DatabaseReference getDatabaseReference() {
        return DbUser.getDbReference();
    }

    private void save(){
        mUser.setUsername(getRoot().inputUsername.getEditText().getText().toString());
        mUser.setPhoneNumber(getRoot().inputPhoneNumber.getEditText().getText().toString());
        DbUser.updateProfile(mUser, this);
    }

    @Override
    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
        if(error == null){
            success(R.string.success_account_update);
            Navigation.findNavController(getRoot().getRoot()).popBackStack();
        } else {
            error(R.string.error_account_update);
        }
    }
}
