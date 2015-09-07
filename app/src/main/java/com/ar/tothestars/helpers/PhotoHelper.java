package com.ar.tothestars.helpers;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.ar.tothestars.R;
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

    /**
     * Use to share a photo
     * @param context current context
     * @param photoBitmap photo to share
     * @param photoTitle title of the photo to share
     */
    public static void sharePicture(Context context, Bitmap photoBitmap, String photoTitle) {
        // convert bitmap to Uri
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), photoBitmap, photoTitle, null);
        Uri photoUri = Uri.parse(path);

        // share image with Uri
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, photoUri);
        intent.setType("image/jpeg");
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_photo)));
    }
}