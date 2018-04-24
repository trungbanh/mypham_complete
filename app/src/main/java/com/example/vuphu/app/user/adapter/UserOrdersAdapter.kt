package com.example.vuphu.app.user.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.vuphu.app.R
import com.example.vuphu.app.`object`.HistoryOrder

class UserOrdersAdapter {

    class orderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val tv_name: TextView
        val tv_price: TextView
        val tv_date: TextView
        private val tv_status: TextView

        init {
            tv_name = itemView.findViewById(R.id.tv_user_order_name)
            tv_date = itemView.findViewById(R.id.tv_user_order_date)
            tv_price = itemView.findViewById(R.id.tv_user_order_price)
            tv_status = itemView.findViewById(R.id.tv_user_order_status)
        }
    }

    class orderAdap(internal var list: List<HistoryOrder>, internal var context: Context) : RecyclerView.Adapter<orderViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): orderViewHolder {
            val v = LayoutInflater.from(context).inflate(R.layout.item_user_order, parent, false)
            return orderViewHolder(v)
        }

        override fun onBindViewHolder(holder: orderViewHolder, position: Int) {
            holder.tv_name.text = list[position].id
            holder.tv_price.text = list[position].paid!!.toString()
            holder.tv_date.text = list[position].createAt.toString()
            //holder.tv_status.setText(list[position].getStatus());


        }

        override fun getItemCount(): Int {
            return list.size
        }
    }
}
