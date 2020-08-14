package com.example.testapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.testapp.adapter.ProfileAdapter;
import com.example.testapp.model.ProfileModel;
import com.example.testapp.util.DataBaseHelper;
import com.example.testapp.util.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ProfileAdapter.ProfileStatusButtonOnClickListener {

    @NonNull private static final String URL = "https://randomuser.me/api/?results=10";

    private RecyclerView profileList;
    private ProgressBar loadingView;

    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingView = findViewById(R.id.loading_item);
        profileList = findViewById(R.id.profile_list);
        profileList.setLayoutManager(new LinearLayoutManager(this,
                                                             RecyclerView.HORIZONTAL,
                                                             false));

        dataBaseHelper = new DataBaseHelper(this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        ProfileLoadTask profileLoadTask = new ProfileLoadTask();
        profileLoadTask.execute(requestQueue);
    }

    @NonNull
    private Response.Listener<JSONObject> getResponseListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        List<ProfileModel> profileModels =
                                JsonParser.parseProfileModels(response.getJSONArray("results"));
                        dataBaseHelper.addProfiles(profileModels);
                        setupProfilesView(dataBaseHelper.getProfileDataList());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    @NonNull
    private Response.ErrorListener getErrorResponseListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setupProfilesView(dataBaseHelper.getProfileDataList());
            }
        };
    }

    private void setupProfilesView(@NonNull List<ProfileModel> profileModels) {
        loadingView.setVisibility(View.GONE);
        profileList.setVisibility(View.VISIBLE);
        profileList.setAdapter(new ProfileAdapter(profileModels, this));
    }

    @Override
    public void profileStatusButtonClick(@ProfileModel.ProfileStatus int profileStatus,
                                         @NonNull String profileId) {
        dataBaseHelper.updateProfileStatus(profileId, profileStatus);
    }

    class ProfileLoadTask extends AsyncTask<RequestQueue, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(RequestQueue... requestQueues) {
            JsonObjectRequest jsonObjectRequest =
                    new JsonObjectRequest(Request.Method.GET,
                            URL,
                            null,
                            getResponseListener(),
                            getErrorResponseListener());
            requestQueues[0].add(jsonObjectRequest);
            return null;
        }
    }
}
