package com.example.vuphu.app.RetrofitAPI;

import com.example.vuphu.app.object.Payment;
import com.example.vuphu.app.object.ProductCallback;
import com.example.vuphu.app.object.order;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    @Multipart
    @POST("/products")
    @Headers("Content-Type: application/json")
    Call<Void> upLoadProduct (@Header("Authorization") String authorization,
                              @Part MultipartBody.Part body,
                              @Query("name") RequestBody name,
                              @Query("price") RequestBody price,
                              @Part("quatity") RequestBody quantity,
                              @Query("description") RequestBody des,
                              @Query("type") RequestBody type
                              );


    @GET("/orders")
    @Headers("Content-Type: application/json")
    Call<List<order>> adminGetOrder (
            @Header("Authorization") String authorization
    );
    @POST("/orders")
    @Headers("Content-Type: application/json")
    Call<Payment> Order (
            @Header("Authorization") String authorization,
            @Body String list
    );

}