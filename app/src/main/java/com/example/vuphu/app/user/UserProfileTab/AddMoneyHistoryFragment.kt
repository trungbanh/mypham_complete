package com.example.vuphu.app.user.UserProfileTab

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.vuphu.app.AcsynHttp.AsyncHttpApi
import com.example.vuphu.app.R
import com.example.vuphu.app.`object`.HistoryDeposit
import com.example.vuphu.app.`object`.listOrder
import com.example.vuphu.app.user.adapter.AddMoneyAdapter
import com.google.gson.Gson
import com.loopj.android.http.JsonHttpResponseHandler

import org.json.JSONObject

import java.util.ArrayList

import cz.msebera.android.httpclient.Header


class AddMoneyHistoryFragment : Fragment() {
    private var list: List<HistoryDeposit>? = null
    private var list_add: RecyclerView? = null
    internal lateinit var gridLayoutManager: LinearLayoutManager
    private var pre: SharedPreferences? = null

    internal lateinit var order: listOrder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_add_money_history, container, false)


        list_add = v.findViewById(R.id.list_user_add_money)

        gridLayoutManager = LinearLayoutManager(context)

        pre = activity!!.getSharedPreferences("data", Context.MODE_PRIVATE)

        getToken(pre!!.getString("token", ""))

        return v
    }

    internal fun getToken(token: String?) {
        AsyncHttpApi.get(token, "/account", null, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                //Log.i("onsucess",response.toString());
                val gson = Gson()
                order = gson.fromJson(response!!.toString(), listOrder::class.java!!)
                if (order.historyDeposit.size <= 0) {
                } else {
                    list = order.historyDeposit
                    Log.i("listhis", list!![0].numberDeposit!!.toString())
                    list_add!!.layoutManager = gridLayoutManager
                    list_add!!.setHasFixedSize(true)
                    list_add!!.isNestedScrollingEnabled = true
                    val adap = context?.let { AddMoneyAdapter.orderAdap(list as MutableList<HistoryDeposit>, it) }
                    list_add!!.adapter = adap
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>?, throwable: Throwable, errorResponse: JSONObject?) {
                Log.i("fail", errorResponse!!.toString())
            }
        })
    }
    companion object {
        public fun newInstance(): AddMoneyHistoryFragment {
            val fragment = AddMoneyHistoryFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }

    }

}
