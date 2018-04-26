package com.example.vuphu.app.admin;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vuphu.app.AcsynHttp.AsyncHttpApi;
import com.example.vuphu.app.ItemOffsetDecoration;
import com.example.vuphu.app.R;
import com.example.vuphu.app.admin.adapter.AdminUserApdater;
import com.example.vuphu.app.object.AcountId;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.MODE_PRIVATE;


public class AdminUserFragment extends Fragment {

    private GridLayoutManager gridLayoutManager;
    private ItemOffsetDecoration itemDecoration;

    private ArrayList<AcountId> list;
    private RecyclerView list_users;
    private SharedPreferences pre;

    public AdminUserFragment() {
        // Required empty public constructor
    }


    public static AdminUserFragment newInstance() {
        AdminUserFragment fragment = new AdminUserFragment();
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
        View v = inflater.inflate(R.layout.fragment_admin_user, container, false);
        pre = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        init(v);
        loadUser();
        return v;
    }

    private void init(View v) {
        list_users = v.findViewById(R.id.list_admin_user);
        list_users.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        list_users.setLayoutManager(gridLayoutManager);
        itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        list_users.addItemDecoration(itemDecoration);
        list = new ArrayList<>();
    }

    private void loadUser() {
        AsyncHttpApi.get(pre.getString("token", ""), "/user", null,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                        Gson gson = new Gson();
                        JSONArray jArray = response;
                        if (jArray != null) {
                            for (int i = 0; i < jArray.length(); i++) {
                                try {
                                    list.add(gson.fromJson(jArray.get(i).toString(), AcountId.class));
                                    Log.i("product", jArray.get(i).toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        AdminUserApdater.userAdap adap = new AdminUserApdater.userAdap(list, getContext());
                        list_users.setAdapter(adap);
                        adap.notifyDataSetChanged();
                    }
                });
    }
}
