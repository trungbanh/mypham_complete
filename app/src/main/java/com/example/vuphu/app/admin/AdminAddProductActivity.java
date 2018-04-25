package com.example.vuphu.app.admin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vuphu.app.AcsynHttp.NetworkConst;
import com.example.vuphu.app.R;
import com.example.vuphu.app.RetrofitAPI.ApiUtils;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.net.URISyntaxException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAddProductActivity extends AppCompatActivity {

    private EditText edt_name_product, edt_price, edt_desc,edt_quantity;
    private TextView tvtype;
    private Spinner edt_type;
    private ImageView img_product;
    private FloatingActionButton btn_add_img;
    private Button btn_add;
    public static final int PICK_IMAGE = 100;
    String mediaPath;

    private String arr [] = {
            "lotion",
            "hair care",
            "skin care cosmetics",
            "perfume",
            "lipstick"};

    private SharedPreferences pre;
    protected   Uri uri;
    private ProgressDialog progressBar;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product);

        Toolbar toolbar = (Toolbar) findViewById(R.id.admin_add_product_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        setTitle("Add product");
        progressBar = new ProgressDialog(this);
        progressBar.setMessage("Đang xử lí...");
        pre =getSharedPreferences("data", MODE_PRIVATE);
        init();
        setDataType();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        edt_name_product = findViewById(R.id.edt_admin_add_name_product);
        edt_price = findViewById(R.id.edt_admin_add_product_price);
        edt_desc = findViewById(R.id.edt_admin_add_product_content);
        edt_quantity = findViewById(R.id.edt_admin_add_quantity_product);
        btn_add_img =findViewById(R.id.btn_admin_add_image);
        btn_add = findViewById(R.id.btn_admin_add_product);
        edt_type = findViewById(R.id.spinner_add_type_product);
        img_product = findViewById(R.id.img_admin_add_product);
        tvtype = findViewById(R.id.tv_type);
    }
    private void setDataType() {

        if(TextUtils.isEmpty(edt_name_product.getText().toString())) {
            edt_name_product.setError("cant be empty");
        }
        if(TextUtils.isEmpty(edt_price.getText().toString())) {
            edt_price.setError("cant be empty");
        }
        if(TextUtils.isEmpty(edt_desc.getText().toString())) {
            edt_desc.setError("cant be empty");
        }
        if(TextUtils.isEmpty(edt_quantity.getText().toString())) {
            edt_quantity.setError("cant be empty");
        }

        ArrayAdapter<String> adapter= new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,arr);
        adapter.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        edt_type.setAdapter(adapter);
        edt_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvtype.setText(arr[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performFileSearch();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduct();
            }
        });
    }
    public void performFileSearch() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode==0) {
            // Get the Image from data
            Uri selectedImage = data.getData();

            Cursor cursor = getContentResolver().query(selectedImage, null, null, null, null);
            if (cursor != null){

            }
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            mediaPath = cursor.getString(columnIndex);
            // Set the Image in ImageView for Previewing the Media
            img_product.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
            cursor.close();

        }else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }
    private void addProduct () {

        Ion.with(getApplicationContext())
                .load("http://192.168.43.198:3000/products")
                .setMultipartParameter("price",edt_price.getText().toString() )
                .setMultipartParameter("quatity", edt_quantity.getText().toString())
                .setMultipartParameter("description", edt_desc.getText().toString())
                .setMultipartParameter("name", edt_name_product.getText().toString())
                .setMultipartParameter("type", tvtype.getText().toString())
                .setMultipartFile("productImage", "application/*", new File(mediaPath))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                    }
                });
    }
}