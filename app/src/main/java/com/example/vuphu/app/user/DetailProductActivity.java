package com.example.vuphu.app.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vuphu.app.AcsynHttp.NetworkConst;
import com.example.vuphu.app.R;
import com.example.vuphu.app.object.Product;
import com.squareup.picasso.Picasso;

public class DetailProductActivity extends AppCompatActivity {

    private TextView desprition, price ;

    private ImageView imageView;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        final String id = intent.getStringExtra("productID");
        final String name = intent.getStringExtra("productNAME");
        final int priceget = intent.getIntExtra("productPRICE",0);
        int soluong = intent.getIntExtra("productQUANLITY",0);
        String pathImage = intent.getStringExtra("productIMAGE");
        String des = intent.getStringExtra("productDES");

        setTitle(name);

        imageView = findViewById(R.id.img_detail_product);
        Picasso.get().load(NetworkConst.network+"/"+pathImage.replace("\\","/")).error(R.drawable.ic_terrain_black_24dp).placeholder(R.drawable.mypham).into(imageView);

        price = findViewById(R.id.tv_price);
        price.setText("Giá: "+priceget+" đ");
        desprition = findViewById(R.id.tv_description);
        desprition.setText(des);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BuyProductActivity.class);
                intent.putExtra("productId",id );
                intent.putExtra("productName",name );
                intent.putExtra("productPrice",priceget );

                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

}
