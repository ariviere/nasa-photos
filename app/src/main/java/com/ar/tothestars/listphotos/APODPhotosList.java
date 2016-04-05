package com.ar.tothestars.listphotos;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ar.tothestars.R;
import com.ar.tothestars.models.APODPhoto;
import com.ar.tothestars.services.APODManager;
import com.ar.tothestars.services.Credentials;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ariviere on 12/09/15.
 */
public class APODPhotosList extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener,
        PhotosListAdapter.Listener {

    private final static String DATE_FORMAT = "yyyy-MM-dd";
    private final static int LOADING_PHOTOS_COUNT = 7;

    private SimpleDateFormat dateFormat;
    private Calendar calendarReference;

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private ArrayList<APODPhoto> photos;
    private PhotosListAdapter adapter;

    private int photosWithError = 0;

    private boolean isLoadingMore = false;
    private int loadingPhotos;

    private Listener listener;

    private int recyclerScrollY = 0;

    public APODPhotosList(Context context) {
        super(context);

        if (!isInEditMode()) {
            init(context);
        }
    }

    public APODPhotosList(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            init(context);
        }
    }

    public APODPhotosList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (!isInEditMode()) {
            init(context);
        }
    }

    @Override
    public void onRefresh() {
        photos.clear();
        photosWithError = 0;
        calendarReference = Calendar.getInstance();
        startGettingPhotos();
    }

    @Override
    public void onSavedButtonClicked() {
        listener.onFavoriteAdded();
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
        loadingPhotos = LOADING_PHOTOS_COUNT;
        Calendar calendar = (Calendar) calendarReference.clone();
        getPhoto(calendar.getTime());
    }

    private void getPhoto(Date photoDate) {
        String dateFormatted = dateFormat.format(photoDate);

        APODManager.getClient()
                .getPhoto(dateFormatted, true, Credentials.NASA_KEY, new Callback<APODPhoto>() {
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
        } else {
            photosWithError++;
        }

        // load another photo until count is finished
        if (loadingPhotos > 0) {
            loadingPhotos--;
            getPhoto(getNextDate());
        } else {
            isLoadingMore = false;
            adapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
        }
    }

    private Date getNextDate() {
        Calendar calendar = (Calendar) calendarReference.clone();
        calendar.add(Calendar.DATE, -(photos.size() + photosWithError));

        return calendar.getTime();
    }

    private void initRecyclerView() {
        adapter = new PhotosListAdapter(getContext(), photos);
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                recyclerScrollY += dy;
                listener.onRecyclerScrolled(dy, recyclerScrollY);

                if (!isLoadingMore
                        && layoutManager.findLastVisibleItemPosition() > photos.size() - 3) {
                    isLoadingMore = true;
                    loadingPhotos = LOADING_PHOTOS_COUNT;
                    getPhoto(getNextDate());
                }
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
                refreshLayout.setRefreshing(true);
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

        dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        calendarReference = Calendar.getInstance();
        calendarReference.add(Calendar.HOUR, -2);

        photos = new ArrayList<>();

        initRecyclerView();

        if (photos.size() == 0) {
            startGettingPhotos();
        }

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

        /**
         * called when a favorite is added
         */
        void onFavoriteAdded();
    }
}
