package com.app.t24.utils;

import android.app.Activity;
import android.text.Html;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by alican on 15.04.2017.
 */

public class AndroidUtilities {

    // decoding html tags from string
    public static String decodeString(String param){
        return Html.fromHtml(param).toString();
    }

    // show progress dialog
    public static MaterialDialog showProgressDialog(Activity activity, int title){

        return new MaterialDialog.Builder(activity)
                .progress(true, 0)
                .content(activity.getResources().getString(title))
                .cancelable(false)
                .build();

    }

    // show content dialog
    public static void showDialog(Activity activity, int title){
        new MaterialDialog.Builder(activity)
                .content(title)
                .show();
    }

}
