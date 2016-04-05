package com.ar.tothestars.listphotos;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.ar.tothestars.models.APODPhoto;
import com.ar.tothestars.ui.APODPhotoItem;

import java.util.ArrayList;

/**
 * Created by ariviere on 07/06/15.
 */
public class PhotosPagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<APODPhoto> photos;

    public PhotosPagerAdapter(Context context, ArrayList<APODPhoto> photos) {
        this.context = context;
        this.photos = photos;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        APODPhotoItem APODPhotoItem = new APODPhotoItem(context);
        APODPhotoItem.setModel(photos.get(position), position);

        container.addView(APODPhotoItem);

        return APODPhotoItem;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((APODPhotoItem) object);
    }
}
