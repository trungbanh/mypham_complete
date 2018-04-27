package com.example.vuphu.app.user.cart;

import android.util.Log;

import com.example.vuphu.app.object.ProductInCart;
import com.example.vuphu.app.object.ProductShow;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Cart {
    private ArrayList<ProductInCart> productBuys ;
    private ArrayList<String> name ;
    private ArrayList<Integer> price ;

    public Cart () {
        this.productBuys = new ArrayList<>();
        name = new ArrayList<>();
        price = new ArrayList<>();
    }

    private static Cart Instance;
    public static Cart getInstance() {
        if (Instance == null) {
            Instance = new Cart();

        }
        return Instance;
    }

    public void addProduct (ProductInCart product,String name ,Integer price) {
        productBuys.add(product);
        this.name.add(name);
        this.price.add(price);
    }

    public ArrayList<ProductShow> getListProduct (){
        ArrayList<ProductShow> arrayList = new ArrayList<>();
        for (int i = 0 ; i< productBuys.size() ; i++) {
            arrayList.add(new ProductShow(name.get(i).toString(),productBuys.get(i).getQuatityBuy(),price.get(i)));
        }

        return  arrayList;
    }

    public String getPostJson () {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Log.i("log ",gson.toJson(productBuys));
        String array = gson.toJson(productBuys);

        //array =  "{ \"products\" :"+array+"}";


        return array;

    }
    public Cart resetCart () {
        return Instance = new Cart();
    }
    public int getSummary () {
        int sum =0;
        for (int i = 0; i<price.size();i++) {
            sum = sum + price.get(i);
        }
        return sum;
    }
    public void Delt (int position){
        productBuys.remove(position);
        name.remove(position);
        price.remove(position);
    }
    public void Edit (int position,int num) {
        productBuys.get(position).setQuatityBuy(num);
    }


}
