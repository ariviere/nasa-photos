package com.ar.tothestars.helpers;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by ariviere on 12/09/15.
 */
public class ComponentsHelper {

    private ComponentsHelper() {

    }

    /**
     * Retrieve the action bar height.
     *
     * @param context context used to retrieve the styled attributes.
     * @return action bar height in pixels.
     */
    public static int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        } else {
            return 0;
        }
    }

    /**
     * retrieve status bar height in px
     *
     * @param context context
     * @return status bar height in px
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
