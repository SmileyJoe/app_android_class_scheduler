package io.smileyjoe.classscheduler.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.database.DbSchedule;
import io.smileyjoe.classscheduler.database.DbUser;
import io.smileyjoe.classscheduler.databinding.DialogBottomSheetScheduleUserBinding;
import io.smileyjoe.classscheduler.databinding.FragmentStyleBinding;
import io.smileyjoe.classscheduler.databinding.ListRowScheduleBinding;
import io.smileyjoe.classscheduler.databinding.ListRowScheduleUserBinding;
import io.smileyjoe.classscheduler.object.Schedule;
import io.smileyjoe.classscheduler.object.User;
import io.smileyjoe.classscheduler.utils.Communication;
import io.smileyjoe.classscheduler.utils.LoadingData;
import io.smileyjoe.classscheduler.utils.StorageUtil;
import io.smileyjoe.classscheduler.viewholder.ScheduleViewHolder;

public class ScheduleUserBottomSheet extends BottomSheetDialogFragment implements ValueEventListener {

    public enum Type{
        ATTENDING(R.string.text_attending, R.string.error_no_attending_users),
        REGISTERED(R.string.text_registered, R.string.error_no_registered_users);

        private @StringRes int mTitle;
        private @StringRes int mErrorNoUsers;

        Type(int title, int errorNoUsers) {
            mTitle = title;
            mErrorNoUsers = errorNoUsers;
        }

        public int getTitle() {
            return mTitle;
        }

        public int getErrorNoUsers() {
            return mErrorNoUsers;
        }
    }

    private int mScheduleId;
    private DialogBottomSheetScheduleUserBinding mView;
    private DatabaseReference mDbSchedule;
    private Type mType;
    private Adapter mAdapter;

    public ScheduleUserBottomSheet(int scheduleId) {
        mScheduleId = scheduleId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = DialogBottomSheetScheduleUserBinding.inflate(inflater, container, false);
        mDbSchedule = DbSchedule.getDbReference(mScheduleId);
        View view = mView.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mDbSchedule.addValueEventListener(this);
        mAdapter = new Adapter();
        mView.recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mView.recycler.setAdapter(mAdapter);
        mView.textType.setText(mType.getTitle());
    }

    @Override
    public void onDestroy() {
        mDbSchedule.removeEventListener(this);
        super.onDestroy();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        mAdapter.setSchedule(DbSchedule.parse(snapshot));
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    public void show(@NonNull FragmentManager manager, ScheduleUserBottomSheet.Type type) {
        mType = type;
        show(manager, type.name());
    }

    private class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

        private Schedule mSchedule;

        public void setSchedule(Schedule schedule) {
            mSchedule = schedule;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ((ViewHolder) holder).onBind(getItem(position));
        }

        private User getItem(int position){
            switch (mType){
                case ATTENDING:
                    return mSchedule.getAttendingUsers().get(position);
                default:
                case REGISTERED:
                    return mSchedule.getRegisteredUsers().get(position);
            }
        }

        @Override
        public int getItemCount() {
            if(mSchedule != null) {
                switch (mType) {
                    case ATTENDING:
                        return mSchedule.getAttendingUsers().size();
                    default:
                    case REGISTERED:
                        return mSchedule.getRegisteredUsers().size();
                }
            } else {
                return 0;
            }
        }

        private class ViewHolder extends RecyclerView.ViewHolder{
            private ListRowScheduleUserBinding mView;
            private LoadingData mLoadingData;

            public ViewHolder(ViewGroup parent) {
                this(ListRowScheduleUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            }

            private ViewHolder(@NonNull ListRowScheduleUserBinding itemView) {
                super(itemView.getRoot());

                mView = itemView;

                mLoadingData = LoadingData.init()
                        .add(mView.textName, 24);
            }

            public void onBind(User user){
                mLoadingData.update(TextUtils.isEmpty(user.getUsername()));

                mView.textName.setText(user.getUsername());

                switch (mType){
                    case REGISTERED:
                        for(User attending:mSchedule.getAttendingUsers()){
                            if(attending.getId().equals(user.getId())){
                                mView.textIsAttending.setVisibility(View.VISIBLE);
                                break;
                            }
                        }
                        break;
                    case ATTENDING:
                        mView.textIsAttending.setVisibility(View.GONE);
                        break;
                }

                mView.imageProfile.load(user);
            }
        }
    }
}
