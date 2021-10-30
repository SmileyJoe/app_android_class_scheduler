package io.smileyjoe.classscheduler.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import io.smileyjoe.classscheduler.object.About;
import io.smileyjoe.classscheduler.utils.Utils;

public class DbAbout {

    private static final String DB_NAME = "about";
    private static final String DB_KEY_DESCRIPTION = "description";
    private static final String DB_KEY_EMAIL = "email";
    private static final String DB_KEY_NAME = "name";
    private static final String DB_KEY_PHONE = "phone";
    private static final String DB_KEY_WEBSITE = "website";

    public static About parse(DataSnapshot itemSnapshot){
        About about = new About();

        about.setDescription(itemSnapshot.child(DB_KEY_DESCRIPTION).getValue(String.class));
        about.setEmail(itemSnapshot.child(DB_KEY_EMAIL).getValue(String.class));
        about.setName(itemSnapshot.child(DB_KEY_NAME).getValue(String.class));
        about.setPhone(itemSnapshot.child(DB_KEY_PHONE).getValue(String.class));
        about.setWebsite(itemSnapshot.child(DB_KEY_WEBSITE).getValue(String.class));

        return about;
    }

    public static DatabaseReference getDbReference(){
        return Utils.getDb().getReference(DB_NAME);
    }
}
