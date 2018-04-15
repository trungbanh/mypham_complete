package com.example.vuphu.app.admin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
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
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            uri = data.getData();
            Log.i("link",uri.toString());
            try {
                final InputStream imageStream = getContentResolver().openInputStream(uri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                img_product.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }
    private void addProduct () {
        File im = new File (getRealPathFromURI(uri));
        RequestBody image = RequestBody.create(MediaType.parse("image/*"),im);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"),edt_name_product.getText().toString());
        RequestBody price = RequestBody.create(MediaType.parse("text/plain"),edt_price.getText().toString());
        RequestBody quatity = RequestBody.create(MediaType.parse("text/plain"),edt_quantity.getText().toString());
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"),edt_desc.getText().toString());
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"),tvtype.getText().toString());
        ApiUtils.getAPIService().upLoadProduct(
                    "Bearer "+ pre.getString(NetworkConst.token,""),
                    image,name,price,quatity,description,type).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i("se",response.message());
                Log.i("se",response.isSuccessful()+"");
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("sf",t.getMessage());
            }
        });
    }
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}