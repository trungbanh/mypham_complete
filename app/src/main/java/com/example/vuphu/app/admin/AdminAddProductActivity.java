package com.example.vuphu.app.admin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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
import com.example.vuphu.app.Dialog.notyfi;
import com.example.vuphu.app.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;

public class AdminAddProductActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 100;
    protected Uri uri;

    private String mediaPath;
    private EditText edt_name_product, edt_price, edt_desc, edt_quantity;
    private TextView tvtype;
    private Spinner edt_type;
    private ImageView img_product;
    private FloatingActionButton btn_add_img;
    private Button btn_add;

    private String arr[] = {
            "Lotion",
            "Hair care",
            "Skin care cosmetics",
            "Perfume",
            "Lipstick"};

    private SharedPreferences pre;
    private ProgressDialog progressBar;
    private String typeProduct;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product);
        init();

        Toolbar toolbar = (Toolbar) findViewById(R.id.admin_add_product_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        setTitle("Add product");
        progressBar = new ProgressDialog(this);
        progressBar.setMessage("Đang xử lí...");
        pre = getSharedPreferences("data", MODE_PRIVATE);
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
        btn_add_img = findViewById(R.id.btn_admin_add_image);
        btn_add = findViewById(R.id.btn_admin_add_product);
        edt_type = findViewById(R.id.spinner_add_type_product);
        img_product = findViewById(R.id.img_admin_add_product);
        tvtype = findViewById(R.id.tv_type);
    }

    private void setDataType() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        edt_type.setAdapter(adapter);
        edt_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeProduct = arr[position];
                tvtype.setText(typeProduct);
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
        if (resultCode == RESULT_OK && requestCode == 0) {
            // Get the Image from data
            Uri selectedImage = data.getData();
            Cursor cursor = getContentResolver().query(selectedImage, null, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            mediaPath = cursor.getString(columnIndex);
            // Set the Image in ImageView for Previewing the Media
            img_product.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
            cursor.close();

        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    private void addProduct() {
        String price = edt_price.getText().toString();
        String quality = edt_quantity.getText().toString();
        String des = edt_desc.getText().toString();
        String name = edt_name_product.getText().toString();

        if (TextUtils.equals(name, "")) {
            Toast.makeText(this, "Product name is empty!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.equals(price, "")) {
            Toast.makeText(this, "Price is empty!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.equals(quality, "")) {
            Toast.makeText(this, "Quality is empty!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.equals(des, "")) {
            Toast.makeText(this, "Introduce is empty!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.equals(typeProduct, "")) {
            Toast.makeText(this, "Type product is empty!", Toast.LENGTH_SHORT).show();
        } else if (typeProduct == null) {
            Toast.makeText(this, "Type product is null!", Toast.LENGTH_SHORT).show();
        } else if (mediaPath == null){
            Toast.makeText(this, "Image product is null!", Toast.LENGTH_SHORT).show();
        }
        else {
            Ion.with(getApplicationContext())
                    .load(NetworkConst.network + "/products")
                    .setMultipartParameter("price", price)
                    .setMultipartParameter("quatity", quality)
                    .setMultipartParameter("description", des)
                    .setMultipartParameter("name", name)
                    .setMultipartParameter("type", tvtype.getText().toString())
                    .setMultipartFile("productImage", "application/*", new File(mediaPath))
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            if (result.isJsonObject()) {
                                notyfi no = new notyfi(AdminAddProductActivity.this);
                                no.setText("post product sucess !!!");
                                no.show();
                            }
                        }
                    });
        }
    }
}