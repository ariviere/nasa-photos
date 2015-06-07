package com.ar.tothestars.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ar.tothestars.R;
import com.ar.tothestars.models.Photo;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by ariviere on 07/06/15.
 */
public class APODPhotoView extends RelativeLayout {

    public static final String VIMEO = "player.vimeo.com";
    public static final String YOUTUBE = "www.youtube.com";
    private Photo mPhoto;

    private PhotoView mPhotoView;
    private TextView mErrorMessageView;

    public APODPhotoView(Context context) {
        super(context);

        if (!isInEditMode()) {
            init(context);
        }
    }

    public APODPhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            init(context);
        }
    }

    public APODPhotoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (!isInEditMode()) {
            init(context);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void setModel(Photo photo) {
        mPhoto = photo;

        if (!isPhotoValid()) {
            mErrorMessageView.setVisibility(View.VISIBLE);
        } else {
            Picasso.with(getContext())
                    .load(mPhoto.getUrl())
                    .fit()
                    .centerInside()
//                .transform(PaletteTransformation.instance())
                    .into(mPhotoView, new com.squareup.picasso.Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {
//                        //get palette
//                        Bitmap bitmap = ((BitmapDrawable) mPhotoView.getDrawable()).getBitmap(); // Ew!
//                        Palette palette = PaletteTransformation.getPalette(bitmap);
//                        int mutedDark = palette.getDarkMutedColor(0x000000);
//                        mPhotoView.setBackgroundColor(mutedDark);

                            Log.d("APODPhotoView", "Successfully loaded " + mPhoto.getUrl());
                        }

                        @Override
                        public void onError() {
                            Log.d("APODPhotoView", "Failed to load " + mPhoto.getUrl());
                            mErrorMessageView.setVisibility(View.VISIBLE);
                        }
                    });
        }
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_photo, this);

        mPhotoView = (PhotoView) findViewById(R.id.photo);
        mErrorMessageView = (TextView) findViewById(R.id.error_message);
    }

    private boolean isPhotoValid() {
        if (mPhoto.getUrl().contains(VIMEO)) {
            return false;
        } else if (mPhoto.getUrl().contains(YOUTUBE)) {
            return false;
        }

        return true;
    }
}
