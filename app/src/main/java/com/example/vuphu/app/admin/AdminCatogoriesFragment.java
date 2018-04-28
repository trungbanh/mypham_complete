package com.example.vuphu.app.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vuphu.app.AcsynHttp.AsyncHttpApi;
import com.example.vuphu.app.AcsynHttp.NetworkConst;
import com.example.vuphu.app.ItemOffsetDecoration;
import com.example.vuphu.app.R;
import com.example.vuphu.app.admin.adapter.AdminProductApdater;
import com.example.vuphu.app.object.Product;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.MODE_PRIVATE;


public class AdminCatogoriesFragment extends Fragment {

    private FloatingActionButton fab;
    private RecyclerView list_product;
    private GridLayoutManager gridLayoutManager;
    private ItemOffsetDecoration itemDecoration;

    private ArrayList<Product> product;
    private SharedPreferences pre;

    private AdminProductApdater.productAdap adap;

    public AdminCatogoriesFragment() {
    }

    public static AdminCatogoriesFragment newInstance() {
        AdminCatogoriesFragment fragment = new AdminCatogoriesFragment();
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
        View v = inflater.inflate(R.layout.fragment_admin_catogories, container, false);
        product = new ArrayList<>();

        pre = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        fab = v.findViewById(R.id.fab_add);
        AddProduct();
        //loafProduct();
        init(v);

        return v;
    }

    private void init(View v) {
        list_product = v.findViewById(R.id.list_admin_product);
        list_product.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        list_product.setLayoutManager(gridLayoutManager);
        list_product.setNestedScrollingEnabled(false);
        itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        list_product.addItemDecoration(itemDecoration);
    }

    private void AddProduct() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AdminAddProductActivity.class));
            }
        });
    }

    public void loafProduct() {

        product.clear();
        AsyncHttpApi.get(pre.getString(NetworkConst.token, ""), "/products/", null,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        Gson gson = new Gson();
                        JSONArray jArray = response;
                        //Log.i("productID",response.toString());
                        if (jArray != null) {
                            for (int i = 0; i < jArray.length(); i++) {
                                try {
                                    product.add(gson.fromJson(jArray.get(i).toString(), Product.class));
                                    Log.i("productID", product.get(i).getId());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        adap = new AdminProductApdater.productAdap(product,
                                getContext(), pre.getString(NetworkConst.token, ""));
                        list_product.setAdapter(adap);
                        adap.notifyDataSetChanged();
                    }
                });
    }


    @Override
    public void onResume() {
        super.onResume();
        loafProduct();
    }
}
