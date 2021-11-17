package io.smileyjoe.classscheduler.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.object.User;
import io.smileyjoe.classscheduler.utils.StorageUtil;

public class ProfileImage extends ShapeableImageView {

    public enum Type{
        VIEW(0, R.drawable.ic_account),
        EDIT(1, R.drawable.ic_plus);

        private int mAttrId;
        private @DrawableRes int mIcon;

        Type(int attrId, int icon) {
            mAttrId = attrId;
            mIcon = icon;
        }

        public int getAttrId() {
            return mAttrId;
        }

        public int getIcon() {
            return mIcon;
        }

        public static Type fromAttr(int id){
            for(Type type:values()){
                if(type.mAttrId == id){
                    return type;
                }
            }

            return null;
        }
    }

    private Type mType;
    private ColorStateList mTint;
    private ScaleType mScaleType;

    public ProfileImage(Context context) {
        super(context);
        init(null);
    }

    public ProfileImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ProfileImage(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        if (attrs != null) {
            TypedArray typedProfileImage = getContext().obtainStyledAttributes(attrs, R.styleable.ProfileImage, 0, 0);

            setType(Type.fromAttr(typedProfileImage.getInt(R.styleable.ProfileImage_profile_image_type, Type.VIEW.getAttrId())));

            typedProfileImage.recycle();
        }

        mTint = getImageTintList();
        mScaleType = getScaleType();

        showDefault();
    }

    private void setType(Type type) {
        mType = type;
    }

    private void showDefault(){
        setImageResource(mType.getIcon());
        setScaleType(mScaleType);
        setImageTintList(mTint);
    }

    public void load(User user){
        if(user.hasProfileImage()){
            StorageUtil.getProfileImage(user.getId(), uri -> {
                setScaleType(ImageView.ScaleType.CENTER);
                setImageTintList(null);
                Glide.with(getContext())
                        .load(uri)
                        .into(this);
            });
        } else {
            showDefault();
        }
    }
}
