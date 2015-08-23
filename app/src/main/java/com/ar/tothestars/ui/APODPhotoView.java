package com.ar.tothestars.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ar.tothestars.R;
import com.ar.tothestars.models.APODPhoto;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by ariviere on 07/06/15.
 */
public class APODPhotoView extends FrameLayout implements View.OnClickListener, View.OnLongClickListener {

    private APODPhoto mPhoto;

    private TextView mPhotoTitle;
    private PhotoView mPhotoView;
    private TextView mErrorMessageView;

    private View mPlusFab;
    private View mSaveFab;
    private View mFullScreenFab;
    private View mShareFab;
    private View mDesktopFab;

    private float mTranslateFirstRange;
    private float mTranslateMidFirstRange;
    private float mTranslateMidSecondRange;

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photo_plus_fab:
                if (mSaveFab.getAlpha() == 0) {
                    showFabButtons();
                } else {
                    hideFabButtons();
                }
                break;
            case R.id.photo_save_fab:

                break;
            case R.id.photo_share_fab:

                break;
            case R.id.photo_fullscreen_fab:

                break;
            case R.id.photo_desktop_fab:

                break;
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.photo_save_fab:

                break;
            case R.id.photo_share_fab:

                break;
            case R.id.photo_fullscreen_fab:

                break;
            case R.id.photo_desktop_fab:

                break;
            default:
                break;
        }
        return false;
    }

    public void setModel(APODPhoto photo) {
        mPhoto = photo;

        mPhotoTitle.setText(photo.getTitle());

        Picasso.with(getContext())
                .load(mPhoto.getUrl())
                .fit()
                .centerCrop()
                .into(mPhotoView, new com.squareup.picasso.Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
//                        Log.d("APODPhotoView", "Successfully loaded " + mPhoto.getUrl());
                    }

                    @Override
                    public void onError() {
//                        Log.d("APODPhotoView", "Failed to load " + mPhoto.getUrl());
                        mErrorMessageView.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_photo, this);

        mPhotoTitle = (TextView) findViewById(R.id.photo_title);
        mPhotoView = (PhotoView) findViewById(R.id.photo_src);
        mErrorMessageView = (TextView) findViewById(R.id.error_message);

        mPlusFab = findViewById(R.id.photo_plus_fab);
        mSaveFab = findViewById(R.id.photo_save_fab);
        mFullScreenFab = findViewById(R.id.photo_fullscreen_fab);
        mDesktopFab = findViewById(R.id.photo_desktop_fab);
        mShareFab = findViewById(R.id.photo_share_fab);

        mPlusFab.setOnClickListener(this);
        mSaveFab.setOnClickListener(this);
        mFullScreenFab.setOnClickListener(this);
        mDesktopFab.setOnClickListener(this);
        mShareFab.setOnClickListener(this);

        mSaveFab.setOnLongClickListener(this);
        mFullScreenFab.setOnLongClickListener(this);
        mDesktopFab.setOnLongClickListener(this);
        mShareFab.setOnLongClickListener(this);

        mTranslateFirstRange = getResources().getDimension(R.dimen.fab_translate_first);
        mTranslateMidFirstRange = getResources().getDimension(R.dimen.fab_translate_mid_first);
        mTranslateMidSecondRange = getResources().getDimension(R.dimen.fab_translate_mid_second);
    }

    private void showFabButtons() {
        mPlusFab.animate().rotation(45);
        mSaveFab.animate().translationX(-mTranslateFirstRange).alpha(1);
        mShareFab.animate().translationY(-mTranslateFirstRange).alpha(1);
        mDesktopFab.animate()
                .translationX(-mTranslateMidFirstRange)
                .translationY(-mTranslateMidSecondRange)
                .alpha(1);
        mFullScreenFab.animate()
                .translationY(-mTranslateMidFirstRange)
                .translationX(-mTranslateMidSecondRange)
                .alpha(1);

    }

    private void hideFabButtons() {
        mPlusFab.animate().rotation(0);
        mSaveFab.animate().translationX(0).alpha(0);
        mShareFab.animate().translationY(0).alpha(0);
        mDesktopFab.animate()
                .translationX(0)
                .translationY(0)
                .alpha(0);
        mFullScreenFab.animate()
                .translationY(0)
                .translationX(0)
                .alpha(0);

    }
}
