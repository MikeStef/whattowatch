package com.micste.whattowatch.utils;

import com.micste.whattowatch.BuildConfig;
import com.micste.whattowatch.netcom.ApiService;
import com.micste.whattowatch.netcom.RetrofitClient;

public class ApiHelper {

    public static final String BASE_URL = BuildConfig.API_BASE_URL;

    public static ApiService getApiService() {
        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }
}
