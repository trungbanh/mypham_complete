package com.example.vuphu.app.object;

public class ProductShow {

    private String name ;
    private int num ;
    private int price ;


    public ProductShow(String name, Integer quatityBuy, Integer price) {
        this.name = name ;
        this.num = quatityBuy;
        this.price = price;
    }

    public String getName () {
        return this.name;
    }
    public int getQuatity () {
        return this.num;
    }
    public int getPrice () {return  this.price;}

    public void setNum(int num) {
        this.num = num;
    }
}
