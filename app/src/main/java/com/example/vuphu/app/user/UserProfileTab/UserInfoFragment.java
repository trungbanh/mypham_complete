package com.example.vuphu.app.user.UserProfileTab;

import android.content.Context;
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
import android.widget.Toast;

import com.example.vuphu.app.AcsynHttp.AsyncHttpApi;
import com.example.vuphu.app.Dialog.notyfi;
import com.example.vuphu.app.R;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;


public class UserInfoFragment extends Fragment {

    SharedPreferences pre ;
    private EditText name ;
    private EditText phone ;
    private EditText addrss ;

    private Button put ;

    public UserInfoFragment() {
        // Required empty public constructor
    }

    public static UserInfoFragment newInstance() {
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init (View v ) {
        name = v.findViewById(R.id.edt_user_name);
        phone = v.findViewById(R.id.edt_user_phoneNumber);
        addrss = v.findViewById(R.id.edt_user_address) ;
        put = v.findViewById(R.id.btn_user_edit_info);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_info, container, false);
        pre = getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
        init(v);
        UpdateList();
        return v ;
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

                Gson gson = new Gson();
                String mes = gson.fromJson(response.toString(),String.class);
                if (mes.equals("User updated!")) {
                    notyfi no =new notyfi(getActivity());
                    no.setText("Update info complete !");
                    no.show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                notyfi no =new notyfi(getActivity());
                no.setText("update fail !");
                no.show();
            }
        });
    }

    private RequestParams getPamrams () {
        RequestParams params = new RequestParams();

        String pname = name.getText().toString();
        String pphone = phone.getText().toString();
        String paddrs = addrss.getText().toString();

        params.put("address",paddrs);
        params.put("name",pname);
        params.put("phone",pphone);


        return params;
    }


}
