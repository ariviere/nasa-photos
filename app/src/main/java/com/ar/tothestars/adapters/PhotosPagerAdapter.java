package com.ar.tothestars.adapters;

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

    private Context mContext;
    private ArrayList<APODPhoto> mPhotos;

    public PhotosPagerAdapter(Context context, ArrayList<APODPhoto> photos) {
        mContext = context;
        mPhotos = photos;
    }

    @Override
    public int getCount() {
        return mPhotos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        APODPhotoItem APODPhotoItem = new APODPhotoItem(mContext);
        APODPhotoItem.setModel(mPhotos.get(position));

        container.addView(APODPhotoItem);

        return APODPhotoItem;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((APODPhotoItem) object);
    }
}
