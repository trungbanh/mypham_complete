package com.example.vuphu.app.user.cart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.vuphu.app.R;
import com.example.vuphu.app.user.adapter.CartAdapter;

public class CartList extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list_product_in_cart);

        mRecyclerView.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new CartAdapter(Cart.getInstance().getListProduct());
        mRecyclerView.setAdapter(mAdapter);

    }
}
