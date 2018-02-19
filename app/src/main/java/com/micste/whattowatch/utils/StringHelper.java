package com.micste.whattowatch.utils;

import android.content.Context;
import android.text.TextUtils;

import com.micste.whattowatch.R;
import com.micste.whattowatch.model.Genre;
import com.micste.whattowatch.model.SpokenLanguage;

import java.util.List;

public class StringHelper {

    public static String parseRuntime(int runtime, Context context) {
        return String.valueOf(runtime) + context.getString(R.string.minutes_short);
    }

    public static String appendLanguages(List<SpokenLanguage> list) {
        StringBuilder sb = new StringBuilder();

        if (list == null) {
            return "";
        }
        else if (list.size() > 1) {
            for (SpokenLanguage spokenLanguage : list) {
                sb.append(spokenLanguage.getName()).append(", ");
            }
            return sb.toString();
        } else {
            return list.get(0).getName();
        }
    }

    public static String appendRating(String rating) {
        StringBuilder sb = new StringBuilder();

        sb.append(rating).append(" / ").append("10");

        return sb.toString();
    }
}
