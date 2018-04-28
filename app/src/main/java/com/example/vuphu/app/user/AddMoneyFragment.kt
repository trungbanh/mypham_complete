package com.example.vuphu.app.user

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.example.vuphu.app.AcsynHttp.AsyncHttpApi
import com.example.vuphu.app.AcsynHttp.NetworkConst
import com.example.vuphu.app.Dialog.notyfi
import com.example.vuphu.app.R
import com.example.vuphu.app.R.string.money
import com.example.vuphu.app.`object`.listOrder
import com.example.vuphu.app.`object`.order
import com.google.gson.Gson
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.squareup.picasso.Picasso

import org.json.JSONObject

import cz.msebera.android.httpclient.Header


class AddMoneyFragment : Fragment() {


    private var numbercard: EditText? = null
    private var seri: EditText? = null
    private var code: EditText? = null

    private var addmoney: Button? = null

    private var pre: SharedPreferences? = null
    private var edit: SharedPreferences.Editor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_money, container, false)

        numbercard = view.findViewById(R.id.edt_card_number)
        seri = view.findViewById(R.id.edt_card_seri)
        code = view.findViewById(R.id.edt_money)

        addmoney = view.findViewById(R.id.btn_add_money);

        pre = activity!!.getSharedPreferences("data", Context.MODE_PRIVATE)
        edit = pre?.edit()

        addmoney!!.setOnClickListener {
            if (TextUtils.isEmpty(numbercard?.text.toString())) {
                numbercard?.error= "cant be empty"
            }
            if (TextUtils.isEmpty(seri?.text.toString()) ) {
                seri?.error= "cant be empty"
            }
            if (seri?.text!!.length !=13) {
                seri?.error= "not enough 13 element "
            }
            if (TextUtils.isEmpty(code?.text.toString()) ) {
                code?.error= "cant be empty"
            }
            if (code?.text!!.length !=13) {
                code?.error= "not enough 13 element"
            }
            else {
                addCast(pre!!.getString("token", ""), numbercard?.text.toString())
            }


        }
        return view
    }

    internal fun getToken(token: String?) {
        AsyncHttpApi.get(token, "/account", null, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                val gson = Gson()
                var order = gson.fromJson(response!!.toString(), listOrder::class.java)
                val money = order.getBalanced()!!
                edit?.putInt("money", money!!)
                edit?.commit()
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, throwable: Throwable, errorResponse: JSONObject?) {
                Log.i("fail", errorResponse!!.toString())
            }
        })
    }

    private fun addCast(token: String?, num: String) {
        val params = RequestParams()
        params.put("balance",num.toInt())
        AsyncHttpApi.post(token, "/account/deposit", params, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                val no = notyfi(activity)
                no.setText("deposit done !")
                no.show();
                getToken(pre?.getString("token",""))
            }
        })
    }
    companion object {
        fun newInstance(): AddMoneyFragment {
            val fragment = AddMoneyFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
