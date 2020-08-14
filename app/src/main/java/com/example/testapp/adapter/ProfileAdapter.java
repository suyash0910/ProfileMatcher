package com.example.testapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.model.ProfileModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    @NonNull private List<ProfileModel> profileModels;
    @NonNull private ProfileStatusButtonOnClickListener profileStatusButtonOnClickListener;

    public ProfileAdapter(@NonNull List<ProfileModel> profileModels,
                          @NonNull ProfileStatusButtonOnClickListener profileStatusButtonOnClickListener) {
        this.profileModels = profileModels;
        this.profileStatusButtonOnClickListener = profileStatusButtonOnClickListener;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.profile_item, parent, false);
        return new ProfileViewHolder(view, profileStatusButtonOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Picasso.get()
                .load(profileModels.get(position).getProfileImageUrl())
                .placeholder(R.drawable.progress_image)
                .error(R.drawable.error_image)
                .into(holder.profileImage);

        holder.profileName.setText(profileModels.get(position).getProfileName());
        holder.ageTextView.setText(String.valueOf(profileModels.get(position).getAge()));
        holder.cityTextView.setText(profileModels.get(position).getCity());

        @ProfileModel.ProfileStatus int profileStatus = profileModels.get(position).getProfileStatus();
        switch (profileStatus) {
            case ProfileModel.ProfileStatus.ACCEPTED:
                holder.positiveButton.setText(R.string.accepted_member_text);
                holder.negativeButton.setText(R.string.reject_button_text);
                break;
            case ProfileModel.ProfileStatus.REJECTED:
                holder.positiveButton.setText(R.string.accept_button_text);
                holder.negativeButton.setText(R.string.declined_member_text);
                break;
            case ProfileModel.ProfileStatus.DEFAULT:
            default:
                holder.positiveButton.setText(R.string.accept_button_text);
                holder.negativeButton.setText(R.string.reject_button_text);
        }
    }

    @Override
    public int getItemCount() {
        return profileModels.size();
    }

    class ProfileViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage;
        TextView profileName;
        TextView ageTextView;
        TextView cityTextView;
        ToggleButton positiveButton;
        ToggleButton negativeButton;

        @NonNull private ProfileStatusButtonOnClickListener profileStatusButtonOnClickListener;

        ProfileViewHolder(@NonNull View itemView,
                          @NonNull ProfileStatusButtonOnClickListener profileStatusButtonOnClickListener) {
            super(itemView);
            this.profileStatusButtonOnClickListener = profileStatusButtonOnClickListener;

            profileImage = itemView.findViewById(R.id.profile_item_image);
            profileName = itemView.findViewById(R.id.profile_item_name);
            ageTextView = itemView.findViewById(R.id.profile_item_age_text);
            cityTextView = itemView.findViewById(R.id.profile_item_city_text);
            positiveButton = itemView.findViewById(R.id.profile_item_positive_button);
            negativeButton = itemView.findViewById(R.id.profile_item_negative_button);

            positiveButton.setOnCheckedChangeListener(getPositiveButtonClickListener());
            negativeButton.setOnCheckedChangeListener(getNegativeButtonClickListener());
        }

        private CompoundButton.OnCheckedChangeListener getPositiveButtonClickListener() {
            return new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Context context = buttonView.getContext();
                    int position = getAbsoluteAdapterPosition();
                    if (isChecked) {

                        // update views
                        positiveButton.setTextOn(context.getString(R.string.accepted_member_text));
                        negativeButton.setChecked(false);

                        // update models
                        ProfileModel changedModel = profileModels.get(position);
                        changedModel.setProfileStatus(ProfileModel.ProfileStatus.ACCEPTED);
                        profileModels.set(position, changedModel);
                        profileStatusButtonOnClickListener.profileStatusButtonClick(ProfileModel.ProfileStatus.ACCEPTED,
                                                                                    changedModel.getProfileId());
                    } else {

                        // update views
                        positiveButton.setTextOff(context.getString(R.string.accept_button_text));

                        // update models
                        ProfileModel changedModel = profileModels.get(position);
                        changedModel.setProfileStatus(ProfileModel.ProfileStatus.DEFAULT);
                        profileModels.set(position, changedModel);
                        profileStatusButtonOnClickListener.profileStatusButtonClick(ProfileModel.ProfileStatus.DEFAULT,
                                                                                    changedModel.getProfileId());
                    }
                }
            };
        }

        private CompoundButton.OnCheckedChangeListener getNegativeButtonClickListener() {
            return new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Context context = buttonView.getContext();
                    int position = getAbsoluteAdapterPosition();
                    if (isChecked) {

                        // update views
                        negativeButton.setTextOn(context.getString(R.string.declined_member_text));
                        positiveButton.setChecked(false);

                        // update models
                        ProfileModel changedModel = profileModels.get(position);
                        changedModel.setProfileStatus(ProfileModel.ProfileStatus.REJECTED);
                        profileModels.set(position, changedModel);
                        profileStatusButtonOnClickListener.profileStatusButtonClick(ProfileModel.ProfileStatus.REJECTED,
                                                                                    changedModel.getProfileId());
                    } else {

                        // update views
                        negativeButton.setTextOff(context.getString(R.string.reject_button_text));

                        // update models
                        ProfileModel changedModel = profileModels.get(position);
                        changedModel.setProfileStatus(ProfileModel.ProfileStatus.DEFAULT);
                        profileModels.set(position, changedModel);
                        profileStatusButtonOnClickListener.profileStatusButtonClick(ProfileModel.ProfileStatus.DEFAULT,
                                                                                    changedModel.getProfileId());
                    }
                }
            };
        }
    }

    public interface ProfileStatusButtonOnClickListener {

        void profileStatusButtonClick(@ProfileModel.ProfileStatus int profileStatus,
                                      @NonNull String profileId);
    }
}
