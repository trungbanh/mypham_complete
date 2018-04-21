package com.example.vuphu.app.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductInCart {

    @SerializedName("product")
    @Expose
    private String productid;
    @SerializedName("product")
    @Expose
    private String product;
    @SerializedName("quatityBuy")
    @Expose
    private Integer quatityBuy;

    public ProductInCart(String id ,int num) {
        this.productid = id ;
        this.quatityBuy = num ;
    }

    public String getproductid() {
        return productid;
    }


    public void setproduct(String productid) {
        this.productid = productid;
    }

    public Integer getQuatityBuy() {
        return quatityBuy;
    }

    public void setQuatityBuy(Integer quatityBuy) {
        this.quatityBuy = quatityBuy;
    }

}