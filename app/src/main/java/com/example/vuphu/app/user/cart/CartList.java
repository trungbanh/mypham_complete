package com.example.vuphu.app.user.cart;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.vuphu.app.AcsynHttp.AsyncHttpApi;
import com.example.vuphu.app.AcsynHttp.NetworkConst;
import com.example.vuphu.app.Dialog.notyfi;
import com.example.vuphu.app.R;
import com.example.vuphu.app.RetrofitAPI.ApiUtils;
import com.example.vuphu.app.object.Payment;
import com.example.vuphu.app.object.ProductInCart;
import com.example.vuphu.app.object.listOrder;
import com.example.vuphu.app.user.adapter.CartAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static okhttp3.MediaType.parse;


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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarcart);
        setSupportActionBar(toolbar);
        toolbar.setTitle("List product in cart");

        init();
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CartAdapter(Cart.getInstance().getListProduct());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
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
                int  checksum =0 ;
                int money = pre.getInt("money",0);
                checksum = money- Cart.getInstance().getSummary();

                if (checksum<0) {
                    notyfi no = new notyfi(CartList.this);
                    no.setText("no enoght money !!!");
                    no.show();
                } else {
                    postPostOrder();
                }
            }
        });
    }
    private void postPostOrder () {

        Log.i("json",Cart.getInstance().getPostJson());

        try {
            //JSONObject jsnobject = new JSONObject(Cart.getInstance().getPostJson());

            if (Cart.getInstance().getPostJson().equals("[]")){
                notyfi no = new notyfi(CartList.this);
                no.setText("null cart");
                no.show();
                return;
            }

            RequestParams params = new RequestParams();
            params.put("products",Cart.getInstance().getPostJson());


            AsyncHttpApi.post(pre.getString("token", ""), "/orders", params, new JsonHttpResponseHandler() {

               @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Gson gson = new Gson();
                    Payment payment = gson.fromJson(response.toString(), Payment.class);
                    Log.i("payment",payment.getCreatedOrder().getId());

                    payment (payment.getCreatedOrder().getId());

                        Cart.getInstance().resetCart();
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(mAdapter);
                    notyfi no = new notyfi(CartList.this);
                    no.show();
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.i("list", errorResponse.toString());
                    notyfi no = new notyfi(CartList.this);
                    no.setText("buy fail !!!");
                    no.show();
                }
            });


        } catch (Throwable tx) {
            Log.e("My App", "Could not parse malformed JSON: \"" + Cart.getInstance().getPostJson() + "\"");
        }

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
