package com.example.vuphu.app.admin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vuphu.app.AcsynHttp.NetworkConst;
import com.example.vuphu.app.R;
import com.example.vuphu.app.RetrofitAPI.ApiUtils;
import com.example.vuphu.app.admin.adapter.AdminOrdersAdapter;
import com.example.vuphu.app.object.order;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class AdminOrdersFragment extends Fragment {

    private ArrayList<order> list = new ArrayList<>();
    private RecyclerView list_order;
    private SharedPreferences pre;

    public AdminOrdersFragment() {
    }

    public static AdminOrdersFragment newInstance() {
        AdminOrdersFragment fragment = new AdminOrdersFragment();
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
        View v = inflater.inflate(R.layout.fragment_admin_orders, container, false);
        pre = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        list_order = v.findViewById(R.id.list_admin_orders);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        list_order.setLayoutManager(gridLayoutManager);
        list_order.setHasFixedSize(true);

        loadOrder();
        return v;
    }

    private void loadOrder() {
        ApiUtils.getAPIService().adminGetOrder("Bearer " + pre.getString(NetworkConst.token, ""))
                .enqueue(new Callback<List<order>>() {
                    @Override
                    public void onResponse(Call<List<order>> call, Response<List<order>> response) {

                        List<order> jArray = response.body();
                        if (response.isSuccessful()) {
                            if (jArray.size() == 0) {
                                Toast.makeText(getActivity(), "you dont have any order", Toast.LENGTH_LONG).show();

                            } else {
                                for (int i = 0; i < jArray.size(); i++) {
                                    list.add(jArray.get(i));
                                    Log.i("order", jArray.get(i).toString());
                                }
                            }
                        }
                        AdminOrdersAdapter.orderAdap adap = new AdminOrdersAdapter.orderAdap(list,
                                getContext(), pre.getString("token", ""));
                        list_order.setAdapter(adap);
                        adap.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<List<order>> call, Throwable t) {
                    }
                });
    }

}
