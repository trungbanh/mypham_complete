package com.example.vuphu.app.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vuphu.app.AcsynHttp.NetworkConst;
import com.example.vuphu.app.R;
import com.example.vuphu.app.object.Product;
import com.example.vuphu.app.user.DetailProductActivity;
import com.squareup.picasso.Picasso;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vuphu on 3/31/2018.
 */

public class ProductApdater {


    private static class productViewHolder extends RecyclerView.ViewHolder{

        private ImageView img_product;
        private TextView tv_name, tv_price;
        public productViewHolder(View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product);
            tv_name = itemView.findViewById(R.id.tv_name_product);
            tv_price = itemView.findViewById(R.id.tv_price);

        }
    }

    public static class productAdap extends RecyclerView.Adapter<productViewHolder>{

        ArrayList<Product> list;
        Context context;
        public productAdap(ArrayList<Product> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public productViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_product, null);
            return new productViewHolder(v);
        }

        @Override
        public void onBindViewHolder(productViewHolder holder,final int position) {
            holder.tv_name.setText(list.get(position).getName());
            holder.tv_price.setText(String.valueOf(list.get(position).getPrice())+" $");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(context, DetailProductActivity.class);
                        intent.putExtra("productID",list.get(position).getId());
                        intent.putExtra("productNAME",list.get(position).getName());
                        intent.putExtra("productPRICE",list.get(position).getPrice());
                        intent.putExtra("productTYPE",list.get(position).getType());
                        intent.putExtra("productQUANLITY",list.get(position).getQuatity());
                        intent.putExtra("productIMAGE",list.get(position).getProductImage());
                        intent.putExtra("productDES",list.get(position).getDescription());

                        context.startActivity(intent);
                    }catch (Exception e) {}
                }
            });
            Picasso.get().load(NetworkConst.network+"/"+list.get(position).getProductImage().replace("\\","/")).error(R.drawable.ic_terrain_black_24dp).placeholder(R.drawable.mypham).into(holder.img_product);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

}
