package io.smileyjoe.classscheduler.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.object.Schedule;

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private TextView mTextHeading;

    public HeaderViewHolder(@NonNull View itemView) {
        super(itemView);

        mContext = itemView.getContext();
        mTextHeading = itemView.findViewById(R.id.text_heading);
    }

    public void onBind(Schedule schedule){
        mTextHeading.setText(schedule.getDay().getTitle(mContext));
    }
}
