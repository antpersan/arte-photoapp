package com.arte.photoapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.arte.photoapp.R;
import com.arte.photoapp.adapters.PhotoRecyclerViewAdapter;
import com.arte.photoapp.fragments.PhotoDetailFragment;
import com.arte.photoapp.model.Character;
import com.arte.photoapp.network.GetPhotoListRequest;

import java.util.ArrayList;
import java.util.List;

public class PhotoListActivity extends AppCompatActivity implements PhotoRecyclerViewAdapter.Events,
        GetPhotoListRequest.Callbacks {

    private boolean mTwoPane;
    private List<Character> mCharacterList = new ArrayList<>();
    private PhotoRecyclerViewAdapter mAdapter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);
        setupActivity();
        loadPhotos();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mProgressDialog.dismiss();
    }

    @Override
    public void onPhotoClicked(Character character) {
        if (mTwoPane) {
            Bundle fragmentArguments = new Bundle();
            fragmentArguments.putString(PhotoDetailFragment.ARG_CHARACTER_DETAIL_URL, character.getDetailUrl());
            PhotoDetailFragment fragment = new PhotoDetailFragment();
            fragment.setArguments(fragmentArguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.photo_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, PhotoDetailActivity.class);
            intent.putExtra(PhotoDetailFragment.ARG_CHARACTER_DETAIL_URL, character.getDetailUrl());
            startActivity(intent);
        }
    }

    private void setupActivity() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.photo_list);
        assert recyclerView != null;
        mAdapter = new PhotoRecyclerViewAdapter(mCharacterList, this, this);
        recyclerView.setAdapter(mAdapter);

        if (findViewById(R.id.photo_detail_container) != null) {
            mTwoPane = true;
        }

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.photo_detail_loading));
        mProgressDialog.show();
    }


    private void loadPhotos() {
        GetPhotoListRequest getPhotoListRequest = new GetPhotoListRequest(this, this);
        getPhotoListRequest.execute();
    }

    @Override
    public void onGetPhotoListSuccess(List<Character> characterList) {
        mProgressDialog.hide();
        mCharacterList.clear();
        mCharacterList.addAll(characterList);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onGetPhotoListError() {
        mProgressDialog.hide();
    }
}
