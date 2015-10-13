package com.ar.tothestars.ui;

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

import com.ar.tothestars.Credentials;
import com.ar.tothestars.R;
import com.ar.tothestars.adapters.PhotosListAdapter;
import com.ar.tothestars.adapters.SavedPhotosListAdapter;
import com.ar.tothestars.helpers.PhotoHelper;
import com.ar.tothestars.models.APODPhoto;
import com.ar.tothestars.services.APODManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ariviere on 12/09/15.
 */
public class APODSavedPhotosList extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener  {

    private final static String DATE_FORMAT = "yyyy-MM-dd";

    private SimpleDateFormat mDateFormat;

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private List<String> mSavedDates;
    private ArrayList<APODPhoto> mPhotos;
    private SavedPhotosListAdapter mAdapter;


    private int mLoadingPhotos;

    private Date mCurrentDateRequested;
    private Listener mListener;

    private int mRecyclerScrollY = 0;

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
        if (mPhotos != null) {
            mPhotos.clear();
        }
        mRefreshLayout.setRefreshing(true);
        startGettingPhotos();
    }

    /**
     * set listener
     *
     * @param listener listener
     */
    public void setListener(Listener listener) {
        mListener = listener;
    }

    private void startGettingPhotos() {
        String prefDates = PhotoHelper.getSavedDates(getContext());

        if (TextUtils.isEmpty(prefDates)) {
            findViewById(R.id.no_favorite).setVisibility(View.VISIBLE);
            mRefreshLayout.setRefreshing(false);
        } else {
            findViewById(R.id.no_favorite).setVisibility(View.GONE);
            mSavedDates = Arrays.asList(prefDates.split(";"));

            if (mSavedDates.size() > 0) {
                mLoadingPhotos = mSavedDates.size();
                getPhoto(getNextDate());
            }
        }
    }

    private void getPhoto(Date photoDate) {
        String dateFormatted = mDateFormat.format(photoDate);

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
        photo.setDate(mCurrentDateRequested);

        if (photo.isValid() && photo.getUrl() != null && !photo.getUrl().equals("")) {
            mPhotos.add(photo);
        }

        // load another photo until count is finished
        if (mLoadingPhotos > 1) {
            mLoadingPhotos--;
            getPhoto(getNextDate());
        } else {
            mAdapter.notifyDataSetChanged();
            mRefreshLayout.setRefreshing(false);
        }
    }

    private Date getNextDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(mSavedDates.get(mLoadingPhotos - 1)));

        mCurrentDateRequested = calendar.getTime();

        return mCurrentDateRequested;
    }

    private void initRecyclerView() {
        mAdapter = new SavedPhotosListAdapter(getContext(), mPhotos);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                mRecyclerScrollY += dy;
                mListener.onRecyclerScrolled(dy, mRecyclerScrollY);
            }
        });
    }

    private void init(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_photos_list, this);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.photo_refresh);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setProgressViewEndTarget(false, 400);

        mRefreshLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                mRefreshLayout.setRefreshing(true);
                return false;
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.photos_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                mRecyclerView.setPadding(0, getResources().getDimensionPixelSize(R.dimen.main_menu_buttons_height), 0, 0);

                return false;
            }
        });


        mDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mPhotos = new ArrayList<>();

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
