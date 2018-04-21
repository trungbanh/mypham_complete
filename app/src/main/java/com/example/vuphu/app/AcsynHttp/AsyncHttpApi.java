package com.example.vuphu.app.AcsynHttp;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class AsyncHttpApi {
    private static final String BASE_URL = NetworkConst.network;
    private static AsyncHttpClient client = new AsyncHttpClient();



    public static void get(String token,String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("Authorization","Bearer "+token);
        client.addHeader("Content-Type","application/x-www-form-urlencoded");
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String token,String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("Authorization","Bearer "+token);
        client.addHeader("Content-Type","application/x-www-form-urlencoded");
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post_signUp(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }
    public static void post_logIn(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }
    public static void put (String token, String url, RequestParams params, JsonHttpResponseHandler responseHandler){
        client.addHeader("Authorization","Bearer "+token);
        client.put(getAbsoluteUrl(url),params, responseHandler);
    }
    public static void delete(String token, String url, JsonHttpResponseHandler responseHandler){
        client.addHeader("Authorization","Bearer "+token);
        client.addHeader("Content-Type","application/x-www-form-urlencoded");
        client.delete(getAbsoluteUrl(url),responseHandler);
    }

    public static void post_admin_product(String token,String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("Authorization","Bearer "+token);
        client.addHeader("Content-Type","multipart/form-data; boundary=MultipartBoundry");
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}
