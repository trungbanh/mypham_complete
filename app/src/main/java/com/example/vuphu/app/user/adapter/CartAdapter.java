package com.example.vuphu.app.user.adapter;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vuphu.app.R;
import com.example.vuphu.app.object.ProductShow;
import com.example.vuphu.app.user.cart.Cart;

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
    public void onBindViewHolder(@NonNull final CartAdapter.ViewHolder holder, final int position) {
        holder.name.setText("Name: "+productInCarts.get(position).getName());
        holder.number.setText("Quantity: "+String.valueOf(productInCarts.get(position).getQuatity()));

        holder.det.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart.getInstance().Delt(position);
                productInCarts.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, productInCarts.size());
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(holder.itemView.getContext());
                dialog.setContentView(R.layout.dialog_cart_edit);
                // set the custom dialog components - text, image and button
                TextView name = (TextView) dialog.findViewById(R.id.tv_product_cart_edit);
                name.setText("Name: "+productInCarts.get(position).getName());
                final EditText num = dialog.findViewById(R.id.edt_product_cart_edit);
                Button dialogButton = (Button) dialog.findViewById(R.id.dialog_submit);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int qua = Integer.parseInt(num.getText().toString());
                        Cart.getInstance().Edit(position,qua);
                        productInCarts.get(position).setNum(qua);
                        Log.i("number",String.valueOf(productInCarts.get(position).getQuatity() +" "+qua ));
                        Cart.getInstance().getListProduct();
                        holder.number.setText("Quantity: "+String.valueOf(productInCarts.get(position).getQuatity()));
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });
    }
    @Override
    public int getItemCount() {
        return productInCarts.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView number;
        private ImageView edit ,det ;

        public ViewHolder(View itemView) {
            super(itemView);
            edit = itemView.findViewById(R.id.img_edt_cart);
            det = itemView.findViewById(R.id.img_det_cart);
            name =itemView.findViewById(R.id.tv_product_cart_name);
            number =itemView.findViewById(R.id.tv_product_cart_number);
        }
    }
}
