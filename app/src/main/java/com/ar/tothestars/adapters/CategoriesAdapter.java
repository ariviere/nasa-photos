package com.ar.tothestars.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.ar.tothestars.ui.APODPhotosList;
import com.ar.tothestars.ui.APODSavedPhotosList;

/**
 * Created by ariviere on 12/09/15.
 */
public class CategoriesAdapter extends PagerAdapter implements APODPhotosList.Listener,
    APODSavedPhotosList.Listener {

    private Context mContext;
    private Listener mListener;

    public CategoriesAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        switch (position) {
            case 0:
                APODPhotosList photosList = new APODPhotosList(mContext);
                photosList.setListener(this);
                container.addView(photosList);
                return photosList;
            case 1:
                APODSavedPhotosList savedPhotosList = new APODSavedPhotosList(mContext);
                savedPhotosList.setListener(this);
                container.addView(savedPhotosList);
                return savedPhotosList;
            default:
                return null;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void onRecyclerScrolled(int dy, int recyclerScrollY) {
        mListener.onRecyclerScrolled(dy, recyclerScrollY);
    }

    /**
     * set listener
     *
     * @param listener listener
     */
    public void setListener(Listener listener) {
        mListener = listener;
    }

    /**
     * listener
     */
    public interface Listener {
        /**
         * when recycler is scrolled
         *
         * @param dy dy
         * @param recyclerScrollY total scroll y
         */
        void onRecyclerScrolled(int dy, int recyclerScrollY);
    }
}
