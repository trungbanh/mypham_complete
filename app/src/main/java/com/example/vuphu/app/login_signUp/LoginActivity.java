package com.example.vuphu.app.login_signUp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.vuphu.app.AcsynHttp.AsyncHttpApi;
import com.example.vuphu.app.AcsynHttp.NetworkConst;
import com.example.vuphu.app.MainActivity;
import com.example.vuphu.app.object.SignUpToken;
import com.example.vuphu.app.R;
import com.example.vuphu.app.object.TokenId;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    private Button login ;
    private ProgressDialog progressBar;
    private EditText emailInput ;
    private EditText passInput ;
    private String mail ;
    private String pass ;
    private SignUpToken token ;

    private SharedPreferences pre;
    private SharedPreferences.Editor edit;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pre =getSharedPreferences("data", MODE_PRIVATE);
        edit=pre.edit();

        intent = getIntent() ;
        init();
        Pre_process ();

        if (!pre.getString(NetworkConst.token,"").isEmpty() &&
                !pre.getString("type_user","").isEmpty()){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        SiginClick ();
    }
    private void SiginClick (){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(emailInput.getText().toString(),passInput.getText().toString());
            }
        });
    }

    private void init() {

        emailInput = findViewById(R.id.edt_email);
        passInput = findViewById(R.id.edt_pass);
        login = findViewById(R.id.btn_signin);
        progressBar = new ProgressDialog(this);
    }
    private void Pre_process (){
        progressBar.setMessage("Đang xử lí...");
        mail = intent.getStringExtra("email");
        pass = intent.getStringExtra("pass");
        emailInput.setText(mail);
        if(TextUtils.isEmpty(emailInput.getText().toString())) {
            emailInput.setError("cant be empty");
        }
        if (emailInput.getText().toString().compareTo("@gmail.com")!= 0){
            emailInput.setError("input must be mail");
        }
        passInput.setText(pass);
        if(TextUtils.isEmpty(passInput.getText().toString())) {
            passInput.setError("cant be empty");
        }
    }
    public void signIn (String mail,String pass){

        RequestParams params = new RequestParams();
        params.put("email",mail);
        params.put("password",pass);
        if (!postSignIn(params).isEmpty()) {
            if (mail.equals("admin@admin.com")) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                edit.putString("type_user", "admin");
                edit.commit();
                progressBar.hide();
                finish();
            } else {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                edit.putString("type_user", "user");
                edit.commit();
                progressBar.hide();
                finish();
            }
        } else {
            progressBar.hide();
            Toast.makeText(this, "incorrect email or password", Toast.LENGTH_SHORT).show();
        }
    }
    public void signUp(View view) {
        startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
    }
    private String postSignIn(RequestParams params) {
        AsyncHttpApi.post_logIn("/user/login",params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                String json = response.toString();
                Gson gson = new Gson();
                token = gson.fromJson(json,SignUpToken.class);
                edit.putString("token",token.getToken());
                edit.putString("message",token.getMessage());
                try {
                    String decode = JWTUtils.decoded(token.getToken());
                    edit.putString("userId",gson.fromJson(decode,TokenId.class).getUserId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                edit.commit();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("error",throwable.getMessage());
            }
        });
        String token = pre.getString(NetworkConst.token,"");
        return token;
    }
}
