package com.example.vuphu.app.user.cart;

import android.content.Context;
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
import com.example.vuphu.app.Dialog.notyfi;
import com.example.vuphu.app.R;
import com.example.vuphu.app.user.adapter.CartAdapter;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

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
        pre = getSharedPreferences("data", MODE_PRIVATE);
        Log.i("token",pre.getString("token",""));
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list_product_in_cart);
        buy = findViewById(R.id.btn_buy);
    }
    private void buyClick () {
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postPostOrder(Cart.getInstance().getPostJson())){
                    Cart.getInstance().resetCart();
                    notyfi no = new notyfi(CartList.this);
                    no.show();
                }else  {
                    notyfi no = new notyfi(CartList.this);
                    no.setText("buy fail !!!");
                    no.show();
                }
            }
        });
    }
    private boolean postPostOrder (String list) {
        final boolean[] temp = new boolean[1];
        RequestParams params = new RequestParams();

        try {
            JSONObject object = new JSONObject(list);
            params.put("products", object);
            Log.i("list pa",params.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AsyncHttpApi.post(pre.getString("token",""),"/orders",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                temp[0] = true;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.i("list",errorResponse.toString());
            }
        });

        return temp[0];
    }
    private void payment (String idOrder) {
        RequestParams params = new RequestParams();
        params.put("_id", idOrder);
        AsyncHttpApi.post(pre.getString(NetworkConst.token,""),"/payment",
                params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

            }
        });
    }
}
