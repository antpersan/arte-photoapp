package com.arte.photoapp.network;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arte.photoapp.model.Character;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetPhotoListRequest {

    private static final String PHOTO_LIST_URL = "http://www.giantbomb.com/api/characters/?api_key=95d4b26d338d8c184569ff98f9360ce8352107ec&format=json";

    public interface Callbacks {
        void onGetPhotoListSuccess(List<Character> photoList);

        void onGetPhotoListError();
    }

    private Context mContext;
    private Callbacks mCallbacks;

    public GetPhotoListRequest(Context context, Callbacks callbacks) {
        mContext = context;
        mCallbacks = callbacks;
    }

    public void execute() {

        // JSONArray volley request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, PHOTO_LIST_URL, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<Character> characters = new ArrayList<>();
                // Transform JSONArray to List<Photo>
                try {
                    JSONArray results = response.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject characterJson = (JSONObject) results.get(i);
                        Character character = new Character();
                        character.setId(characterJson.getString("id"));
                        character.setName(characterJson.getString("name"));
                        character.setDescription(characterJson.getString("description"));
                        JSONObject images = characterJson.getJSONObject("image");
                        character.setImageUrl(images.getString("icon_url"));
                        character.setThumbnailUrl(images.getString("thumb_url"));
                        character.setDetailUrl(characterJson.getString("api_detail_url"));
                        characters.add(character);
                    }
                    // call mCallbacks methods
                    mCallbacks.onGetPhotoListSuccess(characters);
                } catch (JSONException e) {
                    mCallbacks.onGetPhotoListError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mCallbacks.onGetPhotoListError();
            }
        });
        RequestQueueManager.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }
}
