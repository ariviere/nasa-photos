package com.ar.tothestars;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;
import android.widget.Toast;

import com.ar.tothestars.models.Photo;
import com.ar.tothestars.services.APODManager;
import com.ar.tothestars.services.APODParser;
import com.ar.tothestars.ui.PaletteTransformation;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uk.co.senab.photoview.PhotoViewAttacher;


public class MainActivity extends ActionBarActivity {

    private Photo mPhoto;

    private ImageView mPhotoView;
    private PhotoViewAttacher mPhotoViewAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPhotoView = (ImageView) findViewById(R.id.photo);

        getPhoto();
    }

    private void getPhoto() {
        APODManager.getClient()
                .getPhoto("2014-01-02", true, "DEMO_KEY", new Callback<String>() {
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

                        //get palette
                        Bitmap bitmap = ((BitmapDrawable) mPhotoView.getDrawable()).getBitmap(); // Ew!
                        Palette palette = PaletteTransformation.getPalette(bitmap);
                        int mutedDark = palette.getDarkMutedColor(0x000000);
                        mPhotoView.setBackgroundColor(mutedDark);
                    }
                });
    }

}
