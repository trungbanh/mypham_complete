package com.example.vuphu.app.object;

public class ProductShow {

    public ProductShow() {}

    private String name ;
    private int num ;


    public ProductShow(String name, Integer quatityBuy) {
        this.name = name ;
        this.num = quatityBuy;
    }

    public String getName () {
        return this.name;
    }
    public int getQuatity () {
        return this.num;
    }
}
