package com.arte.photoapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.arte.photoapp.R;
import com.arte.photoapp.model.Character;
import com.arte.photoapp.network.RequestQueueManager;

import java.util.List;

public class PhotoRecyclerViewAdapter extends RecyclerView.Adapter<PhotoRecyclerViewAdapter.ViewHolder> {

    public interface Events {
        void onPhotoClicked(Character character);
    }

    private final List<Character> mCharacterList;
    private Events mEvents;
    private Context mContext;

    public PhotoRecyclerViewAdapter(List<Character> items, Context context, Events events) {
        mCharacterList = items;
        mContext = context;
        mEvents = events;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Character character = mCharacterList.get(position);
        holder.mTitle.setText(character.getName());
        holder.mThumbnail.setImageUrl(character.getThumbnailUrl(), RequestQueueManager.getInstance(mContext).getImageLoader());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEvents.onPhotoClicked(character);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCharacterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final NetworkImageView mThumbnail;
        public final TextView mTitle;

        public ViewHolder(View view) {
            super(view);
            mThumbnail = (NetworkImageView) view.findViewById(R.id.photo_thumbnail);
            mTitle = (TextView) view.findViewById(R.id.photo_title);
        }
    }
}
