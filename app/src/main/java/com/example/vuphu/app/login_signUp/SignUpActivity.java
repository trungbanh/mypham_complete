package com.example.vuphu.app.login_signUp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vuphu.app.AcsynHttp.AsyncHttpApi;
import com.example.vuphu.app.Dialog.notyfi;
import com.example.vuphu.app.MainActivity;
import com.example.vuphu.app.R;
import com.example.vuphu.app.object.SignUpToken;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class SignUpActivity extends AppCompatActivity {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private EditText name;
    private EditText email;
    private EditText pass;
    private Button signup;

    private String nameText;
    private String emailText;
    private String passText;

    private SignUpToken token;

    public static boolean checkEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    /*
     * admin@admin.com
     * admin
     * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
    }

    private RequestParams getRequest() {
        RequestParams paramObject = new RequestParams();

        paramObject.put("name", nameText);
        paramObject.put("email", emailText);
        paramObject.put("password", passText);

        return paramObject;
    }

    private boolean postResquest(RequestParams params) {
        final boolean[] result = new boolean[1];
        AsyncHttpApi.post_signUp("/user/signup", params, new JsonHttpResponseHandler() {

            notyfi no = new notyfi(SignUpActivity.this);

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String json = response.toString();
                Gson gson = new Gson();
                token = gson.fromJson(json, SignUpToken.class);
                result[0] = true;
                Log.i("sigup", result[0] + "");

                no.show();
                Toast.makeText(SignUpActivity.this, "signup sucess", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("mail", emailText);
                intent.putExtra("pass", passText);

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                startActivity(intent);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                result[0] = false;
                Log.i("sigup", result[0] + "");
                no.setText("signup fail !!!");
                no.setIcon(R.drawable.ic_delete);
                no.show();
            }
        });
        Log.i("sigup sum", result[0] + "");
        return result[0];
    }

    private void init() {
        name = findViewById(R.id.edt_name);
        email = findViewById(R.id.edt_email);
        pass = findViewById(R.id.edt_pass);
        signup = findViewById(R.id.btn_signup);
    }

    public void signUp(View view) {
        nameText = name.getText().toString();
        emailText = email.getText().toString();
        passText = pass.getText().toString();

        if (TextUtils.equals(nameText, "")) {
            Toast.makeText(this, "Nickname is empty", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.equals(emailText, "")) {
            Toast.makeText(this, "Email is empty", Toast.LENGTH_SHORT).show();
        } else if (!checkEmail(emailText)) {
            Toast.makeText(this, "Email is incorrect", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.equals(passText, "")) {
            Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show();
        } else {
            postResquest(getRequest());
        }
    }
}
g