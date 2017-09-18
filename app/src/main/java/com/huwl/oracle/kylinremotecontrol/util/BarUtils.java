package com.huwl.oracle.kylinremotecontrol.util;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by aierxuan on 2017/9/17.
 */

public class BarUtils {
    public static final int STATUS=0,NAVIGATION=1,ALL=2;

    public static void setWindowBarColor(Activity activity, int colorResId,int forWhat) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                if(forWhat==STATUS || forWhat==ALL){
                    window.setStatusBarColor(ContextCompat.getColor(activity,colorResId));
                }
                if(forWhat==NAVIGATION || forWhat==ALL){
                    Log.e("test","change navigation   is running");
                    window.setNavigationBarColor(ContextCompat.getColor(activity,colorResId));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setWindowBarColor(Dialog dialog, int colorResId,int forWhat) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = dialog.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                if(forWhat==STATUS || forWhat==ALL){
                    window.setStatusBarColor(dialog.getContext().getResources().getColor(colorResId));
                }else if(forWhat==NAVIGATION || forWhat==ALL){
                    window.setNavigationBarColor(dialog.getContext().getResources().getColor(colorResId));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
