package com.example.vuphu.app.user;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vuphu.app.AcsynHttp.AsyncHttpApi;
import com.example.vuphu.app.AcsynHttp.NetworkConst;
import com.example.vuphu.app.R;
import com.example.vuphu.app.object.listOrder;
import com.example.vuphu.app.object.AcountId;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


public class ProfileFragment extends Fragment {

    private SharedPreferences pre ;
    private TextView name ;
    private CircleImageView avt ;
    private AcountId u ;
    private TextView money ;
    private listOrder order;
    private EditText nameedt ;
    private EditText phone ;
    private EditText addrss ;
    private Button put ;

    public ProfileFragment() {

    }


    public static ProfileFragment newInstance(AcountId user) {
        ProfileFragment fragment = new ProfileFragment();
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
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        pre = getActivity().getSharedPreferences("data",MODE_PRIVATE);

        getToken(pre.getString(NetworkConst.token,""));
        init(v);
        UpdateList();

        return v;
    }
    private void init (View v ) {
        name = v.findViewById(R.id.tv_user_name);
        avt = v.findViewById(R.id.img_user) ;
        money = v.findViewById(R.id.tv_user_money);
        nameedt = v.findViewById(R.id.edt_user_name);
        phone = v.findViewById(R.id.edt_user_phoneNumber);
        addrss = v.findViewById(R.id.edt_user_address) ;
        put = v.findViewById(R.id.btn_user_edit_info);
    }

    private void UpdateList () {
        put.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(name.getText().toString())) {
                    name.setError("please fill you name before submit");
                    return;
                }
                if(TextUtils.isEmpty(phone.getText().toString())) {
                    phone.setError("please fill you phone number before submit");
                    return;
                }
                if(TextUtils.isEmpty(addrss.getText().toString())) {
                    addrss.setError("please fill you address before submit");
                    return;
                }
                upDateInfo(pre.getString("token",""));

            }
        });
    }
    private void upDateInfo (String token) {
        AsyncHttpApi.put(token,"/user",getPamrams(),new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                Log.i("error_put",response.toString());
                Gson gson = new Gson();
                String mes = gson.fromJson(response.toString(),String.class);
                if (mes.equals("User updated!")) {
                    Toast.makeText(getActivity(), "update complete!!!", Toast.LENGTH_SHORT).show();
                    getToken(pre.getString("token",""));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("error_put",throwable.getMessage());
            }
        });
    }

    private RequestParams getPamrams () {
        RequestParams params = new RequestParams();

        String pname = nameedt.getText().toString();
        String pphone = phone.getText().toString();
        String paddrs = addrss.getText().toString();

        params.put("address",paddrs);
        params.put("name",pname);
        params.put("phone",pphone);

        return params;
    }

    void getToken (String token) {
        AsyncHttpApi.get(token,"/account",null,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Gson gson = new Gson();
                order = gson.fromJson(response.toString(),listOrder.class);
                u = order.getAccountId();
                name.setText(u.getName());
                Picasso.get().load(NetworkConst.network+"/"+u.getAvatar().replace("\\","/")).error(R.drawable.ic_terrain_black_24dp).into(avt);
                money.setText(order.getBalanced().toString());
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("fail",errorResponse.toString());
            }
        });
    }

}
