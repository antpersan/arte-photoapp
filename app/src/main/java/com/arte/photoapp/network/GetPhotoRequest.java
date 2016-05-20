package com.arte.photoapp.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arte.photoapp.model.Character;

import org.json.JSONException;
import org.json.JSONObject;

public class GetPhotoRequest {


    public interface Callbacks {
        void onGetPhotoSuccess(Character photo);

        void onGetPhotoError();
    }

    private Context mContext;
    private Callbacks mCallbacks;
    private String mDetailUrl;

    public GetPhotoRequest(Context context, Callbacks callbacks, String detailUrl) {
        mContext = context;
        mCallbacks = callbacks;
        mDetailUrl = detailUrl;
    }

    public void execute() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getPhotoUrl(mDetailUrl), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Character character = new Character();
                        try {
                            JSONObject results = response.getJSONObject("results");
                            character.setId(results.getString("id"));
                            character.setName(results.getString("name"));
                            character.setDescription(results.getString("description"));
                            JSONObject images = results.getJSONObject("image");
                            character.setImageUrl(images.getString("screen_url"));
                            character.setThumbnailUrl(images.getString("thumb_url"));
                            character.setDetailUrl(results.getString("api_detail_url"));

                        } catch (JSONException e) {
                            mCallbacks.onGetPhotoError();
                        }

                        mCallbacks.onGetPhotoSuccess(character);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mCallbacks.onGetPhotoError();
                    }
                });
        RequestQueueManager.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }

    private String getPhotoUrl(String apiDetailUrl) {

        return apiDetailUrl+"?api_key=95d4b26d338d8c184569ff98f9360ce8352107ec&format=json";
    }
}
