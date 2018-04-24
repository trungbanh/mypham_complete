package com.example.vuphu.app.user.UserProfileTab;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vuphu.app.AcsynHttp.AsyncHttpApi;
import com.example.vuphu.app.AcsynHttp.NetworkConst;
import com.example.vuphu.app.R;
import com.example.vuphu.app.object.HistoryDeposit;
import com.example.vuphu.app.object.HistoryOrder;
import com.example.vuphu.app.object.listOrder;
import com.example.vuphu.app.object.order;
import com.example.vuphu.app.user.adapter.UserOrdersAdapter;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class OrderHistoryFragment extends Fragment {

    private List<HistoryOrder> list ;
    private RecyclerView list_order;
    LinearLayoutManager gridLayoutManager ;
    private UserOrdersAdapter.orderAdap adap ;

    public OrderHistoryFragment() {
        // Required empty public constructor
    }

    listOrder order;


    public static OrderHistoryFragment newInstance() {
        OrderHistoryFragment fragment = new OrderHistoryFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_order_history, container, false);

        list_order = v.findViewById(R.id.list_user_orders);
        gridLayoutManager  = new LinearLayoutManager(getContext());
        SharedPreferences pre = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        getToken(pre.getString("token",""));
        return v;
    }

    void getToken (String token) {
        AsyncHttpApi.get(token,"/account",null,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Log.i("onsucesshis",response.toString());
                Gson gson = new Gson();
                order = gson.fromJson(response.toString(),listOrder.class);
                if (order.getHistoryOrder().size() == 0){
                } else {
                    list = order.getHistoryOrder();
                    Log.i("listHis",list.get(0).getId());
                    list_order.setLayoutManager(gridLayoutManager);
                    list_order.setHasFixedSize(true);
                    list_order.setNestedScrollingEnabled(true);
                    adap = new UserOrdersAdapter.orderAdap(list, getContext());
                    list_order.setAdapter(adap);
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("fail",errorResponse.toString());
            }
        });
    }
}
