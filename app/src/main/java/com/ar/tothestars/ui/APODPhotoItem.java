package com.ar.tothestars.ui;

import android.content.Context;
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

import java.util.Arrays;

/**
 * Created by ariviere on 07/06/15.
 */
public class APODPhotoItem extends FrameLayout implements View.OnClickListener {

    private APODPhoto photo;
    private View progress;
    private Listener listener;

    private TextView photoTitle;
    private ImageView photoView;
    private TextView errorMessageView;

    private int photoPosition;

    private View plusFab;
    private ImageView saveFab;
    private View fullScreenFab;
    private View shareFab;
    private View desktopFab;

    private float translateFirstRange;
    private float translateMidFirstRange;
    private float translateMidSecondRange;

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
                if (saveFab.getAlpha() == 0) {
                    if (PhotoHelper.getSavedDates(getContext()).contains(
                            String.valueOf(photo.getDate()))) {
                        saveFab.setImageResource(R.drawable.ic_no_favorite);
                    }

                    showFabButtons();
                } else {
                    hideFabButtons();
                }
                break;
            case R.id.photo_save_fab:
                savePhoto();
                break;
            case R.id.photo_share_fab:
                PhotoHelper.sharePicture(getContext(), photo);
                hideFabButtons();
                break;
            case R.id.photo_src:
            case R.id.photo_fullscreen_fab:
                PhotoHelper.startFullScreen(getContext(), photo);
                hideFabButtons();
                break;
            case R.id.photo_desktop_fab:
                PhotoHelper.setPictureAsWallpaper(getContext(), photo);
                hideFabButtons();
                break;
            default:
                break;
        }
    }

    public void setModel(APODPhoto photo, int position) {
        this.photo = photo;

        photoPosition = position;

        photoTitle.setText(photo.getTitle());

        // display photo with picasso
        Picasso.with(getContext())
                .load(this.photo.getUrl())
                .fit()
                .centerCrop()
                .into(photoView, new com.squareup.picasso.Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                        progress.animate().alpha(0);
                    }

                    @Override
                    public void onError() {
                        errorMessageView.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_photo, this);

        photoTitle = (TextView) findViewById(R.id.photo_title);
        photoView = (ImageView) findViewById(R.id.photo_src);
        errorMessageView = (TextView) findViewById(R.id.error_message);
        progress = findViewById(R.id.progress);

        plusFab = findViewById(R.id.photo_plus_fab);
        saveFab = (ImageView) findViewById(R.id.photo_save_fab);
        fullScreenFab = findViewById(R.id.photo_fullscreen_fab);
        desktopFab = findViewById(R.id.photo_desktop_fab);
        shareFab = findViewById(R.id.photo_share_fab);

        photoView.setOnClickListener(this);

        plusFab.setOnClickListener(this);
        saveFab.setOnClickListener(this);
        fullScreenFab.setOnClickListener(this);
        desktopFab.setOnClickListener(this);
        shareFab.setOnClickListener(this);

        translateFirstRange = getResources().getDimension(R.dimen.fab_translate_first);
        translateMidFirstRange = getResources().getDimension(R.dimen.fab_translate_mid_first);
        translateMidSecondRange = getResources().getDimension(R.dimen.fab_translate_mid_second);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private void showFabButtons() {
        plusFab.animate().rotation(45);
        saveFab.animate().translationX(-translateFirstRange).alpha(1);
        shareFab.animate().translationY(-translateFirstRange).alpha(1);
        desktopFab.animate()
                .translationX(-translateMidFirstRange)
                .translationY(-translateMidSecondRange)
                .alpha(1);
        fullScreenFab.animate()
                .translationY(-translateMidFirstRange)
                .translationX(-translateMidSecondRange)
                .alpha(1);

    }

    private void hideFabButtons() {
        plusFab.animate().rotation(0);
        saveFab.animate().translationX(0).alpha(0);
        shareFab.animate().translationY(0).alpha(0);
        desktopFab.animate()
                .translationX(0)
                .translationY(0)
                .alpha(0);
        fullScreenFab.animate()
                .translationY(0)
                .translationX(0)
                .alpha(0);
    }

    private void savePhoto() {
        String allDates = PhotoHelper.getSavedDates(getContext());
        String[] allDatesSplitted = allDates.split(";");

        if (!Arrays.asList(allDatesSplitted).contains(photo.getDate())) {
            if (!TextUtils.isEmpty(allDates)) {
                allDates += ";";
            }

            allDates += photo.getDate();

            PhotoHelper.setSavedDates(getContext(), allDates);
            saveFab.setImageResource(R.drawable.ic_no_favorite);
            Toast.makeText(getContext(), R.string.favorite_added, Toast.LENGTH_SHORT).show();
        } else {
            allDates = allDates.replace(";" + photo.getDate(), "");
            allDates = allDates.replace(String.valueOf(photo.getDate()), "");
            PhotoHelper.setSavedDates(getContext(), allDates);

            saveFab.setImageResource(R.drawable.ic_favorite);
            Toast.makeText(getContext(), R.string.favorite_removed, Toast.LENGTH_SHORT).show();
        }

        listener.onSaveButtonClicked(photoPosition);
        hideFabButtons();
    }

    public interface Listener {
        /**
         * Triggered when the button saved is clicked
         */
        void onSaveButtonClicked(int position);
    }
}
