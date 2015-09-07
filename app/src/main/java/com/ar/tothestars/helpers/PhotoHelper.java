package com.ar.tothestars.helpers;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.IOException;

/**
 * Created by ariviere on 07/09/15.
 */
public class PhotoHelper {

    private PhotoHelper() {
    }

    /**
     * Used to set a picture as wallpaper for the user
     *
     * @param context     context of the view
     * @param photoBitmap bitmap of the photo to be show as desktop
     */
    public static void setPictureAsWallpaper(final Context context, final Bitmap photoBitmap) {
        if (photoBitmap != null) {
            new AlertDialog.Builder(context)
                    .setTitle("Desktop picture")
                    .setMessage("Use this photo as desktop picture ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                WallpaperManager.getInstance(context).setBitmap(photoBitmap);
                            } catch (IOException e) {
                                Log.e("desktop wallpaper", e.getMessage());
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();

        }
    }
}
