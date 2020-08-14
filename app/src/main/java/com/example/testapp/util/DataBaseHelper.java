package com.example.testapp.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testapp.model.ProfileModel;

import java.util.ArrayList;
import java.util.List;

public final class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATA_BASE_NAME = "profileDataBase";
    private static final String TABLE_NAME = "profileTable";
    private static final String PROFILE_ID = "profileId";
    private static final String NAME = "name";
    private static final String IMAGE_URL = "imageUrl";
    private static final String GENDER = "gender";
    private static final String CITY = "city";
    private static final String AGE = "age";
    private static final String PROFILE_STATUS = "profileStatus";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATA_BASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + PROFILE_ID + " TEXT PRIMARY KEY, " + NAME + " TEXT, "
                + IMAGE_URL + " TEXT, " + GENDER + " TEXT, "
                + CITY + " TEXT, " + AGE + " INTEGER, "
                + PROFILE_STATUS + " INTEGER)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public void addProfiles(List<ProfileModel> profileModelList) {
        SQLiteDatabase database = getWritableDatabase();

        for (ProfileModel profileModel : profileModelList) {
            ContentValues values = new ContentValues();
            values.put(PROFILE_ID, profileModel.getProfileId());
            values.put(NAME, profileModel.getProfileName());
            values.put(IMAGE_URL, profileModel.getProfileImageUrl());
            values.put(GENDER, profileModel.getGender());
            values.put(CITY, profileModel.getCity());
            values.put(AGE, profileModel.getAge());
            values.put(PROFILE_STATUS, profileModel.getProfileStatus());
            database.insert(TABLE_NAME, null, values);
        }
        database.close();
    }

    @NonNull
    public List<ProfileModel> getProfileDataList() {
        List<ProfileModel> profileModelList = new ArrayList<>();
        SQLiteDatabase database = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = database.rawQuery(query, null);

        ProfileModel profileModel;
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                profileModel = new ProfileModel(cursor.getString(0),
                                                cursor.getString(1),
                                                cursor.getString(2),
                                                cursor.getString(3),
                                                cursor.getString(4),
                                                cursor.getInt(5));
                profileModel.setProfileStatus(cursor.getInt(6));
                profileModelList.add(profileModel);
            }
        }

        cursor.close();
        return profileModelList;
    }

    public void updateProfileStatus(@NonNull String profileId, int profileStatus) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PROFILE_STATUS, profileStatus);
        database.update(TABLE_NAME, values, PROFILE_ID + " = ?", new String[] {(profileId)});
        database.close();
    }
}
