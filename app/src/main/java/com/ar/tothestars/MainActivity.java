package com.ar.tothestars;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.ar.tothestars.adapters.PhotosAdapter;
import com.ar.tothestars.models.APODPhoto;
import com.ar.tothestars.services.APODManager;
import com.ar.tothestars.services.APODParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity {

    private final static String DATE_FORMAT = "yyyy-MM-dd";
    private final static String PHOTOS = "photos";
    private final static int LOADING_PHOTOS_COUNT = 10;

    private SimpleDateFormat mDateFormat;
    private Calendar mCalendarReference;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private ArrayList<APODPhoto> mPhotos;
    private PhotosAdapter mAdapter;

    private int mPhotosWithError = 0;

    private boolean mIsLoadingMore = false;
    private int mLoadingPhotos = LOADING_PHOTOS_COUNT;

    private Date mCurrentDateRequested;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.photos_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        mCalendarReference = Calendar.getInstance();

        if (savedInstanceState != null) {
            mPhotos = savedInstanceState.getParcelableArrayList(PHOTOS);
        } else {
            mPhotos = new ArrayList<>();
        }

        initViewPager();

        if (mPhotos.size() == 0) {
            Calendar calendar = (Calendar) mCalendarReference.clone();
            mCurrentDateRequested = calendar.getTime();
            getPhoto(calendar.getTime());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(PHOTOS, mPhotos);
    }

    private void getPhoto(Date photoDate) {
        String dateFormatted = mDateFormat.format(photoDate);

        APODManager.getClient()
                .getPhoto(dateFormatted, true, Credentials.NASA_KEY, new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        addPhoto(APODParser.getPhoto(s));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(MainActivity.this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addPhoto(APODPhoto photo) {
        photo.setDate(mCurrentDateRequested);
        Log.d("Photo loaded", mDateFormat.format(photo.getDate()));
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
        }
    }

    private Date getNextDate() {
        Calendar calendar = (Calendar) mCalendarReference.clone();
        calendar.add(Calendar.DATE, -(mPhotos.size() + mPhotosWithError));

        mCurrentDateRequested = calendar.getTime();

        return calendar.getTime();
    }

    private void initViewPager() {
        mAdapter = new PhotosAdapter(this, mPhotos);
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

}
