package com.example.vuphu.app.user.cart;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.vuphu.app.AcsynHttp.AsyncHttpApi;
import com.example.vuphu.app.AcsynHttp.NetworkConst;
import com.example.vuphu.app.R;
import com.example.vuphu.app.object.Payment;
import com.example.vuphu.app.object.ProductInCart;
import com.example.vuphu.app.user.adapter.CartAdapter;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CartList extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button buy ;

    private SharedPreferences pre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        init();
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CartAdapter(Cart.getInstance().getListProduct());
        mRecyclerView.setAdapter(mAdapter);
        buyClick();

    }

    private void init () {
        pre =getSharedPreferences("data", MODE_PRIVATE);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list_product_in_cart);
        buy = findViewById(R.id.btn_buy);
    }
    private void buyClick () {
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postPostOrder(Cart.getInstance().getPostJson());
            }
        });
    }
    private void postPostOrder (List<ProductInCart> listBuy) {
        RequestParams params = new RequestParams();
        params.put("products",listBuy);
        AsyncHttpApi.post(pre.getString(NetworkConst.token,""),"/orders",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("post product",response.toString());
                Gson gson = new Gson();
                Payment pay = gson.fromJson(response.toString(),Payment.class);
                payment(pay.getCreatedOrder().getId());
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.i("post product",errorResponse.toString());
            }
        });
    }
    private void payment (String idOrder) {
        RequestParams params = new RequestParams();
        params.put("_id", idOrder);
        AsyncHttpApi.post(pre.getString(NetworkConst.token,""),"/payment",params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

            }
        });
    }
}
