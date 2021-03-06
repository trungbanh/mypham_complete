package com.example.vuphu.app.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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

import com.example.vuphu.app.AcsynHttp.AsyncHttpApi;
import com.example.vuphu.app.AcsynHttp.NetworkConst;
import com.example.vuphu.app.Dialog.notyfi;
import com.example.vuphu.app.R;
import com.example.vuphu.app.object.Product;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;

import cz.msebera.android.httpclient.Header;

public class AdminEditProductActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;
    protected Uri uri;
    private EditText edt_name_product, edt_desc, edt_quantity, edt_price;
    private TextView select;
    private Spinner type_product;
    private ImageView img_product;
    private Product product;
    private ArrayAdapter<String> listType;
    private Button btn_update;
    private SharedPreferences pre;
    private String arr[] = {
            "lotion",
            "hair care",
            "skin care cosmetics",
            "perfume",
            "lipstick"};

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.admin_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        init();
        type_product.setAdapter(listType);
        pre = getSharedPreferences("data", MODE_PRIVATE);
        Intent intent = getIntent();

        product = new Product();
        if (intent != null) {
            product.setId(intent.getStringExtra("productID"));
            product.setDescription(intent.getStringExtra("productPRICE"));
            product.setName(intent.getStringExtra("productNAME"));
            product.setQuatity(intent.getIntExtra("productQUATITY", 0));
            product.setDescription(intent.getStringExtra("productDES"));
            product.setProductImage(intent.getStringExtra("productIMAGE"));
        }
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
        edt_name_product = findViewById(R.id.edt_admin_name_product);

        edt_desc = findViewById(R.id.edt_admin_product_content);
        edt_quantity = findViewById(R.id.edt_admin_quantity_product);
        type_product = findViewById(R.id.spinner_type_product);
        img_product = findViewById(R.id.img_admin_edit_product);
        btn_update = findViewById(R.id.btn_admin_edit_product);
        edt_price = findViewById(R.id.edt_admin_price_product);
        select = findViewById(R.id.tv_select);
        listType = new ArrayAdapter<>(this.getApplicationContext(),
                android.R.layout.simple_spinner_item, arr);
        listType.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
    }

    private void setDataType() {
        setTitle(product.getName());
        edt_name_product.setText(product.getName());
        edt_quantity.setText(String.valueOf(product.getQuatity()));
        select.setText(product.getType());
        edt_price.setText(product.getPrice().toString());
        edt_desc.setText(product.getDescription());
        Picasso.get().load(NetworkConst.network + "/" + product.getProductImage()
                .replace("\\", "/")).error(R.drawable.ic_terrain_black_24dp)
                .placeholder(R.drawable.mypham).into(img_product);


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateProduct();
            }
        });
        type_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select.setText(arr[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            uri = data.getData();
            Log.i("link", uri.toString());
            try {
                final InputStream imageStream = getContentResolver().openInputStream(uri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                img_product.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    private void UpdateProduct() {
        String price = edt_price.getText().toString();
        String quality = edt_quantity.getText().toString();
        String des = edt_desc.getText().toString();
        String name = edt_name_product.getText().toString();
        String tvType = select.getText().toString();

        if (TextUtils.equals(price, "")) {
            Toast.makeText(this, "Price is empty!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.equals(quality, "")) {
            Toast.makeText(this, "Quality is empty!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.equals(des, "")) {
            Toast.makeText(this, "Description is empty!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.equals(name, "")) {
            Toast.makeText(this, "Product name is empty!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.equals(tvType, "")) {
            Toast.makeText(this, "Type is empty!", Toast.LENGTH_SHORT).show();
        } else {
            RequestParams params = new RequestParams();
            params.put("name", edt_name_product.getText().toString());
            params.put("quatity", edt_quantity.getText().toString());
            params.put("price", edt_price.getText().toString());
            params.put("description", edt_desc.getText().toString());
            params.put("type", select.getText().toString());

            AsyncHttpApi.put(pre.getString(NetworkConst.token, ""), "/products/"
                    + product.getId(), params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    response.toString();
                    try {
                        if (response.get("message").toString().equals("Product updated!")) {
                            notyfi no = new notyfi(AdminEditProductActivity.this);
                            no.setText("edit product complete !!!");
                            no.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
