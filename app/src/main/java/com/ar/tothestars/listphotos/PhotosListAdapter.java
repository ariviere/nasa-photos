package com.ar.tothestars.listphotos;

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

    private final Context context;
    private ArrayList<APODPhoto> photos;
    private Listener listener;

    public PhotosListAdapter(Context context, ArrayList<APODPhoto> photos) {
        this.context = context;
        this.photos = photos;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(new APODPhotoItem(context));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.photoView.setModel(photos.get(i), i);
        viewHolder.photoView.setListener(this);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private APODPhotoItem photoView;

        public ViewHolder(APODPhotoItem view) {
            super(view);
            photoView = view;
        }
    }

    @Override
    public void onSaveButtonClicked(int position) {
        listener.onSavedButtonClicked();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        /**
         * Triggered when user click on the save button
         */
        void onSavedButtonClicked();
    }
}
