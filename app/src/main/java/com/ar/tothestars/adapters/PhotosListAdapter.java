package com.ar.tothestars.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ar.tothestars.models.APODPhoto;
import com.ar.tothestars.ui.APODPhotoItem;

import java.util.ArrayList;

/**
 * Created by ariviere on 09/08/15.
 */
public class PhotosListAdapter extends RecyclerView.Adapter<PhotosListAdapter.ViewHolder>
    implements APODPhotoItem.Listener {

    private final Context mContext;
    private ArrayList<APODPhoto> mPhotos;
    private Listener mListener;

    public PhotosListAdapter(Context context, ArrayList<APODPhoto> mPhotos) {
        this.mContext = context;
        this.mPhotos = mPhotos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(new APODPhotoItem(mContext));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.photoView.setModel(mPhotos.get(i));
        viewHolder.photoView.setListener(this);
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private APODPhotoItem photoView;

        public ViewHolder(APODPhotoItem view) {
            super(view);
            photoView = view;
        }
    }

    @Override
    public void onSaveButtonClicked() {
        mListener.onSavedButtonClicked();
    }

    public void setmListener(Listener mListener) {
        this.mListener = mListener;
    }

    public interface Listener {
        /**
         * Triggered when user click on the save button
         */
        void onSavedButtonClicked();
    }
}
