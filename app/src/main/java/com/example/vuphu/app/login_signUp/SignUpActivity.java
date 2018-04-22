package com.example.vuphu.app.login_signUp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vuphu.app.AcsynHttp.AsyncHttpApi;
import com.example.vuphu.app.Dialog.notyfi;
import com.example.vuphu.app.MainActivity;
import com.example.vuphu.app.object.SignUpToken;
import com.example.vuphu.app.R;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SignUpActivity extends AppCompatActivity {

    private EditText name ;
    private EditText email ;
    private EditText pass ;
    private Button signup ;

    private String nameText ;
    private String emailText ;
    private String passText ;

    private SignUpToken token ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
    }

    /*
    * admin@admin.com
    * admin
    * */

    private RequestParams getRequest () {
        RequestParams paramObject = new RequestParams();

        paramObject.put("name",nameText);
        paramObject.put("email",emailText);
        paramObject.put("password",passText);

        return paramObject ;
    }

    private void postResquest(RequestParams params) {
        AsyncHttpApi.post_signUp("/user/signup",params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String json = response.toString();
                Gson gson = new Gson();
                token = gson.fromJson(json,SignUpToken.class);
                if (token.getToken() != null) {
                    notyfi no = new notyfi((Activity) getBaseContext());
                    no.show();
                    Toast.makeText(SignUpActivity.this, "signup sucess", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.putExtra("mail",emailText);
                    intent.putExtra("pass",passText);

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    startActivity(intent);
                } else {
                    Toast.makeText(SignUpActivity.this, "signup fail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init () {
        name =  findViewById(R.id.edt_name);
        email = findViewById(R.id.edt_email);
        pass =  findViewById(R.id.edt_pass);
        signup = findViewById(R.id.btn_signup);
    }
    void getText() {
        nameText = name.getText().toString();
        emailText= email.getText().toString();
        passText = pass.getText().toString();
    }
    public void signUp(View view) {
        getText();
        postResquest(getRequest());
    }
}
