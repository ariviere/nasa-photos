package com.ar.tothestars.listsavedphotos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ar.tothestars.models.APODPhoto;
import com.ar.tothestars.ui.APODPhotoItem;

import java.util.ArrayList;

/**
 * Created by ariviere on 09/08/15.
 */
public class SavedPhotosListAdapter extends RecyclerView.Adapter<SavedPhotosListAdapter.ViewHolder>
    implements APODPhotoItem.Listener {

    private final Context context;
    private ArrayList<APODPhoto> photos;

    public SavedPhotosListAdapter(Context context, ArrayList<APODPhoto> photos) {
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
        photos.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, photos.size());
    }

}
