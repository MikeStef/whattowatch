package com.micste.whattowatch.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

public class SnackBarHelper {

    public static void showSnackBarMessage(View view, String message) {
        Snackbar sb = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        sb.show();
    }
}
