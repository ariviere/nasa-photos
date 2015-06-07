package com.ar.tothestars;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.ar.tothestars.models.Photo;
import com.ar.tothestars.services.APODManager;
import com.ar.tothestars.services.APODParser;
import com.ar.tothestars.ui.PaletteTransformation;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uk.co.senab.photoview.PhotoViewAttacher;


public class MainActivity extends ActionBarActivity {

    private final static String DATE_FORMAT = "yyyy-MM-dd";
    private static final String PHOTO = "photo";

    private Photo mPhoto;

    private ImageView mPhotoView;
    private PhotoViewAttacher mPhotoViewAttacher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPhotoView = (ImageView) findViewById(R.id.photo);

        if (savedInstanceState != null) {
            mPhoto = savedInstanceState.getParcelable(PHOTO);
        }

        if (mPhoto == null) {
            getPhoto();
        } else {
            showPhoto();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PHOTO, mPhoto);
    }

    private void getPhoto() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        String dateFormatted = sdf.format(calendar.getTime());

        APODManager.getClient()
                .getPhoto(dateFormatted, true, Credentials.NASA_KEY, new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        mPhoto = APODParser.getPhoto(s);
                        showPhoto();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(MainActivity.this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showPhoto() {
        Picasso.with(this)
                .load(mPhoto.getUrl())
                .fit()
                .centerInside()
                .transform(PaletteTransformation.instance())
                .into(mPhotoView, new com.squareup.picasso.Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {

                        //give gestures to the photo
                        if (mPhotoViewAttacher != null) {
                            mPhotoViewAttacher.update();
                        } else {
                            mPhotoViewAttacher = new PhotoViewAttacher(mPhotoView);
                        }

//                        //get palette
//                        Bitmap bitmap = ((BitmapDrawable) mPhotoView.getDrawable()).getBitmap(); // Ew!
//                        Palette palette = PaletteTransformation.getPalette(bitmap);
//                        int mutedDark = palette.getDarkMutedColor(0x000000);
//                        mPhotoView.setBackgroundColor(mutedDark);
                    }
                });
    }

}
