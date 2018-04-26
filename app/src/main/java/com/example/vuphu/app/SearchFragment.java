package com.example.vuphu.app;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vuphu.app.AcsynHttp.NetworkConst;
import com.example.vuphu.app.admin.adapter.AdminProductApdater;
import com.example.vuphu.app.object.Product;
import com.example.vuphu.app.user.adapter.ProductApdater;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class SearchFragment extends Fragment {


    private RecyclerView list_product;
    private ArrayList<Product> product;
    private JSONArray jsonArray;
    private SharedPreferences pre;

    @SuppressLint("ValidFragment")
    public SearchFragment(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public SearchFragment() {
    }

    public static SearchFragment newInstance(JSONArray jsonArray) {
        SearchFragment fragment = new SearchFragment(jsonArray);
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
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        list_product = v.findViewById(R.id.list_search_product);
        list_product.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        list_product.setLayoutManager(gridLayoutManager);
        product = new ArrayList<>();
        pre = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        loafProduct();

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        list_product.addItemDecoration(itemDecoration);

        return v;
    }

    public void loafProduct() {

        Gson gson = new Gson();
        JSONArray jArray = jsonArray;
        if (jArray != null) {
            for (int i = 0; i < jArray.length(); i++) {
                try {
                    product.add(gson.fromJson(jArray.get(i).toString(), Product.class));
                    Log.i("product", jArray.get(i).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        String user = pre.getString("type_user", "");
        if (user.equals("user")) {
            ProductApdater.productAdap adap = new ProductApdater.productAdap(product, getContext());
            list_product.setAdapter(adap);
        } else {
            AdminProductApdater.productAdap adap = new AdminProductApdater.productAdap(product, getContext(), pre.getString(NetworkConst.token, ""));
            list_product.setAdapter(adap);
        }
    }
}
