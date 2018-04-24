package com.example.vuphu.app.user.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vuphu.app.R;
import com.example.vuphu.app.object.ProductShow;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private ArrayList<ProductShow> productInCarts ;

    public CartAdapter (ArrayList<ProductShow> productInCarts) {
        this.productInCarts =productInCarts ;
    }
    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_lisr, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        holder.name.setText("name: "+productInCarts.get(position).getName());
        holder.number.setText("quantity: "+String.valueOf(productInCarts.get(position).getQuatity()));
    }
    @Override
    public int getItemCount() {
        return productInCarts.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView number;

        public ViewHolder(View itemView) {
            super(itemView);
            name =itemView.findViewById(R.id.tv_product_cart_name);
            number =itemView.findViewById(R.id.tv_product_cart_number);
        }
    }
}
