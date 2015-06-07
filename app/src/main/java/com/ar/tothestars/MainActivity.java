package com.ar.tothestars;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.ar.tothestars.adapters.PhotosPagerAdapter;
import com.ar.tothestars.models.Photo;
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


public class MainActivity extends ActionBarActivity {

    private final static String DATE_FORMAT = "yyyy-MM-dd";
    private static final String PHOTOS = "photos";

    private SimpleDateFormat mDateFormat;
    private Calendar calendarReference;

    private ViewPager mPhotosPager;
    private ArrayList<Photo> mPhotos;
    private PhotosPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPhotosPager = (ViewPager) findViewById(R.id.photos_pager);

        mDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        calendarReference = Calendar.getInstance();

        if (savedInstanceState != null) {
            mPhotos = savedInstanceState.getParcelableArrayList(PHOTOS);
        }

        if (mPhotos == null) {
            Calendar calendar = (Calendar)calendarReference.clone();
            getPhoto(calendar.getTime());
        } else {
            initViewPager();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(PHOTOS, mPhotos);
    }

    private void getPhoto(Date date) {
        String dateFormatted = mDateFormat.format(date);

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

    private void addPhoto(Photo photo) {
        if (mPhotos == null) {
            mPhotos = new ArrayList<>();

            initViewPager();
        }

        mPhotos.add(photo);

        if (mPhotos.size() <= 3) {
            getPhoto(getNextDate());
        }

        mAdapter.notifyDataSetChanged();
    }

    private Date getNextDate() {
        Calendar calendar = (Calendar) calendarReference.clone();
        calendar.add(Calendar.DATE, -mPhotos.size());

        return calendar.getTime();
    }

    private void initViewPager() {
        mAdapter = new PhotosPagerAdapter(this, mPhotos);
        mPhotosPager.setAdapter(mAdapter);
        mPhotosPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                if (position + 3 > mPhotos.size()) {
                    getPhoto(getNextDate());
                }
            }
        });
    }


}
