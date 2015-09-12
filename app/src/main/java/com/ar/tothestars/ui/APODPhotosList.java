package com.ar.tothestars.ui;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ar.tothestars.Credentials;
import com.ar.tothestars.R;
import com.ar.tothestars.adapters.PhotosAdapter;
import com.ar.tothestars.models.APODPhoto;
import com.ar.tothestars.services.APODManager;

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
public class APODPhotosList extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener {

    private final static String DATE_FORMAT = "yyyy-MM-dd";
    private final static String PHOTOS = "photos";
    private final static int LOADING_PHOTOS_COUNT = 2;

    private SimpleDateFormat mDateFormat;
    private Calendar mCalendarReference;

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private ArrayList<APODPhoto> mPhotos;
    private PhotosAdapter mAdapter;

    private int mPhotosWithError = 0;

    private boolean mIsLoadingMore = false;
    private int mLoadingPhotos;

    private Date mCurrentDateRequested;

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
        mPhotos.clear();
        startGettingPhotos();
    }

    private void startGettingPhotos() {
        mLoadingPhotos = LOADING_PHOTOS_COUNT;
        Calendar calendar = (Calendar) mCalendarReference.clone();
        mCurrentDateRequested = calendar.getTime();
        getPhoto(calendar.getTime());
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
        } else {
            mPhotosWithError++;
        }

        // load another photo until count is finished
        if (mLoadingPhotos > 0) {
            mLoadingPhotos--;
            getPhoto(getNextDate());
        } else {
            mIsLoadingMore = false;
            mAdapter.notifyDataSetChanged();
            mRefreshLayout.setRefreshing(false);
        }
    }

    private Date getNextDate() {
        Calendar calendar = (Calendar) mCalendarReference.clone();
        calendar.add(Calendar.DATE, -(mPhotos.size() + mPhotosWithError));

        mCurrentDateRequested = calendar.getTime();

        return calendar.getTime();
    }

    private void initRecyclerView() {
        mAdapter = new PhotosAdapter(getContext(), mPhotos);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!mIsLoadingMore
                        && mLayoutManager.findLastVisibleItemPosition() > mPhotos.size() - 3) {
                    mIsLoadingMore = true;
                    mLoadingPhotos = LOADING_PHOTOS_COUNT;
                    getPhoto(getNextDate());
                }
            }
        });
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_photos_list, this);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.photo_refresh);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setProgressViewEndTarget(false, 300);

        mRecyclerView = (RecyclerView) findViewById(R.id.photos_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        mCalendarReference = Calendar.getInstance();
        mCalendarReference.add(Calendar.HOUR, -2);

        mPhotos = new ArrayList<>();

        initRecyclerView();

        if (mPhotos.size() == 0) {
            startGettingPhotos();
        }

    }
}
