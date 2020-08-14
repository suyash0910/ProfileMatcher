package com.example.testapp.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testapp.model.ProfileModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    /**
     * Method used to get list of {@link ProfileModel} from provided {@link JSONArray}.
     */
    @NonNull
    public static List<ProfileModel> parseProfileModels(@NonNull JSONArray jsonArray) {
        List<ProfileModel> profileModels = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject profileObject = jsonArray.getJSONObject(i);
                ProfileModel profileModel = new ProfileModel(getProfileId(profileObject),
                                                             getProfileName(profileObject),
                                                             getProfileImageUrl(profileObject),
                                                             profileObject.getString("gender"),
                                                             getCity(profileObject),
                                                             getAge(profileObject));
                profileModels.add(profileModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return profileModels;
    }

    private static String getProfileId(@NonNull JSONObject profileObject) {
        String profileId = null;
        try {
            JSONObject loginObject = profileObject.getJSONObject("login");
            profileId = loginObject.getString("uuid");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return profileId;
    }

    @Nullable
    private static String getProfileName(@NonNull JSONObject profileObject) {
        String profileName = null;
        try {
            JSONObject nameObject = profileObject.getJSONObject("name");
            profileName = nameObject.getString("title") + ". "
                    + nameObject.getString("first") + " "
                    + nameObject.getString("last");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return profileName;
    }

    @Nullable
    private static String getProfileImageUrl(@NonNull JSONObject profileObject) {
        String profileImageUrl = null;
        try {
            JSONObject pictureObject = profileObject.getJSONObject("picture");
            profileImageUrl = pictureObject.getString("medium");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return profileImageUrl;
    }

    @Nullable
    private static String getCity(@NonNull JSONObject profileObject) {
        String city = null;
        try {
            JSONObject locationObject = profileObject.getJSONObject("location");
            city = locationObject.getString("city");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return city;
    }

    private static int getAge(@NonNull JSONObject profileObject) {
        int age = -1;
        try {
            JSONObject dobObject = profileObject.getJSONObject("dob");
            age = dobObject.getInt("age");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return age;
    }
}
