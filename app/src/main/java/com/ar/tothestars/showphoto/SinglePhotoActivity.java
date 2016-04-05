package com.ar.tothestars.showphoto;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ar.tothestars.R;
import com.ar.tothestars.helpers.PhotoHelper;
import com.ar.tothestars.models.APODPhoto;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by ariviere on 08/09/15.
 */
public class SinglePhotoActivity extends AppCompatActivity {

    private APODPhoto photo;
    private PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_photo);

        photo = getIntent().getParcelableExtra(PhotoHelper.PHOTO);

        photoView = (PhotoView) findViewById(R.id.photo_src);

        Picasso.with(this)
                .load(photo.getUrl())
                .fit()
                .centerInside()
                .into(photoView, new com.squareup.picasso.Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                    }
                });

    }
}
