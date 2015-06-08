package com.ar.tothestars;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.ar.tothestars.adapters.PhotosPagerAdapter;
import com.ar.tothestars.models.Photo;
import com.ar.tothestars.services.APODManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ActionBarActivity {

    private final static String DATE_FORMAT = "yyyy-MM-dd";
    private static final String PHOTOS = "photos";

    private SimpleDateFormat mDateFormat;
    private Calendar mCalendarReference;

    private ViewPager mPhotosPager;
    private ArrayList<Photo> mPhotos;
    private PhotosPagerAdapter mAdapter;

    private int mPhotosWithError = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPhotosPager = (ViewPager) findViewById(R.id.photos_pager);

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
                .getPhoto(dateFormatted, true, Credentials.NASA_KEY, new Callback<Photo>() {
                    @Override
                    public void success(Photo photo, Response response) {
                        addPhoto(photo);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(MainActivity.this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addPhoto(Photo photo) {
        if (photo.isValid() && photo.getUrl() != null && !photo.getUrl().equals("")) {
            mPhotos.add(photo);
        } else {
            mPhotosWithError++;
        }

        if (mPhotos.size() <= 3) {
            getPhoto(getNextDate());
        }

        mAdapter.notifyDataSetChanged();
    }

    private Date getNextDate() {
        Calendar calendar = (Calendar) mCalendarReference.clone();
        calendar.add(Calendar.DATE, -(mPhotos.size() + mPhotosWithError));

        return calendar.getTime();
    }

    private void initViewPager() {
        mAdapter = new PhotosPagerAdapter(this, mPhotos);
        mPhotosPager.setAdapter(mAdapter);
        mPhotosPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position + 3 > mPhotos.size()) {
                    getPhoto(getNextDate());
                }
            }
        });
    }

}
