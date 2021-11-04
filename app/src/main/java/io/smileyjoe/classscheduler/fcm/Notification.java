package io.smileyjoe.classscheduler.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.StringRes;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;

import io.smileyjoe.classscheduler.R;

public class Notification {

    public enum Type{
        CHECK_ATTEND("check_attend", 1000, Channel.GENERAL);

        private String mDataValue;
        private int mGroupId;
        private Channel mChannel;

        Type(String dataValue, int groupId, Channel channel) {
            mDataValue = dataValue;
            mGroupId = groupId;
            mChannel = channel;
        }

        public String getDataValue() {
            return mDataValue;
        }

        public int getGroupId() {
            return mGroupId;
        }

        public Channel getChannel() {
            return mChannel;
        }

        public static Type fromDataValue(String dataValue){
            if(!TextUtils.isEmpty(dataValue)) {
                for (Type type : values()) {
                    if (type.getDataValue().equals(dataValue)) {
                        return type;
                    }
                }
            }

            return null;
        }
    }

    public enum Channel{
        GENERAL("channel_general", R.string.notification_channel_general_name, R.string.notification_channel_general_description);

        private String mId;
        private @StringRes int mName;
        private @StringRes int mDescription;

        Channel(String id, int name, int description) {
            mId = id;
            mName = name;
            mDescription = description;
        }

        public String getName(Context context){
            return context.getString(mName);
        }

        public String getDescription(Context context){
            return context.getString(mDescription);
        }

        public String getId(){
            return mId;
        }

        public void create(Context context){
            CharSequence name = getName(context);
            String description = getDescription(context);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(getId(), name, importance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        public static void createAll(Context context){
            for(Channel channel : values()){
                channel.create(context);
            }
        }
    }

    public static class Builder{

        private Context mContext;
        private String mTitle;
        private String mBody;
        private Type mType;
        private int mId;
        private List<NotificationCompat.Action> mActions;

        private Builder(Context context){
            mContext = context;
        }

        public static Builder with(Context context){
            return new Builder(context);
        }

        public Builder notification(RemoteMessage.Notification notification){
            title(notification.getTitle());
            body(notification.getBody());
            return this;
        }

        public Builder title(String title){
            mTitle = title;
            return this;
        }

        public Builder body(String body){
            mBody = body;
            return this;
        }

        public Builder type(Type type){
            mType = type;
            return this;
        }

        public Builder id(int id){
            mId = id;
            return this;
        }

        public Builder action(NotificationCompat.Action action){
            if(mActions == null){
                mActions = new ArrayList<>();
            }

            mActions.add(action);
            return this;
        }

        public void show(){
            if (!TextUtils.isEmpty(mTitle) && !TextUtils.isEmpty(mBody) && mId >= 0) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, mType.getChannel().getId())
                        .setContentTitle(mTitle)
                        .setContentText(mBody)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setGroup(mType.name());

                for(NotificationCompat.Action action : mActions){
                    builder.addAction(action);
                }

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
                notificationManager.notify(mId, builder.build());

                showGroupSummary();
            }
        }

        private void showGroupSummary(){
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);

            NotificationCompat.Builder groupBuilder =
                    new NotificationCompat.Builder(mContext, mType.getChannel().getId())
                            .setGroupSummary(true)
                            .setSmallIcon(R.drawable.ic_notification)
                            .setGroup(mType.name())
                            .setAutoCancel(true)
                            .setStyle(new NotificationCompat.InboxStyle());

            notificationManager.notify(mType.getGroupId(), groupBuilder.build());
        }

    }

}
