package com.ar.tothestars.navigation;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.ar.tothestars.listphotos.APODPhotosList;
import com.ar.tothestars.listsavedphotos.APODSavedPhotosList;

/**
 * Created by ariviere on 12/09/15.
 */
public class CategoriesAdapter extends PagerAdapter implements APODPhotosList.Listener,
    APODSavedPhotosList.Listener {

    private Context context;
    private Listener listener;
    private APODSavedPhotosList savedPhotosList;

    public CategoriesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        switch (position) {
            case 0:
                APODPhotosList photosList = new APODPhotosList(context);
                photosList.setListener(this);
                container.addView(photosList);
                return photosList;
            case 1:
                savedPhotosList = new APODSavedPhotosList(context);
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
        listener.onRecyclerScrolled(dy, recyclerScrollY);
    }

    @Override
    public void onFavoriteAdded() {
        savedPhotosList.onRefresh();
    }

    /**
     * set listener
     *
     * @param listener listener
     */
    public void setListener(Listener listener) {
        this.listener = listener;
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
