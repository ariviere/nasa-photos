package com.ar.tothestars.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ar.tothestars.R;
import com.ar.tothestars.helpers.PhotoHelper;
import com.ar.tothestars.models.APODPhoto;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by ariviere on 07/06/15.
 */
public class APODPhotoItem extends FrameLayout implements View.OnClickListener {

    private APODPhoto mPhoto;
    private Bitmap mPhotoBitmap;
    private View mProgress;
    private Listener mListener;

    private TextView mPhotoTitle;
    private ImageView mPhotoView;
    private TextView mErrorMessageView;

    private View mPlusFab;
    private ImageView mSaveFab;
    private View mFullScreenFab;
    private View mShareFab;
    private View mDesktopFab;

    private float mTranslateFirstRange;
    private float mTranslateMidFirstRange;
    private float mTranslateMidSecondRange;

    public APODPhotoItem(Context context) {
        super(context);

        if (!isInEditMode()) {
            init(context);
        }
    }

    public APODPhotoItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            init(context);
        }
    }

    public APODPhotoItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (!isInEditMode()) {
            init(context);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photo_plus_fab:
                if (mSaveFab.getAlpha() == 0) {
                    if (PhotoHelper.getSavedDates(getContext()).contains(
                            String.valueOf(mPhoto.getCalendarDate().getTimeInMillis()))) {
                        mSaveFab.setImageResource(R.drawable.ic_no_favorite);
                    }

                    showFabButtons();
                } else {
                    hideFabButtons();
                }
                break;
            case R.id.photo_save_fab:
                Calendar dateCal = mPhoto.getCalendarDate();

                String allDates = PhotoHelper.getSavedDates(getContext());
                String[] allDatesSplitted = allDates.split(";");

                if (!Arrays.asList(allDatesSplitted).contains(String.valueOf(dateCal.getTimeInMillis()))) {
                    if (!TextUtils.isEmpty(allDates)) {
                        allDates += ";";
                    }

                    allDates += dateCal.getTimeInMillis();
                    PhotoHelper.setSavedDates(getContext(), allDates);
                    mSaveFab.setImageResource(R.drawable.ic_no_favorite);
                    Toast.makeText(getContext(), R.string.favorite_added, Toast.LENGTH_SHORT).show();
                } else {
                    allDates = allDates.replace(";" + dateCal.getTimeInMillis(), "");
                    allDates = allDates.replace(String.valueOf(dateCal.getTimeInMillis()), "");
                    PhotoHelper.setSavedDates(getContext(), allDates);

                    mSaveFab.setImageResource(R.drawable.ic_favorite);
                    Toast.makeText(getContext(), R.string.favorite_removed, Toast.LENGTH_SHORT).show();
                }

                mListener.onSaveButtonClicked();

                break;
            case R.id.photo_share_fab:
                PhotoHelper.sharePicture(getContext(), mPhotoBitmap, mPhoto.getTitle());
                break;
            case R.id.photo_src:
            case R.id.photo_fullscreen_fab:
                PhotoHelper.startFullScreen(getContext(), mPhoto);
                break;
            case R.id.photo_desktop_fab:
                PhotoHelper.setPictureAsWallpaper(getContext(), mPhotoBitmap);
                break;
            default:
                break;
        }
    }

    public void setModel(APODPhoto photo) {
        mPhoto = photo;

        mPhotoTitle.setText(photo.getTitle());

        // display photo with picasso
        Picasso.with(getContext())
                .load(mPhoto.getUrl())
                .fit()
                .centerCrop()
                .into(mPhotoView, new com.squareup.picasso.Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                        mProgress.animate().alpha(0);
                    }

                    @Override
                    public void onError() {
                        mErrorMessageView.setVisibility(View.VISIBLE);
                    }
                });

        // get base bitmap of the photo
        Observable.just(mPhoto.getUrl())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        try {
                            URL url = new URL(s);
                            mPhotoBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_photo, this);

        mPhotoTitle = (TextView) findViewById(R.id.photo_title);
        mPhotoView = (ImageView) findViewById(R.id.photo_src);
        mErrorMessageView = (TextView) findViewById(R.id.error_message);
        mProgress = findViewById(R.id.progress);

        mPlusFab = findViewById(R.id.photo_plus_fab);
        mSaveFab = (ImageView) findViewById(R.id.photo_save_fab);
        mFullScreenFab = findViewById(R.id.photo_fullscreen_fab);
        mDesktopFab = findViewById(R.id.photo_desktop_fab);
        mShareFab = findViewById(R.id.photo_share_fab);

        mPhotoView.setOnClickListener(this);

        mPlusFab.setOnClickListener(this);
        mSaveFab.setOnClickListener(this);
        mFullScreenFab.setOnClickListener(this);
        mDesktopFab.setOnClickListener(this);
        mShareFab.setOnClickListener(this);

        mTranslateFirstRange = getResources().getDimension(R.dimen.fab_translate_first);
        mTranslateMidFirstRange = getResources().getDimension(R.dimen.fab_translate_mid_first);
        mTranslateMidSecondRange = getResources().getDimension(R.dimen.fab_translate_mid_second);
    }

    public void setListener(Listener listener) {
        mListener = listener;
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

    public interface Listener {
        /**
         * Triggered when the button saved is clicked
         */
        void onSaveButtonClicked();
    }
}
