package com.example.vuphu.app.admin.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vuphu.app.AcsynHttp.AsyncHttpApi;
import com.example.vuphu.app.R;
import com.example.vuphu.app.object.order;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class AdminOrdersAdapter {

    private static class orderViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_product, tv_quantity, tv_status;
        private ImageButton btn_paid, btn_delete;

        public orderViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_admin_order_name);
            tv_quantity = itemView.findViewById(R.id.tv_admin_order_quantity);
            tv_product = itemView.findViewById(R.id.tv_admin_order_product);
            tv_status = itemView.findViewById(R.id.tv_admin_order_status);
            btn_paid = itemView.findViewById(R.id.btn_admin_order_check_paid);
            btn_delete = itemView.findViewById(R.id.btn_admin_order_delete);
        }
    }

    public static class orderAdap extends RecyclerView.Adapter<orderViewHolder> {
        ArrayList<order> list;
        Context context;
        String token;

        public orderAdap(ArrayList<order> list, Context context, String token) {
            this.list = list;
            this.context = context;
            this.token = token;
        }

        @Override
        public orderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.admin_item_order, parent, false);
            orderViewHolder vh = new orderViewHolder(v);

            return vh;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBindViewHolder(orderViewHolder holder, final int position) {
            holder.tv_name.setText(list.get(position).get_id());
            holder.tv_product.setText(list.get(position).getProduct());
            holder.tv_quantity.setText(list.get(position).getQuatityBuy());
            holder.tv_status.setText(list.get(position).getStatus());

            if (list.get(position).getStatus().equals("paid")) {
                ColorStateList csl = AppCompatResources.getColorStateList(context, R.color.colorPrimary);
                holder.btn_paid.setImageTintList(csl);
            } else {
                ColorStateList csl = AppCompatResources.getColorStateList(context, android.R.color.black);
                holder.btn_paid.setImageTintList(csl);
            }
            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new AlertDialog.Builder(context)
                            .setTitle("Delete item ?")
                            .setMessage("are your really want to delete ?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    AsyncHttpApi.delete(token, "/orders/" + list.get(position).get_id(), new JsonHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                            super.onSuccess(statusCode, headers, response);
                                            list.remove(position);
                                            AdminOrdersAdapter.orderAdap.super.notifyDataSetChanged();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }
            });
            holder.btn_paid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    list.get(position).setStatus("paid");
                    RequestParams params = new RequestParams();
                    params.put("status", list.get(position).getStatus());
                    AsyncHttpApi.put(token, "/orders/" + list.get(position).get_id(), params
                            , new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);
                                    Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                    AdminOrdersAdapter.orderAdap.super.notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}
