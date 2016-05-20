package com.arte.photoapp.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.arte.photoapp.R;
import com.arte.photoapp.model.Character;
import com.arte.photoapp.network.GetPhotoRequest;
import com.arte.photoapp.network.RequestQueueManager;

public class PhotoDetailFragment extends Fragment implements GetPhotoRequest.Callbacks {

    public static final String ARG_CHARACTER_DETAIL_URL = "character_detail_url";

    private Character mPhoto;
    private NetworkImageView mImage;
    private ProgressDialog mProgressDialog;

    public PhotoDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_CHARACTER_DETAIL_URL)) {
            String photoDetailUrl = getArguments().getString(ARG_CHARACTER_DETAIL_URL);
            loadPhoto(photoDetailUrl);

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.photo_detail_loading));
        mProgressDialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        mProgressDialog.dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.photo_detail, container, false);


        mImage = (NetworkImageView) rootView.findViewById(R.id.photo_image);
        if (mPhoto != null) {
            loadPhotoDetails(mPhoto);
        }

        return rootView;
    }

    private void loadPhotoDetails(Character photo) {
        mPhoto = photo;
        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(mPhoto.getName());
        }
        mImage.setImageUrl(photo.getImageUrl(), RequestQueueManager.getInstance(activity).getImageLoader());
    }

    private void loadPhoto(String characterDetailUrl) {
        GetPhotoRequest getPhotoRequest = new GetPhotoRequest(getActivity(), this, characterDetailUrl);
        getPhotoRequest.execute();
    }

    @Override
    public void onGetPhotoSuccess(Character photo) {
        mProgressDialog.hide();
        loadPhotoDetails(photo);
    }

    @Override
    public void onGetPhotoError() {
        mProgressDialog.hide();
        Toast.makeText(getActivity(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
    }
}
