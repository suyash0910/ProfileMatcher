package com.example.testapp.model;

import androidx.annotation.IntDef;

public class ProfileModel {

    @IntDef({ ProfileStatus.DEFAULT, ProfileStatus.ACCEPTED, ProfileStatus.REJECTED })
    public @interface ProfileStatus {

        int DEFAULT = 0;
        int ACCEPTED = 1;
        int REJECTED = 2;
    }

    private final String profileId;
    private final String profileName;
    private final String profileImageUrl;
    private final String gender;
    private final String city;
    private final int age;

    @ProfileStatus private int profileStatus;

    public ProfileModel(String profileId,
                        String profileName,
                        String profileImageUrl,
                        String gender,
                        String city,
                        int age) {
        this.profileId = profileId;
        this.profileName = profileName;
        this.profileImageUrl = profileImageUrl;
        this.gender = gender;
        this.city = city;
        this.age = age;
        this.profileStatus = ProfileStatus.DEFAULT;
    }

    public String getProfileId() {
        return profileId;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getGender() {
        return gender;
    }

    public String getCity() {
        return city;
    }

    public int getAge() {
        return age;
    }

    @ProfileStatus
    public int getProfileStatus() {
        return profileStatus;
    }

    public ProfileModel setProfileStatus(int profileStatus) {
        this.profileStatus = profileStatus;
        return this;
    }
}
