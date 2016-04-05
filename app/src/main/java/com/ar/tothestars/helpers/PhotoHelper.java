package com.ar.tothestars.helpers;

import android.Manifest;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.ar.tothestars.R;
import com.ar.tothestars.showphoto.SinglePhotoActivity;
import com.ar.tothestars.models.APODPhoto;

import java.io.IOException;
import java.net.URL;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by ariviere on 07/09/15.
 */
public class PhotoHelper {

    /**
     * photo for activity info transfert
     */
    public static final String PHOTO = "photo";
    public static final String PHOTOS_ARRAY = "photosArray6";

    private PhotoHelper() {
    }

    /**
     * Used to set a picture as wallpaper for the user
     *
     * @param context     context of the view
     * @param photo bitmap of the photo to be show as desktop
     */
    public static void setPictureAsWallpaper(final Context context, final APODPhoto photo) {
        // get base bitmap of the photo
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.desktop_picture))
                .setMessage(context.getString(R.string.desktop_picture_desc))
                .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Observable.just(photo.getHdurl())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        try {
                                            URL url = new URL(s);
                                            final Bitmap photoBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                                            if (photoBitmap != null) {
                                                setWallpaper(context, photoBitmap);
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    }
                })
                .setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    /**
     * Use to share a photo
     *
     * @param context     current context
     * @param photo       photo to share
     */
    public static void sharePicture(final Context context, final APODPhoto photo) {
        // get base bitmap of the photo
        Observable.just(photo.getUrl())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        try {
                            URL url = new URL(s);
                            final Bitmap photoBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                            if (photoBitmap != null) {
                                // convert bitmap to Uri
                                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions((Activity) context, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                                }

                                String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), photoBitmap, photo.getTitle(), null);
                                Uri photoUri = Uri.parse(path);

                                // share image with Uri
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_SEND);
                                intent.putExtra(Intent.EXTRA_STREAM, photoUri);
                                intent.setType("image/jpeg");
                                context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_photo)));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * Use to start full screen photo activity
     *
     * @param context context
     * @param photo   photo to fullscreen
     */
    public static void startFullScreen(Context context, APODPhoto photo) {
        Intent intent = new Intent(context, SinglePhotoActivity.class);
        intent.putExtra(PHOTO, photo);
//                ActivityOptionsCompat options = ActivityOptionsCompat
//                        .makeSceneTransitionAnimation((Activity)getContext(), mPhotoView, "robot");
//
        context.startActivity(intent);
    }

    public static String getSavedDates(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                "APODphotosPreferences", Context.MODE_PRIVATE);

        return sharedPref.getString(PHOTOS_ARRAY, "");
    }

    public static void setSavedDates(Context context, String savedDates) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                "APODphotosPreferences", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PHOTOS_ARRAY, savedDates);
        editor.apply();
    }

    private static void setWallpaper(final Context context, Bitmap bitmap) {
        Observable.just(bitmap)
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        try {
                            WallpaperManager.getInstance(context).setBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}