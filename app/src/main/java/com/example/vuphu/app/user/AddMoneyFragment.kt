package com.example.vuphu.app.user

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.example.vuphu.app.AcsynHttp.AsyncHttpApi
import com.example.vuphu.app.Dialog.notyfi
import com.example.vuphu.app.R
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams

import org.json.JSONObject

import cz.msebera.android.httpclient.Header


class AddMoneyFragment : Fragment() {


    private var numbercard: EditText? = null

    private var addmoney: Button? = null

    private var pre: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_money, container, false)

        numbercard = view.findViewById(R.id.edt_card_number)
        addmoney = view.findViewById(R.id.btn_add_money)

        pre = activity!!.getSharedPreferences("data", Context.MODE_PRIVATE)

        addmoney!!.setOnClickListener {
            if (TextUtils.isEmpty(numbercard?.text.toString())) {
                numbercard?.error= "cant be empty"
            } else {
                addCast(pre!!.getString("token", ""), numbercard?.text.toString())
            }

        }
        return view
    }

    private fun addCast(token: String?, num: String) {
        val params = RequestParams()
        params.put("balance",num.toInt())

        AsyncHttpApi.post(token, "/account/deposit", params, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                val no = notyfi(activity)
                no.setText("deposit done !")
                no.setIcon(R.drawable.ic_add_money)
                no.show();
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
