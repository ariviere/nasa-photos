package com.ar.tothestars.listsavedphotos;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ar.tothestars.services.Credentials;
import com.ar.tothestars.R;
import com.ar.tothestars.helpers.PhotoHelper;
import com.ar.tothestars.models.APODPhoto;
import com.ar.tothestars.services.APODManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ariviere on 12/09/15.
 */
public class APODSavedPhotosList extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener  {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private List<String> savedDates;
    private ArrayList<APODPhoto> photos;
    private SavedPhotosListAdapter adapter;

    private int loadingPhotos;

    private Listener listener;

    private int recyclerScrollY = 0;

    public APODSavedPhotosList(Context context) {
        super(context);

        if (!isInEditMode()) {
            init(context);
        }
    }

    public APODSavedPhotosList(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            init(context);
        }
    }

    public APODSavedPhotosList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (!isInEditMode()) {
            init(context);
        }
    }

    @Override
    public void onRefresh() {
        if (photos != null) {
            photos.clear();
        }
        refreshLayout.setRefreshing(true);
        adapter.notifyDataSetChanged();
        startGettingPhotos();
    }

    /**
     * set listener
     *
     * @param listener listener
     */
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private void startGettingPhotos() {
        String prefDates = PhotoHelper.getSavedDates(getContext());

        if (TextUtils.isEmpty(prefDates)) {
            findViewById(R.id.no_favorite).setVisibility(View.VISIBLE);
            refreshLayout.setRefreshing(false);
        } else {
            findViewById(R.id.no_favorite).setVisibility(View.GONE);
            savedDates = Arrays.asList(prefDates.split(";"));

            if (savedDates.size() > 0) {
                loadingPhotos = savedDates.size();
                getPhoto(getNextDate());
            }
        }
    }

    private void getPhoto(String photoDate) {

        APODManager.getClient()
                .getPhoto(photoDate, true, Credentials.NASA_KEY, new Callback<APODPhoto>() {
                    @Override
                    public void success(APODPhoto photo, Response response) {
                        addPhoto(photo);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addPhoto(APODPhoto photo) {
        if (photo.isValid() && photo.getUrl() != null && !photo.getUrl().equals("")) {
            photos.add(photo);
        }

        // load another photo until count is finished
        if (loadingPhotos > 1) {
            loadingPhotos--;
            getPhoto(getNextDate());
        } else {
            adapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
        }
    }

    private String getNextDate() {
        return savedDates.get(loadingPhotos - 1);
    }

    private void initRecyclerView() {
        adapter = new SavedPhotosListAdapter(getContext(), photos);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                recyclerScrollY += dy;
                listener.onRecyclerScrolled(dy, recyclerScrollY);
            }
        });
    }

    private void init(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_photos_list, this);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.photo_refresh);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setProgressViewEndTarget(false, 400);

        refreshLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                if (!TextUtils.isEmpty(PhotoHelper.getSavedDates(getContext()))) {
                    refreshLayout.setRefreshing(true);
                }
                return false;
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.photos_recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                recyclerView.setPadding(0, getResources().getDimensionPixelSize(R.dimen.main_menu_buttons_height), 0, 0);

                return false;
            }
        });


        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        photos = new ArrayList<>();

        initRecyclerView();

        startGettingPhotos();

    }

    /**
     * listener for photos list
     */
    public interface Listener {
        /**
         * called when recycler view is scrolled
         *
         * @param dy              dy
         * @param recyclerScrollY total scrollY
         */
        void onRecyclerScrolled(int dy, int recyclerScrollY);
    }
}
