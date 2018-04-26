package com.example.vuphu.app.user

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.example.vuphu.app.AcsynHttp.AsyncHttpApi
import com.example.vuphu.app.MainActivity
import com.example.vuphu.app.R
import com.example.vuphu.app.`object`.Payment
import com.example.vuphu.app.`object`.ProductInCart
import com.example.vuphu.app.`object`.listOrder
import com.example.vuphu.app.user.cart.Cart
import com.google.gson.Gson
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams

import org.json.JSONObject

import cz.msebera.android.httpclient.Header

class BuyProductActivity : AppCompatActivity() {

    private var productsName: TextView? = null
    private var price: TextView? = null
    private var quantity: TextView? = null
    private var sumary: TextView? = null
    private var sub: ImageView? = null
    private var add: ImageView? = null
    private var buy: Button? = null

    var money: Int? =null

    private var pre: SharedPreferences? = null
    private var edit: SharedPreferences.Editor? = null

    internal var no = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_product)

        init()

        pre = getSharedPreferences("data", Context.MODE_PRIVATE)
        edit = pre!!.edit()

        val intent = intent

        val name = intent.getStringExtra("productName")
        val priceget = intent.getIntExtra("productPrice",0)
        val id = intent.getStringExtra("productId")

        if (name == null) {
            Log.i("null", "fadfasf")
        }

        getToken(pre!!.getString("token",""))

        productsName!!.text = name
        price!!.text = priceget.toString() + ""

        sub!!.setOnClickListener {
            if (no == 1) {
                Toast.makeText(this@BuyProductActivity, "munber can't lower than 1", Toast.LENGTH_SHORT).show()
            } else {
                no--
                quantity!!.text = no.toString()
                sumary!!.text = (no * priceget).toString()
            }
        }

        add!!.setOnClickListener {
            no++
            quantity!!.text = no.toString()
            sumary!!.text = (no * priceget).toString()
        }
        buy!!.setOnClickListener {
            val check = money?.minus(Integer.parseInt(quantity!!.text.toString()).times(no))
            if (check?.compareTo(0)!! <0) {
                Toast.makeText(this@BuyProductActivity, "no enough money", Toast.LENGTH_SHORT).show()
            } else {
                //add to cart
                Cart.getInstance().addProduct( ProductInCart(id,no),name)
                Toast.makeText(applicationContext,"add to list complete !!!",Toast.LENGTH_SHORT).show()
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                //postPostOrder(product.id, no)
            }
        }
    }
    internal fun getToken(token: String?) {
        AsyncHttpApi.get(token, "/account", null, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                val gson = Gson()
                var order = gson.fromJson<listOrder>(response!!.toString(), listOrder::class.java!!)
                money = order.balanced
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, throwable: Throwable, errorResponse: JSONObject?) {
                Log.i("fail", errorResponse!!.toString())
            }
        })
    }
    private fun init() {
        productsName = findViewById(R.id.tv_buy_name)
        price = findViewById(R.id.tv_buy_price)
        quantity = findViewById(R.id.tv_buy_count)
        sumary = findViewById(R.id.tv_sum_money)
        sub = findViewById(R.id.usr_sub_order)
        add = findViewById(R.id.usr_add_order)
        buy = findViewById(R.id.btn_buy)
    }



}
