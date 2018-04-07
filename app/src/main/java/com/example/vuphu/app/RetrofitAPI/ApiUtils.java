package com.example.vuphu.app.RetrofitAPI;

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "http://10.0.2.2:3000";

    public static Api getAPIService() {

        return RetrofitClient.getInstance(BASE_URL).create(Api.class);
    }
}
